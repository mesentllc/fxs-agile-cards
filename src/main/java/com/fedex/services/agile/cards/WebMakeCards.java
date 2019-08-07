package com.fedex.services.agile.cards;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import com.fedex.services.agile.cards.enums.StopSequenceEnum;
import com.fedex.services.agile.cards.service.V1ApiService;
import com.fedex.services.agile.cards.service.WebProcess;
import com.fedex.services.agile.cards.utility.WebUtils;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
@Data
@EqualsAndHashCode(callSuper = false)
public class WebMakeCards extends Application {
	private static final DatePicker dpAfter = new DatePicker();
	private WebView webView;
	private ComboBox<String> cbxPI;
	private ComboBox<String> cbxSprint;
	private ComboBox<String> cbxTeam;
	private Button btnType;
	private Button btnSubmit;
	private RadioButton rbFeatures;
	private RadioButton rbUserStories;
	private RadioButton rbOnePerPage;
	private RadioButton rbFourPerPage;
	private ToggleGroup tgType;
	private ToggleGroup tgPerPage;
	private WebProcess v1ApiService;

	private Pane setupUI() {
		Font bold = Font.font("System Bold", FontWeight.BOLD, 12.0);
		webView = new WebView();
		HBox container = new HBox();
		VBox webContainer = new VBox(webView);
		VBox leftSpacer = new VBox();
		leftSpacer.setPrefSize(20, 500);
		VBox rightSpacer = new VBox();
		rightSpacer.setPrefSize(20, 500);
		webContainer.setPrefSize(800, 500);
		HBox typeContainer = new HBox();
		VBox controlContainer = new VBox();
		controlContainer.setPrefSize(300, 500);
		cbxPI = new ComboBox<>();
		cbxPI.setOnAction(event -> btnSubmit.setDisable(!filterSelected()));
		cbxSprint = new ComboBox<>();
		cbxSprint.setOnAction(event -> btnSubmit.setDisable(!filterSelected()));
		cbxTeam = new ComboBox<>();
		cbxTeam.setOnAction(event -> btnSubmit.setDisable(!filterSelected()));
		tgType = new ToggleGroup();
		rbUserStories = new RadioButton("User Stories");
		rbUserStories.setPadding(new Insets(0, 20, 0, 0));
		rbUserStories.setToggleGroup(tgType);
		rbUserStories.setSelected(true);
		rbFeatures = new RadioButton("Features");
		rbFeatures.setPadding(new Insets(0, 20, 0, 0));
		rbFeatures.setToggleGroup(tgType);
		btnType = new Button();
		btnType.setText("Set");
		btnType.setDisable(false);
		btnType.setOnAction(event -> {
			rbUserStories.setDisable(true);
			rbFeatures.setDisable(true);
			btnType.setDisable(true);
			v1ApiService = new V1ApiService(btnSubmit, dpAfter, rbFeatures.isSelected(), rbOnePerPage);
			v1ApiService.process(this);
		});
		typeContainer.getChildren().addAll(rbUserStories, rbFeatures, btnType);
		Label lblPerPage = new Label("Number of cards per page:");
		lblPerPage.setFont(bold);
		tgPerPage = new ToggleGroup();
		rbOnePerPage = new RadioButton("1 Card");
		rbOnePerPage.setPadding(new Insets(0, 20, 0, 0));
		rbOnePerPage.setToggleGroup(tgPerPage);
		rbFourPerPage = new RadioButton("4 Cards");
		rbFourPerPage.setSelected(true);
		rbFourPerPage.setToggleGroup(tgPerPage);
		HBox pageContainer = new HBox();
		pageContainer.getChildren().addAll(rbOnePerPage, rbFourPerPage);
		Label lblPI = new Label();
		lblPI.setText("Program Increment:");
		lblPI.setFont(bold);
		lblPI.setPadding(new Insets(10, 10, 10, 0));
		Label lblSprint = new Label();
		lblSprint.setText("Sprint:");
		lblSprint.setFont(bold);
		lblSprint.setPadding(new Insets(10, 10, 10, 0));
		Label lblTeam = new Label();
		lblTeam.setText("Team:");
		lblTeam.setFont(bold);
		lblTeam.setPadding(new Insets(10, 10, 10, 0));
		Label lblType = new Label();
		lblType.setText("Card Type:  (Please select and SET before we start.)");
		lblType.setFont(bold);
		lblType.setPadding(new Insets(10, 10, 10, 0));
		Label lblAfter = new Label();
		lblAfter.setText("Created After:");
		lblAfter.setFont(bold);
		lblAfter.setPadding(new Insets(10, 10, 10, 0));
		Label lblSpacer = new Label();
		lblSpacer.setPadding(new Insets(20));
		HBox btnBox = new HBox();
		btnSubmit = new Button();
		btnSubmit.setText("Extract");
		btnSubmit.setDisable(true);
		btnSubmit.setOnAction(event -> {
			if (filterSelected()) {
				btnSubmit.setDisable(true);
				if (rbFeatures.isSelected()) {
					webView.getEngine().load(StopSequenceEnum.CARDDATA_FEATURE.getUrl() + buildSelectString() + "&Accept=application/json");
				}
				else {
					webView.getEngine().load(StopSequenceEnum.CARDDATA_US.getUrl() + buildSelectString() + "&Accept=application/json");
				}
			}
			else {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Must select AT LEAST one of the filters.\n PI, Sprint or Team.", ButtonType.OK);
				alert.setTitle("No filter selected.");
				alert.showAndWait();
			}
		});
		Button btnExit = new Button();
		btnExit.setText("Exit");
		btnExit.setOnAction(event -> System.exit(0));
		Label lblBtnSpacer = new Label();
		lblBtnSpacer.setPadding(new Insets(20));
		btnBox.getChildren().addAll(btnSubmit, lblBtnSpacer, btnExit);
		controlContainer.getChildren().addAll(lblType, typeContainer, lblPerPage, pageContainer, lblPI, cbxPI,
			lblSprint, cbxSprint, lblTeam, cbxTeam, lblAfter, dpAfter, lblSpacer, btnBox);
		container.getChildren().addAll(webContainer, leftSpacer, controlContainer, rightSpacer);
		return container;
	}

	private boolean filterSelected() {
		return !(cbxPI.getSelectionModel().isSelected(0) && cbxSprint.getSelectionModel().isSelected(0) &&
		         cbxTeam.getSelectionModel().isSelected(0));
	}

	private String buildSelectString() {
		StringBuilder sb = new StringBuilder();
		if (hasUserSelection(cbxPI)) {
			sb.append("&where=Scope.Name='").append(cbxPI.getSelectionModel().getSelectedItem()).append("'");
		}
		if (hasUserSelection(cbxSprint)) {
			if (sb.length() > 0) {
				sb.append(";");
			}
			else {
				sb.append("&where=");
			}
			sb.append("Timebox.Name='").append(cbxSprint.getSelectionModel().getSelectedItem()).append("'");
		}
		if (hasUserSelection(cbxTeam)) {
			if (sb.length() > 0) {
				sb.append(";");
			}
			else {
				sb.append("&where=");
			}
			sb.append("Team.Name='").append(cbxTeam.getSelectionModel().getSelectedItem()).append("'");
		}
		return sb.toString();
	}

	private boolean hasUserSelection(ComboBox<String> cbx) {
		return (!cbx.getSelectionModel().getSelectedItem().startsWith(" -- All "));
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
		stage.setResizable(false);
	}
}