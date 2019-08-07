package com.fedex.services.agile.cards.enums;

public enum StopSequenceEnum {
	LOGIN("https://sso.v1host.com/sp/ACS.saml2"),
	COMBOLOAD("https://www19.v1host.com/FedEx/ApiConsole.mvc/rest-1.v1/Data/Story?sel=Scope,Timebox,Team"),
	CARDDATA_FEATURE("https://www19.v1host.com/FedEx/ApiConsole.mvc/rest-1.v1/Data/Story?sel=SuperAndUp.Number,SuperAndUp.Name,SuperAndUp.Description,SuperAndUp.Scope"),
	CARDDATA_US("https://www19.v1host.com/FedEx/ApiConsole.mvc/rest-1.v1/Data/Story?sel=Number,Name,Description,Estimate,Reference,Super,Team,Parent,Timebox,Priority,Owners,Scope"),
	HISTORY("https://www19.v1host.com/FedEx/api/ActivityStream/");

	private String url;

	StopSequenceEnum(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}
}
