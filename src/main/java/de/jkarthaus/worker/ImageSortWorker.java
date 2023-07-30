package de.jkarthaus.worker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.jkarthaus.model.ImageSortResult;
import de.jkarthaus.model.SortSource;
import de.jkarthaus.tools.ConfigTools;
import de.jkarthaus.tools.FolderTools;
import de.jkarthaus.tools.TikaContentParser;

@Singleton
public class ImageSortWorker {

    private final String[] extensions = new String[]{"jpg", "JPG", "mp4", "MP4"};

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    private final static Logger logger = LoggerFactory.getLogger(ImageSortWorker.class);

    @Inject
    ConfigTools configTools;

    @Inject
    TikaContentParser tikaContentParser;


    /**
     *
     */
    public ImageSortResult sortImages() {
        ImageSortResult imageSortResult = new ImageSortResult();
        logger.info("Woker starting...");
        List<SortSource> sortSourcesList = ConfigTools.getSources(configTools.getConfig());
        // -- Iterate the Sources
        for (int i = 0; i < sortSourcesList.size(); i++) {
            logger.info("Processing Source:" + sortSourcesList.get(i).getSourceDir().getAbsolutePath());
            List<File> sourceFileList = (List<File>) FileUtils.listFiles(sortSourcesList.get(i).getSourceDir(),
                    extensions, true);
            for (File sourceFile : sourceFileList) {
                boolean isDuplication = false;
                try {
                    logger.info("Processing File:" + sourceFile.getCanonicalPath());
                    String destFile = getDestFilename(sourceFile, sortSourcesList.get(i).getPreString());
                    if (destFile != null) {
                        while (destinationExists(destFile)) {
                            destFile = getDuplicateFilename(destFile);
                            isDuplication = true;
                        }
                        // now move the File
                        logger.debug("Move File from:" + sourceFile.getAbsolutePath() + " -> " + destFile);
                        FileUtils.moveFile(sourceFile, new File(destFile));
                        if (isDuplication) {
                            imageSortResult.increaseDuplicationImage();
                        } else {
                            imageSortResult.increaseRegularImage();
                        }
                    } else {
                        logger.info("File NOT moved");
                        imageSortResult.increaseErrors();
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    imageSortResult.increaseErrors();
                }
            }

        }
        return imageSortResult;
    }

    /**
     * @param destFileName
     * @return
     */
    private boolean destinationExists(String destFileName) {
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            logger.info("File " + FilenameUtils.getName(destFileName) + " already exists... create a duplicate");
            return true;
        }
        return false;
    }

    /**
     * @param destFileName
     * @return
     */
    private String getDuplicateFilename(String destFileName) {
        String result = "";
        int counter = 0;
        String simpleName = FilenameUtils.getBaseName(destFileName);
        if (simpleName.charAt(simpleName.length() - 3) == '_') {
            counter = Integer.valueOf(simpleName.substring(simpleName.length() - 2));
            counter++;
            simpleName = simpleName.substring(0, simpleName.length() - 2);
            if (counter < 10) {
                simpleName += "0";
            }
            simpleName += "" + counter;
        } else {
            simpleName += "_00";
        }
        result = FilenameUtils.getFullPath(destFileName) + simpleName + "."
                + FilenameUtils.getExtension(destFileName).trim().toLowerCase();
        logger.info("Try new Duplicate Filename : " + simpleName);
        return result;
    }

    /**
     * @param srcFile
     * @return
     */
    private String getDestFilename(File srcFile, String filePre) {
        GregorianCalendar metaDate = new GregorianCalendar();
        String result = configTools.getConfig().getProperty("destination");
        try {

            metaDate.setTime(tikaContentParser.getMetaInfCreateDate(srcFile));
            logger.debug("Exif Date from File =  " + simpleDateFormat.format(metaDate.getTime()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
        result += FolderTools.getLevel1String(metaDate);
        result += FolderTools.getLevel2String(metaDate);
        result += FolderTools.getDestFilename(filePre, metaDate, FilenameUtils.getExtension(srcFile.getName()));
        return result;
    }

}
