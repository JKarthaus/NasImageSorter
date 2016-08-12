package de.jkarthaus.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

public class TikaJPGParser {

	public static final String EXIF_DATE_STRING = "Date/Time";
	DateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
	BodyContentHandler handler = new BodyContentHandler();
	ParseContext pcontext = new ParseContext();
	JpegParser JpegParser = new JpegParser();

	/**
	 * 
	 * @param jpgFile
	 * @return
	 * @throws TikaException
	 * @throws SAXException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public Date getExifCreateDate(File jpgFile)
			throws FileNotFoundException, IOException, SAXException, TikaException, ParseException {
		Metadata metadata = new Metadata();
		JpegParser.parse(new FileInputStream(jpgFile), handler, metadata, pcontext);

		String dateString = metadata.get(EXIF_DATE_STRING);
		return df.parse(dateString);
	}

}
