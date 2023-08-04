package de.jkarthaus.tools;

import de.jkarthaus.exceptions.MetaDataParseFailException;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Singleton
@Slf4j
public class TikaContentParser {

    public static final String IMAGE_META_CREATE = "Date/Time";
    public static final String MPG_META_CREATE = "Creation-Date";
    public static final String MP4_META_CREATE = "dcterms:created";


    DateFormat dateFormat1 = new SimpleDateFormat();
    DateFormat dateFormat2 = new SimpleDateFormat("");
    DateFormat dateFormat3 = new SimpleDateFormat("");
    // meta:creation-date: 2009-07-26T12:39:52

    BodyContentHandler handler = new BodyContentHandler();
    ParseContext pcontext = new ParseContext();
    AutoDetectParser parser = new AutoDetectParser();


    /**
     * @param mediaFile
     * @return
     * @throws TikaException
     * @throws SAXException
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ParseException
     */
    @SuppressWarnings("resource")
    public LocalDateTime getMetaInfCreateDate(File mediaFile)
            throws FileNotFoundException, IOException, SAXException, TikaException, MetaDataParseFailException {

        Metadata metadata = new Metadata();
        LocalDateTime result = null;

        parser.parse(new FileInputStream(mediaFile), handler, metadata, pcontext);

        String dateString = metadata.get(Metadata.ORIGINAL_DATE);

        if (dateString == null) {
            dateString = metadata.get(MP4_META_CREATE);
        }

        if (dateString == null) {
            dateString = metadata.get(IMAGE_META_CREATE);
        }
        // Try to get MPG Date
        if (dateString == null) {
            log.debug("Exif Meta String not found trying MPG Meta Data");
            dateString = metadata.get(MPG_META_CREATE);
        }
        // Date not set in MetaData
        if (StringUtils.defaultString(dateString, "0000").startsWith("0000")) {
            log.error("Cannot find valid Date in MetaData - Use Now as Date.");
            return LocalDateTime.now();
        }
        // Date is found - try to parse

        try {
            return LocalDateTime.parse(dateString);
        } catch (Exception e) {
            log.debug("Parsing DateFormat1 failded, try DateFormat2");
        }

        try {
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"));
        } catch (Exception e) {
            log.debug("Parsing DateFormat1 failded, try DateFormat2");
        }

        // Try format 2
        try {
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        } catch (Exception e1) {
            log.debug("Parsing DateFormat2 fail, try DateFormat3");

        }
        // Try format 3
        try {
            return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        } catch (Exception e1) {
            log.debug("Parsing DateFormat3 fail");

        }

        log.error("Parsing Creation Date failed");
        throw new MetaDataParseFailException();
    }

}
