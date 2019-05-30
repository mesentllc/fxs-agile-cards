package com.fedex.services.agile.cards.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileTaskDao {
	public String retrieveJson(String resource) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(resource));
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
