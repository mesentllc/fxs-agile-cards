package com.fedex.services.agile.cards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HistoryRecord {
	@JsonProperty("verb")
	private String verb;
	@JsonProperty("time")
	private String time;
	@JsonProperty("object")
	private HistoryObject historyObject;

}
