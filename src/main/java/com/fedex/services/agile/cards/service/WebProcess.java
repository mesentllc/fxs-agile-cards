package com.fedex.services.agile.cards.service;

import com.fedex.services.agile.cards.WebMakeCards;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public abstract class WebProcess {
	String lastUrl;
	protected abstract boolean isDone();
	protected abstract void postProcess();
	public abstract void process(WebMakeCards app);

	@SuppressWarnings("unchecked")
	void setChangeListener(WebEngine webEngine) {
		Worker worker = webEngine.getLoadWorker();
		worker.stateProperty().addListener((observable, oldValue, newValue) -> {
			log.debug("oldValue: " + oldValue);
			log.debug("newValue: " + newValue);
			log.debug("Worker message: " + worker.getMessage());
			if (newValue == Worker.State.FAILED) {
				log.error("Exception: ", worker.getException());
			}
			if (newValue == Worker.State.SUCCEEDED && isDone()) {
				postProcess();
			}
			else {
				lastUrl = worker.getMessage();
			}
		});
	}
}
