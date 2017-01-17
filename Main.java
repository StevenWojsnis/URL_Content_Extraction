import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This program takes an input and output file as command-line arguments.
 * 
 * The input file will contain a list of URLs, each URL having its own line.
 * The output file will have information about those URLs in the input file written to it.
 * 
 * Depending on the type of the URL, this program will also write all the lines of the 
 * URL file to a newly created file, store the picture contained in the URL in its own
 * newly created file, or store the PDF file contained in the URL in its own newly created file.
 * 
 * Note: This program only performs this action on URLs of the types: .html, .htm, .txt,
 * .jpg, .jpeg, .gif, and .pdf
 * 
 * If the URL is not of any of the types listed above, then only information about the URL
 * will be printed to the output file (a new file will NOT be created with contents of URL).
 * 
 * If new file IS created (URL type is handled by program) then information about the
 * action performed will be printed to the output file (name of file, URL that was used,
 * lines of code written to new file (only if processed file was a text-based file)).
 * 
 * See README file for further information.
 * 
 * 
 * @author Steven Wojsnis
 * 
 * CS370 Software Engineering - Prof. Goldberg
 * Project 2 - Due: 11/23/16
 * Section: Mo/We 10:45-12:00 PM
 * 
 *
 */
public class Main {

	/**
	 * Driver method for this application.
	 * 
	 * Calls methods to retrieve the list of input URLs, process those
	 * URLs, and iterates through them, calling further, more specific,
	 * methods to process those URLs (from the ProcessURL class),
	 * 
	 * @param args : Input file name and Output file name
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static void main(String[] args)
		throws MalformedURLException, IOException{
		
			//Get a list of all the URLs listed in the input file
			ArrayList<String> urlList = retrieveUrlList(args[0]);
			
			//Create an iterator to iterate through that list
			Iterator<String> urlIterate = urlList.iterator();
			
			//Instantiate the ProcessURL object to be used to process URLs
			//Note, passes the output file name (args[1]) to the ProcessURL constructor
			ProcessURL processor = new ProcessURL(args[1]);
			
			//Iterates through all provided input URLs
			while(urlIterate.hasNext()){
				
				//Gets URL string for current iteration
				String urlString = urlIterate.next().toString();
				
				try{
					
					URL url = new URL(urlString); //Create URL from URL string
					URLConnection connection = url.openConnection(); //Establishes connection using URL
					processor.getURLinfo(connection); //Prints URL info
					
					//Determines appropriate action, depending on file type of URL
					processor.actionChooser(urlString, processor.determineUrlType(urlString), connection);
					
				}catch(MalformedURLException e){
					//In case an invalid URL is in the input file
					System.out.println("Invalid URL: "+urlString+". URL wasn't processed.");
				}
			}
	}
	
	/**
	 * Parses the input file, and separates it into a list of URLs used
	 * for processing
	 * 
	 * @param fileName : Name of input file
	 * @return : List of URLs
	 */
	public static ArrayList<String> retrieveUrlList(String fileName){
		
		ArrayList<String> urlList = new ArrayList<String>();
		
		try{
			//Uses these readers to parse through input file. FileReader feeds into BufferedReader
			FileReader reader = new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader);
			
			String line;
			
			//Reads all lines, each of which contains a URL - adds that URL to the list
			while((line = br.readLine()) != null){
				 urlList.add(line);
			}
			
			//Close connection
			br.close();
			
			return urlList;
			
		}catch(FileNotFoundException e){
			System.out.println("Cannot find input file with name '"+fileName+"'");
		}catch (IOException e){
			e.printStackTrace();
		}
		
		return urlList;
	}
}
