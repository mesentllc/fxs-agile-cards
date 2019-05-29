package com.fedex.services.agile.cards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class V1Object {
	@JsonProperty("total")
	private Integer totalElements;
	@JsonProperty("Assets")
	private List<V1Asset> assets;
}
