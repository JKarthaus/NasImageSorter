package de.jkarthaus.worker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.jkarthaus.model.ImageSortResult;
import de.jkarthaus.model.SortSource;
import de.jkarthaus.tools.ConfigTools;
import de.jkarthaus.tools.FolderTools;
import de.jkarthaus.tools.TikaJPGParser;

public class ImageSortWorker {

	Properties config = new Properties();

	private String[] extensions = new String[] { "jpg", "JPG" };
	private TikaJPGParser tikaJPGParser;
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
	private static final Logger logger = LogManager.getLogger(ImageSortWorker.class);

	/**
	 * 
	 * @param config
	 */
	public ImageSortWorker(Properties config) {
		this.config = config;
		tikaJPGParser = new TikaJPGParser();
	}

	/**
	 * 
	 */
	public ImageSortResult sortImages() {
		ImageSortResult imageSortResult = new ImageSortResult();
		logger.info("Woker starting...");
		List<SortSource> sortSourcesList = ConfigTools.getSources(config);
		// -- Iterate the Sources
		for (int i = 0; i < sortSourcesList.size(); i++) {
			logger.info("Processing Source:" + sortSourcesList.get(i).getSourceDir().getAbsolutePath());
			List<File> sourceFileList = (List<File>) FileUtils.listFiles(sortSourcesList.get(i).getSourceDir(),
					extensions, true);
			for (File sourceFile : sourceFileList) {
				try {
					logger.info("Processing File:" + sourceFile.getCanonicalPath());
					String destFile = getDestFilename(sourceFile, sortSourcesList.get(i).getPreString());
					while (destinationExists(destFile)) {
						destFile = getDuplicateFilename(destFile);
						imageSortResult.increaseDuplicationImage();
					}
					// now move the File
					logger.debug("Move File from:" + sourceFile.getAbsolutePath() + " -> " + destFile);
					FileUtils.copyFile(sourceFile, new File(destFile), true);
					imageSortResult.increaseRegularImage();
				} catch (Exception e) {
					logger.error(e);
					imageSortResult.increaseErrors();
				}
			}

		}
		return imageSortResult;
	}

	/**
	 * 
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
	 * 
	 * @param destFileName
	 * @return
	 */
	private String getDuplicateFilename(String destFileName) {
		String result = "";
		int counter = 0;
		String simpleName = FilenameUtils.getBaseName(destFileName);
		if (simpleName.charAt(simpleName.length() - 3) == '_') {
			counter = new Integer(simpleName.substring(simpleName.length() - 2));
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
	 * 
	 * @param srcFile
	 * @return
	 */
	private String getDestFilename(File srcFile, String filePre) {
		GregorianCalendar exifDate = new GregorianCalendar();
		String result = config.getProperty("destination");
		try {
			exifDate.setTime(tikaJPGParser.getExifCreateDate(srcFile));
			logger.debug("Exif Date from Image =  " + simpleDateFormat.format(exifDate.getTime()));
		} catch (Exception e) {
			logger.error(e);
		}
		result += FolderTools.getLevel1String(exifDate);
		result += FolderTools.getLevel2String(exifDate);
		result += FolderTools.getDestFilename(filePre, exifDate, FilenameUtils.getExtension(srcFile.getName()));
		return result;
	}

}
