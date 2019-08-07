package com.fedex.services.agile.cards.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class V1FeatureObject {
	@JsonProperty("total")
	private Integer totalElements;
	@JsonProperty("Assets")
	private List<V1FeatureAsset> assets;
}
