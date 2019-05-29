package com.fedex.services.agile.cards.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileTaskDao implements TaskDao {
	private final String filename;

	public FileTaskDao(String filename) {
		this.filename = filename;
	}

	@Override
	public String retrieveJson() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			StringBuilder buffer = new StringBuilder();
			while (reader.ready()) {
				buffer.append(reader.readLine());
			}
			reader.close();
			return buffer.toString();
		}
		catch (IOException ioe) {
			throw new RuntimeException("Exception Caught: ", ioe);
		}
	}
}
