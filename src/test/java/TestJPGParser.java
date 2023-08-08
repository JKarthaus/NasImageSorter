import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.image.JpegParser;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJPGParser {

    public static void main(final String[] args) throws IOException, SAXException, TikaException, ParseException {

        // detecting the file type
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(new File("/home/joern/test/imgsrc/MOV_0012.mp4"));
        ParseContext pcontext = new ParseContext();

        // Jpeg Parse
        JpegParser JpegParser = new JpegParser();
        //org.apache.tika.parser.video
        MP4Parser mp4Parser = new MP4Parser();

        //JpegParser.parse(inputstream, handler, metadata, pcontext);
        mp4Parser.parse(inputstream, handler, metadata, pcontext);
        System.out.println("Contents of the document:" + handler);
        System.out.println("Metadata of the document:");
        String[] metadataNames = metadata.names();

        for (String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }
        System.out.println("------------------------------------------------------------------");

        System.out.println("Date: " + metadata.get("Date/Time"));
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");
        Date result = df.parse(metadata.get("Date/Time"));
        System.out.println(result.toString());

    }
}
