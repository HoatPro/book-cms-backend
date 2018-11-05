package vbee.bookcmsbackend.models;

import java.io.FileInputStream;

public class MSWord {
	String fileName;
	FileInputStream file;

	public MSWord(String fileName, FileInputStream file) {
		this.fileName = fileName;
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public FileInputStream getFile() {
		return file;
	}

	public void setFile(FileInputStream file) {
		this.file = file;
	}

}
