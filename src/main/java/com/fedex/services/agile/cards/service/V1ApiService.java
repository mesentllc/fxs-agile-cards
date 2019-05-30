package com.fedex.services.agile.cards.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.services.agile.cards.WebMakeCards;
import com.fedex.services.agile.cards.enums.StopSequenceEnum;
import com.fedex.services.agile.cards.model.V1Asset;
import com.fedex.services.agile.cards.model.V1Object;
import com.fedex.services.agile.cards.model.V1Value;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.web.WebEngine;
import lombok.extern.apachecommons.CommonsLog;
import net.sf.dynamicreports.report.exception.DRException;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLPreElement;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

@CommonsLog
public class V1ApiService extends WebProcess {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String url = "https://www19.v1host.com/FedEx/sso.html?TargetResource=" +
	                                  "https://www19.v1host.com/FedEx/ApiConsole.mvc/";
	private WebMakeCards app;
	private WebEngine engine;
	private StopSequenceEnum stopSequence;

	private ObservableList<String> piItems(V1Object v1Object) {
		Set<String> items = new TreeSet<>();
		items.add(" -- All PIs --");
		for (V1Asset asset : v1Object.getAssets()) {
			V1Value v1Value = asset.getAttribute().getProgramIncrement();
			if (v1Value != null && v1Value.getValue() != null) {
				items.add(v1Value.getValue());
			}
		}
		return FXCollections.observableArrayList(items);
	}

	private ObservableList<String> sprintItems(V1Object v1Object) {
		Set<String> items = new TreeSet<>();
		items.add(" -- All Sprints --");
		for (V1Asset asset : v1Object.getAssets()) {
			V1Value v1Value = asset.getAttribute().getSprint();
			if (v1Value != null && v1Value.getValue() != null) {
				items.add(v1Value.getValue());
			}
		}
		return FXCollections.observableArrayList(items);
	}

	private ObservableList<String> teamItems(V1Object v1Object) {
		Set<String> items = new TreeSet<>();
		items.add(" -- All Teams --");
		for (V1Asset asset : v1Object.getAssets()) {
			V1Value v1Value = asset.getAttribute().getTeam();
			if (v1Value != null && v1Value.getValue() != null) {
				items.add(v1Value.getValue());
			}
		}
		return FXCollections.observableArrayList(items);
	}

	@Override
	protected boolean isDone() {
		return lastUrl.contains(stopSequence.getUrl());
	}

	@Override
	protected void postProcess() {
		String json;

		switch (stopSequence) {
			case LOGIN:
				stopSequence = StopSequenceEnum.COMBOLOAD;
				engine.load(stopSequence.getUrl() + "&Accept=application/json");
				break;
			case COMBOLOAD:
				stopSequence = StopSequenceEnum.CARDDATA;
				json = retrieveJson();
				engine.load("about:blank");
				try {
					V1Object v1Object = mapper.readValue(json, V1Object.class);
					app.getCbxPI().setItems(piItems(v1Object));
					app.getCbxPI().getSelectionModel().selectFirst();
					app.getCbxSprint().setItems(sprintItems(v1Object));
					app.getCbxSprint().getSelectionModel().selectFirst();
					app.getCbxTeam().setItems(teamItems(v1Object));
					app.getCbxTeam().getSelectionModel().selectFirst();
				}
				catch (IOException ioe) {
					log.error("Exception Caught: ", ioe);
				}
				app.getBtnSubmit().setDisable(false);
				break;
			case CARDDATA:
				APICardService service = new APICardService();
				json = retrieveJson();
				engine.load("about:blank");
				app.getBtnSubmit().setDisable(false);
				try {
					service.process(json, null);
					System.exit(0);
				}
				catch (DRException | IOException e) {
					throw new RuntimeException("Exception Caught: ", e);
				}
		}
	}

	private String retrieveJson() {
		Document document = engine.getDocument();
		HTMLPreElement element = (HTMLPreElement)document.getElementsByTagName("pre").item(0);
		return element.getTextContent();
	}

	@Override
	public void process(WebMakeCards app) {
		this.app = app;
		engine = app.getWebView().getEngine();
		stopSequence = StopSequenceEnum.LOGIN;
		setChangeListener(engine);
		engine.load(url);
	}
}
