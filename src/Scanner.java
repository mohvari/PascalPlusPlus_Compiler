
import java.io.*;

/**
 * This is the template of class 'scanner'. You should place your own 'scanner class here and
 * your scanner should match this interface. 
 *
 */
public class Scanner 
{
	public String CV;
	int lineNumber = 1;
	public static int x = 0;

	Scanner(String filename) throws Exception
	{
		File f = new File(filename);
	    if (!f.exists())
	        throw new Exception ("File does not exist: " + f);
	    if (!f.isFile())
	      throw new Exception("Should not be a directory: " + f);
		if (!f.canRead())
			throw new Exception("Can not read input file: " + f);
		// ...
	}

	public String nextToken() throws Exception
	{
		if (x == 0){
			x++;
			return "function";
		}
		return "$";
	}
	
}
