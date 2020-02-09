import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * This is the template of class 'scanner'. You should place your own 'scanner class here and
 * your scanner should match this interface. 
 *
 */
public class LexicalAnalyzer
{
	public static String TOKEN;
	public static String STP;
	public String CV;
	int lineNumber = 1;
	public static boolean firstCall = true;
	public File fileObject;
	public Scanner fileReader;
	LexicalAnalyzer(String filename) throws Exception
	{
		this.fileObject = new File(filename);
	    if (!this.fileObject.exists())
	        throw new Exception ("File does not exist: " + this.fileObject);
	    if (!this.fileObject.isFile())
	      throw new Exception("Should not be a directory: " + this.fileObject);
		if (!this.fileObject.canRead())
			throw new Exception("Can not read input file: " + this.fileObject);

		this.fileReader = new Scanner(this.fileObject);

	}

	public String findTokenStp(String line)
	{
//		System.out.printf("this is the line\t%s\n", line);
		String[] tokenWithDescription = line.split(",");
		if (tokenWithDescription[0].equals("keyword") | tokenWithDescription[0].equals("parenthesis"))
		{
			this.TOKEN = tokenWithDescription[1];
			this.STP = tokenWithDescription[1];
//			System.out.printf("Token:%s\tSTP:%s\n", this.TOKEN, this.STP);
			return this.TOKEN;
		}
		this.TOKEN = tokenWithDescription[0];
		this.STP = tokenWithDescription[1];
//		System.out.printf("Token:%s\tSTP:%s\n", this.TOKEN, this.STP);
		return this.TOKEN;
	}

	public String nextToken() throws Exception
	{
		if(this.fileReader.hasNextLine())
		{
			if(this.firstCall)
			{
				this.firstCall = false;
				String line = this.fileReader.nextLine().substring(1);
				return this.findTokenStp(line);
			}
			String line = this.fileReader.nextLine();
			return this.findTokenStp(line);

		}
		fileReader.close();
		return "$";

//		if (x == 0){
//			x++;
//			return "function";
//		}
//		return "$";
	}
	
}
