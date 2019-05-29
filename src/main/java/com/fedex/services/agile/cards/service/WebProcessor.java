package com.fedex.services.agile.cards.service;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class WebProcessor {
	private final String waitForString;
	private final String url;
	private boolean done;
	private String lastMessage;

	public WebProcessor(String url, String waitForString) {
		this.url = url;
		this.waitForString = waitForString;
	}

	public boolean isDone() {
		return done;
	}

	@SuppressWarnings("unchecked")
	public void process(WebEngine webEngine) {
		Worker worker = webEngine.getLoadWorker();
		worker.stateProperty().addListener((observable, oldValue, newValue) -> {
			lastMessage = worker.getMessage();
			log.info("oldValue: " + oldValue);
			log.info("newValue: " + newValue);
			log.info("Worker message: " + lastMessage);
			if (newValue == Worker.State.FAILED) {
				log.info("Exception: ", worker.getException());
			}
			if (newValue == Worker.State.SUCCEEDED && lastMessage.contains(waitForString)) {
				done = true;
			}
		});
		webEngine.load(url);
	}
}
