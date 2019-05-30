package com.fedex.services.agile.cards.enums;

public enum StopSequenceEnum {
	LOGIN("https://sso.v1host.com/sp/ACS.saml2"),
	COMBOLOAD("https://www19.v1host.com/FedEx/ApiConsole.mvc/rest-1.v1/Data/Story?sel=Scope,Timebox,Team"),
	CARDDATA("https://www19.v1host.com/FedEx/ApiConsole.mvc/rest-1.v1/Data/Story?sel=Number,Name,Description,Estimate,Reference,Super,Team,Parent,Timebox,Priority,Owners");

	private String url;

	StopSequenceEnum(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}
}
