import java.util.*;

public class CodeGenerator {
    int lastVariableNumber = 1;
    int lastFunctionNumber = 1;
    LexicalAnalyzer scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like
    ArrayList<String> globalStringsDecelerations = new ArrayList<>();
    ArrayList<String> codes = new ArrayList<>();
    Stack<String> semanticStack = new Stack<>();
    HashMap<String, String> typeMap = new HashMap<>();
    HashMap<String, String> functionSignaturesMap = new HashMap<>();
    HashMap<String, String> builtInFunctions = new HashMap<>();
    ArrayList<String> builtInFunctionsDeclarations = new ArrayList<>();
    ArrayList<String> argumentsNamesTypes = new ArrayList<>();
    ArrayList<String> argNames = new ArrayList<>();
    ArrayList<String> argTypes = new ArrayList<>();


    // Define any variables needed for code generation
    public CodeGenerator(LexicalAnalyzer scanner) {
        this.scanner = scanner;
        // initialize type mapping
        typeMap.put("number", "i32");
        typeMap.put("integer", "i32");
        typeMap.put("char", "i8");
        typeMap.put("boolean", "i1");
        typeMap.put("real", "double");
        typeMap.put("string", "i8*");

        builtInFunctions.put("write", "@printf");
        functionSignaturesMap.put("@printf", "i32 (i8*, ...)*");
        builtInFunctionsDeclarations.add("declare i32 @printf(i8*, ...)");
    }

    public void generate(String semantic) {
//        System.out.println(semantic);
        String name;
        String content;
        String value;
        int len;
        String type;
        String llvmCommand;
        switch (semantic.substring(1)) {
            case "create_const_string":
                name = String.format("@.str-%d", lastVariableNumber++);
                content = Utils.addEndToString(LexicalAnalyzer.STP);
                len = content.length() - 2 - 2;
                llvmCommand = String.format("%s =  private constant [%d x i8] c%s", name, len, content);
                globalStringsDecelerations.add(llvmCommand);
                defineLocalVariablePtr("." + name.substring(1), String.format("[%d x i8]* %s", len, name));
                semanticStack.push("i8* %." + name.substring(1));
                break;
            case "create_const_number":
                semanticStack.push(LexicalAnalyzer.STP);
                break;
            case "push_function":
                name = "@" + LexicalAnalyzer.STP;
                semanticStack.push(name);
                break;
            case "add_dollar":
                semanticStack.push("$");
                break;
            case "push_arg_id":
                name = "%" + LexicalAnalyzer.STP; //TODO: what should I named the argument?
                semanticStack.push(name);
                break;
            case "push_arg_type":
                type = LexicalAnalyzer.STP; // TODO: Do I need to use "%d"?
                semanticStack.push(type);
                break;
            case "make_arguments":
                argumentsNamesTypes.clear();
                argNames.clear();
                argTypes.clear();
                while (!semanticStack.peek().equals("$")) {
                    String argType = semanticStack.pop();
                    argTypes.add(typeMap.get(argType));
                    String argName = semanticStack.pop();
                    argNames.add(argName);
                    argumentsNamesTypes.add(typeMap.get(argType) + " " + argName);
                }
                semanticStack.pop();
                Collections.reverse(argumentsNamesTypes);
                Collections.reverse(argNames);
                Collections.reverse(argTypes);
                break;
            case "function_def":
                name = semanticStack.pop();
                type = typeMap.get(LexicalAnalyzer.STP);
                defineFunction(name, type);
                break;
            case "add_begin_block":
                codes.add("{");
                break;
            case "add_end_block":
                codes.add("}");
                break;
            case "push_id_statement":
                name = "%" + LexicalAnalyzer.STP;
                semanticStack.push(name);
                break;
            case "function_call_(":
                semanticStack.push("$");
                break;
            case "function_call_)":
                ArrayList<String> arguments = new ArrayList<>();
                while (!semanticStack.peek().equals("$")) {
                    arguments.add(semanticStack.pop());
                }
//                System.out.println(semanticStack.peek());
                semanticStack.pop();
                Collections.reverse(arguments);

                String functionName = semanticStack.pop();
                if (builtInFunctions.get(functionName) != null) {
                    functionName = builtInFunctions.get(functionName);
                }

                llvmCommand = String.format("%%func_out_%d = call %s %s(%s)", lastFunctionNumber,
                        functionSignaturesMap.get(functionName), functionName, arguments.stream().reduce((a, b) -> a + "," + b).orElse(""));
                codes.add(llvmCommand);
                semanticStack.push(String.format("func_out_%d", lastFunctionNumber++)); // TODO ++
                break;
            case "return_const":
                codes.add(String.format("ret %s %s", typeMap.get(LexicalAnalyzer.TOKEN), LexicalAnalyzer.STP));
                break;
            case "make_simple_var":
                value = semanticStack.pop();
                type = semanticStack.pop();
                name = semanticStack.pop();
                defineLocalSimpleVar(name, type, value);

            case "push_id_expr":
                name = "%" + LexicalAnalyzer.STP;
                semanticStack.push(name);
                break;
            default:
                break;

        }

    }

    private void defineFunction(String name, String type) {
        if (argumentsNamesTypes.isEmpty()){
            String llvmCommand = String.format("define %s %s", type, name);
            codes.add(llvmCommand);
            functionSignaturesMap.put(name, String.format("%s ()", type));
        }
        else
        {
            String args = new String();
            String signature = new String();
            signature += (type + " (");
            for (int i = 0; i < argumentsNamesTypes.size(); i++)
            {
                if (i == argumentsNamesTypes.size() - 1){
                    args += (argumentsNamesTypes.get(i)); // TODO: is get() alright>
                    signature += (argTypes.get(i));
                } else {
                    args += (argumentsNamesTypes.get(i) + ", "); // TODO: is get() alright>
                    signature += (argTypes.get(i) + ", ");
                }

            }
            signature += ")";
            String llvmCommand = String.format("define %s %s(%s)", type, name, args);
            codes.add(llvmCommand);
            functionSignaturesMap.put(name, signature);
        }
    }

    private void defineLocalSimpleVar(String name, String type, String value) {
        type = typeMap.get(type);
        String llvmCommand = String.format("%%%s = alloca %s, align 4", name, type);
        codes.add(llvmCommand);
        llvmCommand = String.format("store %s %s, %s* %s, align 4", type, value, type, name);
        codes.add(llvmCommand);
    }

    private void defineLocalVariablePtr(String name, String addressBaseVar) {
        String llvmCommand = String.format("%%%s = getelementptr inbounds %s, i32 0, i32 0", name, addressBaseVar);
        codes.add(llvmCommand);
    }

    public void finishCode() // You may need this
    {

    }

    public void writeOutput(String outputName) {
        // Can be used to print the generated code to output
        // I used this because in the process of compiling, I stored the generated code in a structure
        // If you want, you can output a code line just when it is generated (strongly NOT recommended!!)
        for (String funcDeclaration : builtInFunctionsDeclarations) {
            System.out.println(funcDeclaration);
        }

        for (String declaration : globalStringsDecelerations) {
            System.out.println(declaration);
        }
        for (String code : codes) {
            System.out.println(code);
        }
    }
}
