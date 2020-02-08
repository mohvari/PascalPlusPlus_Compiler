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
	public String CV;
	int lineNumber = 1;
	public static boolean firstCall = true;
	public File fileObject;
	public Scanner fileReader;
	public String firstWrongChar;
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

	public String nextToken() throws Exception
	{
		if(this.fileReader.hasNextLine())
		{
			if(this.firstCall)
			{
				this.firstCall = false;
				String line = this.fileReader.nextLine().substring(3);
				System.out.printf("this is the line:\t%s\n", line);
				String[] tokenWithDescription = line.split(",");
				System.out.printf("This is the token\t%s\n",tokenWithDescription[0]);
				return tokenWithDescription[0];
			}
			String line = this.fileReader.nextLine();
			System.out.printf("this is the line:\t%s\n", line);
			String[] tokenWithDescription = line.split(",");
			System.out.printf("This is the token\t%s\n", tokenWithDescription[0]);

			return tokenWithDescription[0];
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
