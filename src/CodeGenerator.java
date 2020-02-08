import java.util.*;

public class CodeGenerator {
    int lastVariableNumber = 1;
    int lastFunctionNumber = 1;
    LexicalAnalyzer scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like
    ArrayList<String> globalStringsDeclerations = new ArrayList<>();
    ArrayList<String> codes = new ArrayList<>();
    Stack<String> semanticStack = new Stack<>();
    HashMap<String, String> typeMap = new HashMap<>();
    HashMap<String, String> functionSignaturesMap = new HashMap<>();


    // Define any variables needed for code generation
    public CodeGenerator(LexicalAnalyzer scanner) {
        this.scanner = scanner;
        // initialize type mapping
        typeMap.put("integer", "i32");
        typeMap.put("char", "i8");
        typeMap.put("boolean", "i1");
        typeMap.put("real", "double");
        typeMap.put("string", "i8*");

    }

    public void generate(String semantic) {
//        System.out.println(semantic);
        String name;
        String content;
        int value;
        String type;
        String llvmCommand;
        switch (semantic.substring(1)) {
            case "create_const_string":
                int len = 10;
                name = String.format("@.str-%d", lastVariableNumber++);
                content = Utils.addEndToString("");
                llvmCommand = String.format("%s =  private [%d x i8] c%s", name, len, content);
                globalStringsDeclerations.add(llvmCommand);
                defineLocalVariable("." + name.substring(1), String.format("[%d x i8]* %s", len, name));
                semanticStack.push("i8* %%." + name.substring(1));
                break;
            case "create_const_number":
                break;
            case "push_function":
                name = "@" + LexicalAnalyzer.STP;
                semanticStack.push(name);
                break;
            case "function_type":
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
                semanticStack.push(LexicalAnalyzer.STP);
                break;
            case "function_call_(":
                semanticStack.push("$");
                break;
            case "function_call_)":
                ArrayList<String> arguments = new ArrayList<>();
                while (!semanticStack.peek().equals("$")) {
                    arguments.add(semanticStack.pop());
                }
                semanticStack.pop();
                Collections.reverse(arguments);

                String functionName = semanticStack.pop();
                llvmCommand = String.format("%%func_out_%d = call %s %s)", lastFunctionNumber,
                        functionSignaturesMap.get(functionName), arguments.stream().reduce((a, b) -> a + "," + b).get());
                codes.add(llvmCommand);
                semanticStack.push(String.format("func_out_%d", lastFunctionNumber++));
                break;
            case "return_const":
                codes.add(String.format("ret %s %s", typeMap.get(LexicalAnalyzer.TOKEN), LexicalAnalyzer.STP));
                break;



            default:
                break;

        }

    }

    private void defineFunction(String name, String type) {
        String llvmCommand = String.format("define %s %s()", type, name);
        codes.add(llvmCommand);
        functionSignaturesMap.put(name, String.format("%s", type));
    }

    private void defineLocalVariable(String name, String addressBaseVar) {
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
        for (String declaration : globalStringsDeclerations) {
            System.out.println(declaration);
        }
        for (String code : codes) {
            System.out.println(code);
        }
    }
}
