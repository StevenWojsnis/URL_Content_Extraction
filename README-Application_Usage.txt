#####################################
# Instructions on using application #
#####################################

NOTE: The program stores newly created files with the same extension of the file they were created
from, thus, for a .gif URL, a .gif file will be created.

For .html and .htm files, to view a static copy of the webpage, open the file using a browser - to
see the lines of the files, open using a text editor.

#######################
# Running the program #
#######################

Place the downloaded content (retrieved_pages folder (application will create if not present), inputURLs.txt, 
Main.java, ProcessURL.java, and outputURLinfo.txt) into a known directory and navigate to that directory in 
the command prompt.

From here, to compile the program, you should run the following command:

	javac Main.java

After this step, .class files will appear in your chosen directory. Next, to run the program,
you should run the following command:
	
	java Main inputURLs.txt outputURLinfo.txt

Technically, you can choose any files to run, but inputURLs has sample input already set up in
the correct format, and outputURLinfo is just a blank text file that will store information from
the application.

Note: The format of the input file is as follows:

	In the text file, there should be a list of URLs, with each URL on its own line.
	If multiple URLs are on one line, the application will not process any of them.

If a wrong file is used (say, another random text file), the program will attempt to process it
as if it was in the proper format - likely returning a myriad of "Invalid URL" messages to the console.

The "Invalid URL" message is built into the program to help catch any errors with the input file, and only 
means that the line being percieved as a URL won't be processed.

Note: Essentially, if this happens, the console will continue printing the "Invalid URL" message until
the text file is fully parsed - which (depending on the size of the file) could take a while. Thus, if
this happens, and you accidentally run an incorrect input file, simply press the 'Ctrl' button and 'c' button
on your keyboard simultaneously, to stop processing.

Technically, you should include the retrieved_pages in the directory your running the program from, to store
newly created files. However, if the application doesn't detect the "retrieved_pages" folder, it will create
it and store the newly created files in there.

Note for inputs: If you try to input a URL that isn't a pdf file with the extension .pdf at the end of the URL,
an unreadable pdf file will be created.


#############################
# What The Application Does #
#############################

This application takes various URLs and prints the following information about them to the output file:

	Content Type
	Content Length
	Last Modified
	Expiration
	Content Encoding

Furthermore, this application will further process URLs with the following file types:

	.html
	.htm
	.txt
	.jpg
	.jpeg
	.gif
	.pdf

For .html, .htm, and .txt, new files will be created storing the contents of the file obtained from the URL,
and the URL, new file name, and lines written into the new file will be printed in the output file.

For .jpg, .jpeg, and .gif, the image obtained from the URL will be stored is a new file with extension that
matches the image type from the URL. The URL and new file name will be written to the output file.

For .pdf, new files will be created storing the PDF file contained in the URL. The URL and new file name
will be written to the output file.

Note: For URLs with file types not supported by this application, only URL info will be printed to the output
file, no other actions will be taken.

The new files will be contained in a folder created in the directory that the program is being executed from called
"retrieved_pages" where the files will be named in a sequential manner (retrieved_file_i  -- where i is a unique
integer).
