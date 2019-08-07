package com.fedex.services.agile.cards.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fedex.services.agile.cards.WebMakeCards;
import com.fedex.services.agile.cards.enums.FilterEnum;
import com.fedex.services.agile.cards.enums.StopSequenceEnum;
import com.fedex.services.agile.cards.model.History;
import com.fedex.services.agile.cards.model.HistoryRecord;
import com.fedex.services.agile.cards.model.TaskModel;
import com.fedex.services.agile.cards.model.V1Asset;
import com.fedex.services.agile.cards.model.V1Object;
import com.fedex.services.agile.cards.model.V1Value;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.web.WebEngine;
import lombok.extern.apachecommons.CommonsLog;
import net.sf.dynamicreports.report.exception.DRException;
import org.w3c.dom.Document;
import org.w3c.dom.html.HTMLPreElement;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@CommonsLog
public class V1ApiService extends WebProcess {
	private static final ObjectMapper mapper = new ObjectMapper();
	private static final String url = "https://www19.v1host.com/FedEx/sso.html?TargetResource=" +
	                                  "https://www19.v1host.com/FedEx/ApiConsole.mvc/";
	private static final String instructHtml = "<br/><br /><p style=\"font-size:large;\">So not to print all the Epics, Features and User Stories currently " +
	                                           "found in the FedEx VersionOne Database, you are required to select <u>at least one</u> of the dropdown items on the " +
	                                           "right (Program Increment, Sprint and/or Team) before you will be able to press the \"Extract\" button on " +
	                                           "the right.</p><p style=\"font-size:large;\">It will pop-up the print dialog box once completed, so you " +
	                                           "are able to print out the extracted cards. If you wish to save the cards to a PDF file, select " +
	                                           "\"Microsoft Print to PDF\" from the Printer dialog box.</p><p style=\"font-size:large;\">The \"Created After\"" +
	                                           " is optional, and used to further reduce the number of cards that will be printed.</p>";
	private final List<TaskModel> stories = new ArrayList<>();
	private final Button btnExtract;
	private final DatePicker dpAfter;
	private final RadioButton rbOnePage;
	private final boolean features;
	private WebMakeCards app;
	private WebEngine engine;
	private StopSequenceEnum stopSequence;
	private Map<String, TaskModel> storyMap;
	private String extraUrl = null;

	public V1ApiService(Button btnExtract, DatePicker dpAfter,  boolean features, RadioButton rbOnePage) {
		this.btnExtract = btnExtract;
		this.dpAfter = dpAfter;
		this.features = features;
		this.rbOnePage = rbOnePage;
	}

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
		String searchFor = stopSequence.getUrl();
		if (extraUrl != null) {
			searchFor += extraUrl;
		}
		return lastUrl.contains(searchFor);
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
				if (features) {
					stopSequence = StopSequenceEnum.CARDDATA_FEATURE;
				}
				else {
					stopSequence = StopSequenceEnum.CARDDATA_US;
				}
				json = retrieveJsonFromHTML();
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
					return;
				}
				engine.loadContent(instructHtml);
				break;
			case CARDDATA_US:
				try {
					storyMap = APICardService.dumpToMap(retrieveJsonFromHTML());
				}
				catch (IOException ioe) {
					log.error("Exception Caught: ", ioe);
					return;
				}
				if (storyMap.size() > 0) {
					printCardsAndExitIfNoDate();
					stopSequence = StopSequenceEnum.HISTORY;
					storyMap.keySet().stream().findFirst().ifPresent(key -> {
						extraUrl = key;
						engine.load(stopSequence.getUrl() + key);
					});
				}
				else {
					notifyNoStories();
				}
				break;
			case CARDDATA_FEATURE:
				try {
					storyMap = APICardService.dumpFeatureToMap(retrieveJsonFromHTML());
				}
				catch (IOException ioe) {
					log.error("Exception Caught: ", ioe);
					return;
				}
				if (storyMap.size() > 0) {
					printCardsAndExitIfNoDate();
				}
				else {
					notifyNoStories();
				}
				break;
			case HISTORY:
				json = retrieveJsonFromHTML();
				try {
					History[] history = mapper.readValue(json, History[].class);
					if (createdAfterDate(history)) {
						stories.add(storyMap.get(extraUrl));
					}
					storyMap.remove(extraUrl);
					if (storyMap.size() > 0) {
						storyMap.keySet().stream().findFirst().ifPresent(key -> {
							extraUrl = key;
							engine.load(stopSequence.getUrl() + key);
						});
					}
					else {
						extraUrl = null;
						if (stories.size() > 0) {
							APICardService.process(stories, null, features, rbOnePage);
							System.exit(0);
						}
						else {
							notifyNoStories();
						}
					}
				}
				catch (IOException | DRException ioe) {
					log.error("Exception Caught: ", ioe);
				}
		}
	}

	private void notifyNoStories() {
		stopSequence = StopSequenceEnum.CARDDATA_US;
		Alert alert = new Alert(Alert.AlertType.INFORMATION, "No stories found for current selection.", ButtonType.OK);
		alert.setTitle("No stories.");
		alert.showAndWait();
		btnExtract.setDisable(false);
	}

	private void printCardsAndExitIfNoDate() {
		if (dpAfter.getValue() == null) {
			for (String key : storyMap.keySet()) {
				stories.add(storyMap.get(key));
			}
			try {
				APICardService.process(stories, null, features, rbOnePage);
			}
			catch (DRException | IOException e) {
				log.error("Exception Caught: ", e);
			}
			System.exit(0);
		}
	}

	private boolean createdAfterDate(History[] historyArray) {
		LocalDate earliestDate = LocalDate.now();
		for (History history : historyArray) {
			HistoryRecord historyRecord = history.getHistoryRecord();
			LocalDate recordDate = LocalDate.parse(historyRecord.getTime(), DateTimeFormatter.ISO_DATE_TIME);
			if (earliestDate.isAfter(recordDate)) {
				earliestDate = recordDate;
			}
			if ("Story".equals(historyRecord.getHistoryObject().getAssetType())) {
				if ("Created".equals(historyRecord.getVerb())) {
					return dpAfter.getValue().isBefore(recordDate);
				}
			}
		}
		return dpAfter.getValue().isBefore(earliestDate);
	}

	private String retrieveJsonFromHTML() {
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
