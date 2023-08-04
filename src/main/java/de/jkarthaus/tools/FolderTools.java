package de.jkarthaus.tools;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FolderTools {

    static DateTimeFormatter destFileFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss", Locale.GERMANY);
    static DateTimeFormatter level2Formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.GERMANY);

    /**
     * @param dateFromMeta
     * @return
     */
    public static String getDestFilename(String filePre, LocalDateTime ldtFromMetaData, String fileExt) {
        String result = "";
        result += "/" + ldtFromMetaData.format(destFileFormatter);
        // Add filePre
        if (filePre != null && filePre.trim().length() != 0) {
            result += "_" + filePre;
        }
        // Pretty up
        result = StringUtils.replace(result, ":", "-");
        result = StringUtils.replace(result, " ", "_");
        // Add extension
        result += "." + fileExt.trim().toLowerCase();
        // --
        return result;
    }

    /**
     * @return
     */
    public static String getLevel1String(LocalDateTime ldtFromMetaData) {
        String result = "/" + ldtFromMetaData.getYear();
        return result;
    }

    /**
     * @param imageDate
     * @return
     */
    public static String getLevel2String(LocalDateTime ldtFromMetaData) {
        String result = "";
        // Todo : Add DateInfo
        result += "/" + ldtFromMetaData.format(level2Formatter);
        // Add Manual Pre text
        result += "_[]";
        // Pretty up
        result = StringUtils.replace(result, ":", "-");
        result = StringUtils.replace(result, " ", "_");
        // --
        return result;

    }


}
