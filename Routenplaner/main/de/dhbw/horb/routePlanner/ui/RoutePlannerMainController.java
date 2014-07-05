package de.dhbw.horb.routePlanner.ui;

import java.net.URLEncoder;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.w3c.dom.Attr;

import de.dhbw.horb.routePlanner.Constants;

public class RoutePlannerMainController {

	// Reference to the main application.
	private RoutePlannerMainApp routePlannerMainApp;
	private WebEngine webEngine;

	@FXML
	private WebView testWebView;

	@FXML
	private Button closeButton;

	@FXML
	private Button infoButton;

	@FXML
	private Button testButton;

	@FXML
	private ComboBox<String> startComboBox;

	@FXML
	private ComboBox<String> targetComboBox;

	@FXML
	private Button calculateRouteButton;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public RoutePlannerMainController() {
	}

	@FXML
	void testButtonClicked(ActionEvent event) {
		// webEngine.load("http://overpass-api.de/api/convert?data=%28%28way%28238669065%29%3Bway%2826577114%29%3B%29%3B%3E%3B%29%3Bout%3B&target=ol_fixed");
		webEngine.getLoadWorker().stateProperty().addListener(
	            new ChangeListener<State>() {
	              @Override public void changed(ObservableValue ov, State oldState, State newState) {

	                  if (newState == Worker.State.SUCCEEDED) {
	              		Attr styleAttr = webEngine.getDocument().getElementById("statusline").getAttributeNode("style");
	            		styleAttr.setValue("display: none;");
	                  }
	                  
	                }
	            });
		webEngine.load("http://overpass-api.de/api/convert?data=%28%28way%2827809852%29%3Bway%2827809853%29%3Bway%2842720124%29%3Bway%284004562%29%3Bway%2829201096%29%3Bway%2842720117%29%3Bway%2841189157%29%3Bway%285052588%29%3B%29%3B%3E%3B%29%3Bout%20body%3B%0A&target=ol_fixed");
		//webEngine.load(generateLinkQuery());

	}

	
	
	@FXML
	void startComboBoxClicked(ActionEvent event) {

	}

	@FXML
	void calculateRouteButtonClicked(ActionEvent event) {

	}

	@FXML
	void infoButtonClicked(ActionEvent event) {

	}

	@FXML
	void closeButtonClicked(ActionEvent event) {
		routePlannerMainApp.primaryStage.close();
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setRoutePlannerMainApp(RoutePlannerMainApp routePlannerMainApp) {
		this.routePlannerMainApp = routePlannerMainApp;
		new AutoCompleteComboBoxListener<>(startComboBox);
		new AutoCompleteComboBoxListener<>(targetComboBox);
		webEngine = testWebView.getEngine();
	}

	private String generateLinkQuery() {
		String linkStart = Constants.LINK_LINKSTART;
		String linkEnd = Constants.LINK_LINKEND;
		String completeLink = Constants.LINL_COMPLETELINK;

		// DELETE Testweise:
		ArrayList<String> ways = new ArrayList<>();
		ways.add("238669065");
		ways.add("26577114");
		ArrayList<String> nodes = new ArrayList<>();
		nodes.add("291435955");
		nodes.add("96140183");

		for (String string : ways) {
			completeLink += "way(" + string + ");";
		}

		for (String string : nodes) {
			completeLink += "node(" + string + ");";
		}

		completeLink += linkEnd;

		// FIXME Andere Möglichkeit?!
		String result = linkStart + URLEncoder.encode(completeLink) + Constants.LINK_LINKTARGET;

		return result;

	}
}
