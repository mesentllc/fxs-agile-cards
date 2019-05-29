package com.fedex.services.agile.cards;

import com.fedex.services.agile.cards.service.WebProcessor;
import com.fedex.services.agile.cards.utility.WebUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.extern.apachecommons.CommonsLog;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@CommonsLog
public class WebMakeCards extends Application {
	private WebView webView;
	private ComboBox cbxPI;
	private ComboBox cbxSprint;
	private ComboBox cbxTeam;

	private Pane setupUI() {
		Font bold = Font.font("System Bold", FontWeight.BOLD, 12.0);
		webView = new WebView();
		HBox container = new HBox();
		VBox webContainer = new VBox(webView);
		VBox spacer = new VBox();
		spacer.setPrefSize(20, 500);
		webContainer.setPrefSize(800, 500);
		VBox controlContainer = new VBox();
		controlContainer.setPrefSize(200, 500);
		Label lblPI = new Label();
		lblPI.setText("Program Increment:");
		lblPI.setFont(bold);
		cbxPI = new ComboBox();
		cbxPI.setDisable(true);
		Label lblSprint = new Label();
		lblSprint.setText("Sprint:");
		lblSprint.setFont(bold);
		cbxSprint = new ComboBox();
		cbxSprint.setDisable(true);
		Label lblTeam = new Label();
		lblTeam.setText("Team:");
		lblTeam.setFont(bold);
		cbxTeam = new ComboBox();
		cbxTeam.setDisable(true);
		controlContainer.getChildren().addAll(lblPI, cbxPI, lblSprint, cbxSprint, lblTeam, cbxTeam);
		container.getChildren().addAll(webContainer, spacer, controlContainer);
		return container;
	}

	public static void main(String[] args) {
		try {
			WebUtils.setupProxies();
			WebUtils.setupSSL();
		}
		catch (NoSuchAlgorithmException | KeyManagementException e) {
			log.error("Exception Caught: ", e);
			System.exit(-1);
		}
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("VersionOne Card Extractor");
		stage.setScene(new Scene(setupUI()));
		stage.show();
		WebEngine webEngine = webView.getEngine();
		WebProcessor webProcessor =
			new WebProcessor("https://www19.v1host.com/FedEx/sso.html?TargetResource=https://www19.v1host.com/FedEx/ApiConsole.mvc/",
			"https://sso.v1host.com/sp/ACS.saml2");
		webProcessor.process(webEngine);

	}
}