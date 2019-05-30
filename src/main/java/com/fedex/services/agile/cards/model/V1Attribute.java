package com.fedex.services.agile.cards.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class V1Attribute {
	@JsonProperty("Number")
	private V1Value number;
	@JsonProperty("Name")
	private V1Value name;
	@JsonProperty("Description")
	private V1Value description;
	@JsonProperty("Estimate")
	private V1Value points;
	@JsonProperty("Reference")
	private V1Value reference;
	@JsonProperty("Super.Name")
	private V1Value superName;
	@JsonProperty("Super.Number")
	private V1Value superNumber;
	@JsonProperty("Team.Name")
	private V1Value team;
	@JsonProperty("Parent.Name")
	private V1Value parentName;
	@JsonProperty("Parent.Number")
	private V1Value parentNumber;
	@JsonProperty("Timebox.Name")
	private V1Value sprint;
	@JsonProperty("Owners.Name")
	private V1Values owners;
	@JsonProperty("Priority.Name")
	private V1Value priority;
	@JsonProperty("Scope.Name")
	private V1Value programIncrement;
}
