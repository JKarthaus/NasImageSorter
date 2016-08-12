package de.jkarthaus.model;

public class ImageSortResult {

	private int duplications;
	private int countImages;
	private int errors;

	public ImageSortResult() {
		duplications = 0;
		countImages = 0;
		errors = 0;
	}

	public void increaseRegularImage() {
		countImages++;
	}

	public void increaseDuplicationImage() {
		countImages++;
		duplications++;
	}

	public void increaseErrors() {
		countImages++;
		errors++;
	}

	public int getDuplications() {
		return duplications;
	}

	public int getCountImages() {
		return countImages;
	}

	public int getErrors() {
		return errors;
	}

	public String getResultText() {
		String result = "<b>Bilder Synchronisation durchgeführt</b><p>";
		result += "<b>" + getCountImages() + "<b> Bilder wurden verschoben.<p>";
		result += "<b>" + getDuplications() + "<b> Duplikate waren enthalten.<p>";
		result += "<b>" + getErrors() + "<b> Bilder erzeugten Fehler.<p>";
		return result;
	}

	@Override
	public String toString() {
		return getResultText();
	}

}
