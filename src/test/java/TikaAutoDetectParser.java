import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TikaAutoDetectParser {

	public static void main(String[] args) throws IOException, SAXException, TikaException {
		AutoDetectParser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();
		MP4Parser mp4Parser = new MP4Parser();
		Metadata metadata = new Metadata();
		//parser.parse(new FileInputStream(new File("/home/joern/Backup_NAS/Bilder_Sony_M4_Aqua/2009-07-26 10.39.52.jpg")), handler, metadata);
		//mp4Parser.parse(new FileInputStream(new File("/home/joern/test/imgsrc/VID_20110825_202250.3gp")), handler, metadata);
		parser.parse(new FileInputStream(new File("/home/jkarthaus/Bilder/20230627_Pixel6/PXL_20230616_205724301.TS.mp4")), handler, metadata);
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			System.out.println(name + ": " + metadata.get(name));
		}


	}

}
