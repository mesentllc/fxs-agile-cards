package com.fedex.services.agile.cards.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

@CommonsLog
public class HistoryObjectTest {
	private String json;

	@Before
	public void prepare() throws IOException {
		json = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json/testHistoryObject.json").getFile()), "UTF-8");
	}

	@Test
	public void mapperTest() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		History[] history = mapper.readValue(json, History[].class);
		Assert.assertNotNull(history);
		Assert.assertEquals(13L, history.length);
		for (History item : history) {
			HistoryRecord record = item.getHistoryRecord();
			log.info(record.getHistoryObject().getAssetType() + " " + record.getVerb() + " -> " + record.getTime());
		}
	}
}
