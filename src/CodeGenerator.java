
public class CodeGenerator 
{
    LexicalAnalyzer scanner; // This was my way of informing CG about Constant Values detected by Scanner, you can do whatever you like
    
    // Define any variables needed for code generation
    
    public CodeGenerator(LexicalAnalyzer scanner)
    {
        this.scanner = scanner;
    }

    public void generate(String semantic)
    {
    	System.out.println(semantic); // Just for debug
    	
            if (semantic.equals("NoSem"))
            	return;
            else if(semantic.equals("@push")){
                System.out.println("Hello compiler");
            }
            /*
             * else if (sem.equals("SemanticRoutine1"))
             * {
             * 	...
             * }
             * else if (sem.equals("SemanticRoutine2"))
             * {
             * 	...
             * }
             */
    }
    
    public void finishCode() // You may need this
    {

    }

    public void writeOutput(String outputName)
    {
    	// Can be used to print the generated code to output
    	// I used this because in the process of compiling, I stored the generated code in a structure
    	// If you want, you can output a code line just when it is generated (strongly NOT recommended!!)
    }
}
