package de.dhbw.horb.routePlanner.ui;

import java.net.URLEncoder;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
		//webEngine.load("http://overpass-api.de/api/convert?data=%28%28way%28238669065%29%3Bway%2826577114%29%3B%29%3B%3E%3B%29%3Bout%3B&target=ol_fixed");
		webEngine.load(generateLinkQuery());
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

	private String  generateLinkQuery() {
		String linkStart = Constants.LINK_LINKSTART;
		String linkEnd = Constants.LINK_LINKEND;
		String completeLink = Constants.LINL_COMPLETELINK;
		
		//TODO Testweise:
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
		
		//TODO Andere Möglichkeit?!
		String result = linkStart + URLEncoder.encode(completeLink) + Constants.LINK_LINKTARGET;
		
		return result;
		
	}
}
