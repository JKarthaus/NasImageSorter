package de.jkarthaus.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TikaContentParser {

	public static final String IMAGE_META_CREATE = "Date/Time";
	public static final String MPG_META_CREATE = "Creation-Date";

	DateFormat dateFormat1 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
	DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

	BodyContentHandler handler = new BodyContentHandler();
	ParseContext pcontext = new ParseContext();
	AutoDetectParser parser = new AutoDetectParser();

	private static final Logger logger = LogManager.getLogger(TikaContentParser.class);

	/**
	 * 
	 * @param mediaFile
	 * @return
	 * @throws TikaException
	 * @throws SAXException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ParseException
	 */
	public Date getMetaInfCreateDate(File mediaFile)
			throws FileNotFoundException, IOException, SAXException, TikaException {
		Metadata metadata = new Metadata();
		Date result = null;

		parser.parse(new FileInputStream(mediaFile), handler, metadata, pcontext);

		String dateString = metadata.get(IMAGE_META_CREATE);
		if (dateString == null) {
			logger.debug("Exif Meta String not found trying MPG Meta Data");
			dateString = metadata.get(MPG_META_CREATE);
		}

		try {
			result = dateFormat1.parse(dateString);
		} catch (ParseException e) {
			logger.debug("Parsing DateFormat1 failded, try DateFormat2");
			try {
				result = dateFormat2.parse(dateString);
			} catch (ParseException e1) {
				logger.error("Parsing Creation Date failed try 2 Formats both failed");
				logger.error(e);
			}
		}

		return result;
	}

}
