package de.jkarthaus.model;

import java.io.File;

public class SortSource {

	File sourceDir;
	String preString;

	public String getPreString() {
		return preString;
	}

	public void setPreString(String preString) {
		this.preString = preString;
	}

	public File getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(File sourceDir) {
		this.sourceDir = sourceDir;
	}

}
