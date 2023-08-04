package de.jkarthaus.service;

import de.jkarthaus.exceptions.MetaDataParseFailException;
import de.jkarthaus.exceptions.ParameterNotPlausibleException;
import de.jkarthaus.model.SortResult;
import de.jkarthaus.tools.FolderTools;
import de.jkarthaus.tools.TikaContentParser;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
@Slf4j
public class MediaSortService {

    private final String[] extensions = new String[]{"jpg", "JPG", "mp4", "MP4"};

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");

    @Inject
    TikaContentParser tikaContentParser;


    /**
     *
     */
    public SortResult sortImages(
            Path source,
            Path destination,
            String destPreText,
            boolean verbose) throws ParameterNotPlausibleException {

        if (!source.toFile().exists()
                || !source.toFile().isDirectory()) {
            log.error("Source :{} not found, or is not a Directory",source);
            throw new ParameterNotPlausibleException();
        }
        if (!destination.toFile().exists()
                || !destination.toFile().isDirectory()) {
            log.error("Destination :{} not found, or is not a Directory",destination);
            throw new ParameterNotPlausibleException();
        }
        // -- Start the Process...
        SortResult sortResult = new SortResult();
        int percent = -1;
        int actCounter = 0;
        Double actualPercent = Double.valueOf(0);
        log.info("Woker starting...");
        // -- Iterate the Sources
        log.info("Processing Source:" + source.toFile().getAbsolutePath());
        List<File> sourceFileList = (List<File>) FileUtils.listFiles(
                source.toFile(),
                extensions,
                true
        );
        for (File sourceFile : sourceFileList) {
            actCounter++;
            actualPercent = Double.valueOf(((double) actCounter / sourceFileList.size()) * 100);
            if (percent != actualPercent.intValue()) {
                percent = actualPercent.intValue();
                log.info("Actual done:{} Percent of {} Files", percent, sourceFileList.size());
            }
            boolean isDuplication = false;
            try {
                if (verbose) {
                    log.info("Processing File:" + sourceFile.getCanonicalPath());
                }
                String destFile = getDestFilename(
                        sourceFile,
                        destPreText,
                        destination
                );
                if (destFile != null) {
                    while (destinationExists(destFile)) {
                        destFile = getDuplicateFilename(destFile);
                        isDuplication = true;
                    }
                    // now move the File
                    log.debug("Move File from:" + sourceFile.getAbsolutePath() + " -> " + destFile);
                    FileUtils.copyFile(sourceFile, new File(destFile));
                    if (isDuplication) {
                        sortResult.increaseDuplicationImage();
                    } else {
                        sortResult.increaseRegularImage();
                    }
                } else {
                    log.info("File NOT moved");
                    sortResult.increaseErrors();
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                sortResult.increaseErrors();
            } catch (MetaDataParseFailException e) {
                log.error("Error getting Meta Data from File:{}", sourceFile.getName());
                sortResult.increaseErrors();
            }
        }
        return sortResult;
    }

    /**
     * @param destFileName
     * @return
     */
    private boolean destinationExists(String destFileName) {
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            log.info("File " + FilenameUtils.getName(destFileName) + " already exists... create a duplicate");
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
        log.info("Try new Duplicate Filename : " + simpleName);
        return result;
    }

    /**
     * @param srcFile
     * @return
     */
    private String getDestFilename(File srcFile, String filePre, Path destination)
            throws TikaException, IOException, MetaDataParseFailException, SAXException {
        LocalDateTime timeStampFromMetaData;
        String result = destination.toString();
        timeStampFromMetaData = tikaContentParser.getMetaInfCreateDate(srcFile);
        log.debug("Exif Date from File = {} ", timeStampFromMetaData);
        result += FolderTools.getLevel1String(timeStampFromMetaData);
        result += FolderTools.getLevel2String(timeStampFromMetaData);
        result += FolderTools.getDestFilename(
                filePre,
                timeStampFromMetaData,
                FilenameUtils.getExtension(srcFile.getName())
        );
        return result;
    }

}
