package de.jkarthaus.tools;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class FolderTools {

	static SimpleDateFormat destFileFormatter = new SimpleDateFormat("dd-MM-yyyy_H-mm-ss", Locale.GERMANY);
	static SimpleDateFormat level2Formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

	/**
	 * 
	 * @param imageDate
	 * @return
	 */
	public static String getDestFilename(String filePre, GregorianCalendar imageDate, String fileExt) {
		String result = "";
		// Todo : Add TimeInfo
		result += "/" + destFileFormatter.format(imageDate.getTime());
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
	 * 
	 * @param imageDate
	 * @return
	 */
	public static String getLevel1String(GregorianCalendar imageDate) {
		String result = "/" + imageDate.get(GregorianCalendar.YEAR);
		return result;
	}

	/**
	 * 
	 * @param imageDate
	 * @return
	 */
	public static String getLevel2String(GregorianCalendar imageDate) {
		String result = "";
		// Todo : Add DateInfo
		result += "/" + level2Formatter.format(imageDate.getTime());
		// Add Manual Pre text
		result += "_[]";
		// Pretty up
		result = StringUtils.replace(result, ":", "-");
		result = StringUtils.replace(result, " ", "_");
		// --
		return result;

	}

	/**
	 * @deprecated
	 * @param imageDate
	 * @return
	 */
	@Deprecated
	public static String xxgetLevel2String(GregorianCalendar imageDate) {
		String result = "";
		switch (imageDate.get(GregorianCalendar.MONTH)) {
		case 0:
			result += "/01_Januar______________";
			break;
		case 1:
			result += "/02__Februar____________";
			break;
		case 2:
			result += "/03___März______________";
			break;
		case 3:
			result += "/04____April____________";
			break;
		case 4:
			result += "/05_____Mai_____________";
			break;
		case 5:
			result += "/06______Juni___________";
			break;
		case 6:
			result += "/07_______Juli__________";
			break;
		case 7:
			result += "/08________August_______";
			break;
		case 8:
			result += "/09_________September___";
			break;
		case 9:
			result += "/10__________Oktober____";
			break;
		case 10:
			result += "/11___________November__";
			break;
		case 11:
			result += "/12____________Dezember_";
			break;
		}
		return result;
	}

	/**
	 * 
	 * @param imageDate
	 * @return
	 */
	public static String getLevel3String(GregorianCalendar imageDate) {
		// 2016-08-06-Sonntag_
		String result = "" + imageDate.get(GregorianCalendar.YEAR);
		result += "-" + (imageDate.get(GregorianCalendar.MONTH) + 1);
		result += "-" + imageDate.get(GregorianCalendar.DAY_OF_MONTH);
		switch (imageDate.get(GregorianCalendar.DAY_OF_WEEK)) {
		case GregorianCalendar.MONDAY:
			result += "-Montag_____";
			break;
		case GregorianCalendar.TUESDAY:
			result += "-Dienstag___";
			break;
		case GregorianCalendar.WEDNESDAY:
			result += "-Mittwoch___";
			break;
		case GregorianCalendar.THURSDAY:
			result += "-Donnerstag_";
			break;
		case GregorianCalendar.FRIDAY:
			result += "-Freitag____";
			break;
		case GregorianCalendar.SATURDAY:
			result += "-Samstag____";
			break;
		case GregorianCalendar.SUNDAY:
			result += "-Sonntag____";
			break;
		}
		return result;
	}

}
