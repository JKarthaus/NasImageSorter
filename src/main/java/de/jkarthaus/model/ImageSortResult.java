package de.jkarthaus.model;


import org.apache.commons.lang3.StringUtils;

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

    public String toHtmlString() {
        String result = "<b>Bilder Synchronisation durchgeführt</b><p>\n";
        result += "<b>" + getCountImages() + "<b> Bilder wurden verschoben.<p>\n";
        result += "<b>" + getDuplications() + "<b> Duplikate waren enthalten.<p>\n";
        result += "<b>" + getErrors() + "<b> Bilder erzeugten Fehler.<p>\n";
        return result;
    }

    @Override
    public String toString() {
        String text = toHtmlString();
        text = StringUtils.remove(text, "<b>");
        text = StringUtils.remove(text, "</b>");
        text = StringUtils.remove(text, "<p>");
        return text;
    }

}
