import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.jpeg.JpegParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TestJPGParser {

	public static void main(final String[] args) throws IOException, SAXException, TikaException, ParseException {

		// detecting the file type
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(new File("/home/joern/IMG_5560.JPG"));
		ParseContext pcontext = new ParseContext();

		// Jpeg Parse
		JpegParser JpegParser = new JpegParser();
		JpegParser.parse(inputstream, handler, metadata, pcontext);
		System.out.println("Contents of the document:" + handler.toString());
		System.out.println("Metadata of the document:");
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			System.out.println(name + ": " + metadata.get(name));
		}
		System.out.println("------------------------------------------------------------------");
		
		System.out.println("Date: " + metadata.get("Date/Time"));
		DateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
		Date result =  df.parse(metadata.get("Date/Time"));  
		System.out.println(result.toString());
		
	}
}