import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

import javax.imageio.ImageIO;

/**
 * Class that contains methods for processing the input URL (including determining the
 * file type of the URL page, and also handling the printing of URL information and
 * printing to / creation of output files to hold the contents of the URL.
 * 
 * @author Steven Wojsnis
 *
 */
public class ProcessURL {
	static int i = 0; //Used for naming the created files
	static String outputFileForInfo; //The output file specified by the user
	
	/**
	 * Constructor for ProcessURL class, assigns the value of the output file to this class' static variable
	 * @param outputFile : output file specified by user
	 */
	public ProcessURL(String outputFile){
		
		outputFileForInfo = outputFile;
	}
	
	/**
	 * Method that finds various types of information for any given URL. That information is
	 * written to the output file via FileWriter, BufferedWriter, and PrintWriter.
	 * 
	 * NOTE: CREDIT TO DAVID FLANAGAN (following is his source code):
	 * 
	 * // Display the URL address, and information about it.
	 *
	 *	System.out.println(uc.getURL().toExternalForm() + &quot;:&quot;);
	 *
	 *	System.out.println(&quot; Content Type: &quot; + uc.getContentType());
	 *
	 *	System.out.println(&quot; Content Length: &quot; + uc.getContentLength());
	 *
	 *	System.out.println(&quot; Last Modified: &quot; + new Date(uc.getLastModified()));
	 *
	 *	System.out.println(&quot; Expiration: &quot; + uc.getExpiration());
	 *
	 *	System.out.println(&quot; Content Encoding: &quot; + uc.getContentEncoding());
	 * 
	 * @param uc : The connection of the URL of which we are obtaining information
	 * @throws IOException
	 */
	public void getURLinfo(URLConnection uc) throws IOException{
		
		File file = new File(outputFileForInfo); //Creates new instance of a file, referring to output file
		
		try{
			
			//These are used to write to the output file. FileWriter feeds into BufferedWriter, which feeds
			//into PrintWriter (which is ultimately used to print to the file).
			FileWriter fw = new FileWriter (file.getAbsolutePath(), true);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);
			
			//Printing various types of URL information to output file
			out.println(uc.getURL().toExternalForm()+ ":");
			out.println(" Content Type: " + uc.getContentType());
			out.println(" Content Length: " + uc.getContentLength());
			out.println(" Last Modified: " + new Date(uc.getLastModified()));
			out.println(" Expiration: " + uc.getExpiration());
			out.println(" Content Encoding: " + uc.getContentEncoding());
			out.println();
			out.println();
			
			//Closing the streams
			out.close();
			bw.close();
			fw.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Method used to determine what kind of file type that URL contains.
	 * Achieves this by checking the last 4 characters (or 5, in the case of .html)
	 * to determine file type.
	 * 
	 * Returns the file type.
	 * 
	 * Returns "invalid" if file isn't any of the provided types.
	 * 
	 * @param url : String version of the URL whose file type is being determined
	 * @return The file type, either html,htm,jpeg, etc. Returns invalid if none of the provided types
	 */
	public String determineUrlType(String url){
		
		//Straightforward string checking. Uses substrings to find the end of each URL.
		if(url.substring(url.length()-5).equals(".html"))
			return "html";
		else if(url.substring(url.length()-5).equals(".jpeg"))
			return "jpeg";
		else if(url.substring(url.length()-4).equals(".htm"))
			return "htm";
		else if(url.substring(url.length()-4).equals(".jpg"))
			return "jpg";
		else if(url.substring(url.length()-4).equals(".txt"))
			return "txt";
		else if(url.substring(url.length()-4).equals(".gif"))
			return "gif";
		else if(url.substring(url.length()-4).equals(".pdf"))
			return "pdf";
		else return "invalid";
	}
	
	/**
	 * Chooses an appropriate action(whether to print file contents, or save image/pdf to file)
	 * to perform on the contents of the URL being processed.
	 * 
	 * Does this by using the file type of the contents of the URL, which was determined using
	 * the determineUrlType method.
	 * 
	 * Calls the appropriate method once appropriate action is determined.
	 * 
	 * @param url : String version of the url being processed.
	 * @param urlType : Type of the file contained in the URL
	 * @param conn : Connection to the URL being processed.
	 */
	public void actionChooser(String url, String urlType, URLConnection conn){
		try{
			
			//Straightforward comparisons, determining which method to call based on the
			//type of the URL being processed.
			
			//Does nothing if the urlType isn't handled by program - won't perform any action
			if(urlType.equals("invalid"));//do nothing
			else if(urlType.equals("html"))
				printTextOfFile(conn, url, urlType);//do this
			else if(urlType.equals("jpeg"))
				printImageOfFile(url, urlType);//do this
			else if(urlType.equals("htm"))
				printTextOfFile(conn, url, urlType);//do this
			else if(urlType.equals("jpg"))
				printImageOfFile(url, urlType);//do this
			else if(urlType.equals("txt"))
				printTextOfFile(conn, url, urlType);//do this
			else if(urlType.equals("gif"))
				printImageOfFile(url, urlType);//do this
			else if(urlType.equals("pdf"))
				printPDFtoFile(conn);//do this

			} catch(IOException e){
				e.printStackTrace();
			}
	}
	/**
	 * Creates a file and stores the contents of a URL in that file.
	 * Note: This will be called for files of the types: .html, .htm, .txt
	 * The file that is created will also be the same type as the URL page it was taken from.
	 * 
	 * NOTE: Part of this method (printing out the contents of file) was inspired by David Flanagan's following code:
	 * 
	 * // Read and print out the first five lines of the URL.
	 *
	 *	System.out.println(&quot;First five lines:&quot;);
	 *
	 *	DataInputStream in = new DataInputStream(uc.getInputStream());
	 *
	 *	for(int i = 0; i &lt; 5; i++) {
	 *
	 *		String line = in.readLine();
	 *
	 *		if (line == null) break;
	 *
	 *		System.out.println(&quot; &quot; + line);
	 *
	 *	} // for
	 * 
	 * @param uc : Connection to the URL being evaluated
	 * @param urlString : URL in string form
	 * @param urlType : Type of the contents contained in the URL being evaluated 
	 * @throws IOException
	 */
	public void printTextOfFile(URLConnection uc, String urlString, String urlType) throws IOException{
		
		//Checks if sub-directory exists
		File folder = new File("retrieved_pages");
		if(!folder.exists())
			new File("retrieved_pages").mkdirs();
		
		//Creates a new file with appropriate type, and according to static counter i
		File file = new File("retrieved_pages/retrieved_file_"+i++ +"."+urlType);
		int lineCounter = 0;
		
		//Used to write to the newly created file. FileWriter feeds into BufferedWriter, which feeds into PrintWriter
		FileWriter fw = new FileWriter (file.getAbsolutePath());
		BufferedWriter bw = new BufferedWriter (fw);
		PrintWriter out = new PrintWriter (bw);
		
		//Initiates a BufferedReader to read in the contents of the URL
		BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		String line;
		
		//Loop that reads in a line from the BufferedReader(of the URL) and prints it to the newly
		//created file.
		while((line = br.readLine()) != null ) {
			lineCounter++;
			out.println(" " + line);

		} // for
		
		//Closing the streams
		br.close();
		out.close();
		bw.close();
		fw.close();
		
		//Method that prints clerical information to output file
		printNewFileInfo(urlString, file.toString(), lineCounter);
	}
	
	/**
	 * Saves the image obtained from the URL being processed in its own newly
	 * created file with type corresponding to the image.
	 * 
	 * Uses the ImageIO class to read, as well as write and save the image from the URL in the new file.
	 * 
	 * @param urlString : URL being processed in string form
	 * @param imageType : Type of the image obtained from the URL being processed
	 * @throws IOException
	 */
	public void printImageOfFile(String urlString, String imageType) throws IOException{
		
		//Checks if sub-directory exists
			File folder = new File("retrieved_pages");
			if(!folder.exists())
				new File("retrieved_pages").mkdirs();
		
		//Recreates URL from the urlString
		URL url = new URL(urlString);
		
		//Creates a BufferedImage of the image from the URL using ImageIO
		//Writes that BufferedImage to the newly created file, again using ImageIO
		try{
			BufferedImage image = ImageIO.read(url);
			File file = new File("retrieved_pages/retrieved_file_"+i++ +"."+imageType); //Creates new file with proper type, using static int i
			ImageIO.write(image, imageType, file);
			
			//Method that prints clerical information to output file
			//Note, we pass through a -1 for the lines read argument, because no lines are read for images
			printNewFileInfo(urlString, file.toString(), -1);
			
		}catch(IllegalArgumentException e){
			System.out.println("Error processing "+ urlString + " detected file type doesn't match actual type");
		}
		
	}
	/**
	 * Saves the PDF file obtained from the URL being processed in its own newly
	 * created file with .pdf extension.
	 * 
	 * Note that this method makes use of byte arrays to process the PDF. PDF files cannot
	 * be read/written with a FileWriter or reader, as was done for the printTextOfFile method,
	 * as this will corrupt the PDF file.
	 * 
	 * Instead, the PDF file from the URL must be saved as a byte array, using an InputStream,
	 * and then saved to the new file, from the byte array, using OutputStream.
	 * 
	 * @param conn : Connection to the URL being processed
	 */
	public void printPDFtoFile(URLConnection conn){
		
		//Checks if sub-directory exists
			File folder = new File("retrieved_pages");
			if(!folder.exists())
				new File("retrieved_pages").mkdirs();
		
		//Creates new file with .pdf as extension, using static int i to distinguish from other newly created files
		File file = new File("retrieved_pages/retrieved_file_"+i++ +".pdf");
		String urlString = conn.getURL().toString();
		
		try{
			//InputStream obtained from connection to URL - used to read in the bytes of the PDF file
			InputStream connInputStream = conn.getInputStream();	
			//OutputStream which will be used to write to the newly created .pdf file
			OutputStream out = new FileOutputStream(file);
			
			//Byte array that will be used as a buffer to transfer the contents of the pdf 
			//from the inputStream to the OutputStream
	        byte[] buffer = new byte[1024];

	        //Initialize the length of the buffer to 0 - if nothing is read from URL pdf file, won't
	        //enter while loop
			int currbufferLength = 0;
			
			//As long as there are more bytes to be read, connInputStream.read will return a value greater that 0
			//Specifically, it will return the number of lines it read. Thus the loop will continue until end of bytes
		    while ((currbufferLength = connInputStream.read(buffer, 0, buffer.length)) > 0) {
		    	
		    	//Here we write from the buffer to the newly created .pdf file
		    	//Note that we use currbufferLength as the len argument, because this was the number
		    	//of bytes that we wrote to the buffer, as determined by the while loop condition
		    	out.write(buffer, 0, currbufferLength);
		    	
		    	//Forces any data remaining in the buffer out - makes way for next iteration of loop
		        out.flush();
		    }
		    
		    //Close stream
			out.close();
			
			//Method that prints clerical information to output file
			//Note, we pass through a -1 for the lines read argument, because no lines are read for PDF files
			printNewFileInfo(urlString, file.toString(), -1);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints clerical information about newly created files and the contents of them to the
	 * output file.
	 * 
	 * For text-based files, will print the name of the file, and the number of lines that
	 * were written into the new file from the URL file
	 * 
	 * For image-based files and PDF files, will print the name of the file
	 * 
	 * Note, for all files (given that their type is handled by program), 
	 * this method will print the URL that was processed to obtain that file,
	 * as well as the folder in which these newly created files are being held in.
	 * 
	 * @param url : URL that was processed
	 * @param fileNameWithDirectory : Name of the newly created file (including the folder its contained in)
	 * @param numLines : Number of lines that were written (applicable to text-based files) 
	 */
	public void printNewFileInfo(String url, String fileNameWithDirectory, int numLines){
		
		//Separates the file name from the folder its contained in, using substrings
		//Note, this works because all newly created files are placed in the same
		//folder (and we know the name of said folder).
		String directory = fileNameWithDirectory.substring(0, 15);
		String fileName = fileNameWithDirectory.substring(16);
		
		//Instantiates new file, referring to the output file
		File file = new File(outputFileForInfo);
		
		try{
			
			//Writers used to write to the output file. FilerWriter feeds into BufferedWriter which
			//feeds into PrintWriter(which will write to the file)
			FileWriter fw = new FileWriter (file.getAbsolutePath(), true);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);
			
			//Prints info that applies to all types of files
			out.println("A file was created for the URL: "+url);
			out.println("The name of the new file is: " + fileName + " located in the " + directory + " folder.");
			
			//If lines were read in (note, images and pdf files will pass through a -1 for numLines)
			//then print out that number.
			if(numLines >= 0)
				out.println("Number of lines printed from page: " + numLines);
			
			out.println();
			out.println();
			
			//Close streams
			out.close();
			bw.close();
			fw.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
}
