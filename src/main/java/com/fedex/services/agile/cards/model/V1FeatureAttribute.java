package com.fedex.services.agile.cards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class V1FeatureAttribute {
	@JsonProperty("SuperAndUp.Number")
	private V1Values portfolioNumbers;
	@JsonProperty("SuperAndUp.Name")
	private V1Values portfolioNames;
	@JsonProperty("SuperAndUp.Description")
	private V1Values portfolioDescriptions;
	@JsonProperty("SuperAndUp.Scope.Name")
	private V1Values programIncrements;
}
