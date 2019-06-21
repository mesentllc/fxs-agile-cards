package com.fedex.services.agile.cards.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class V1ObjectTest {
	private String json;

	@Before
	public void prepare() throws IOException {
		json = FileUtils.readFileToString(new File(getClass().getClassLoader().getResource("json/testV1Object.json").getFile()), "UTF-8");
	}

	@Test
	public void mapperTest() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		V1Object v1Object = mapper.readValue(json, V1Object.class);
		Assert.assertNotNull(v1Object);
		Assert.assertEquals(3L, v1Object.getTotalElements().longValue());
		Assert.assertEquals(5, v1Object.getAssets().size());
	}
}
