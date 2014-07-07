package de.dhbw.horb.routePlanner.ui;

import java.io.UnsupportedEncodingException;
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
import de.dhbw.horb.routePlanner.Constants;

public class RoutePlannerMainController {

	// Reference to the main application.
	private RoutePlannerMainApp routePlannerMainApp;
	private WebEngine webEngine;

	public String linkStart = Constants.LINK_LINKSTART;
	public String linkEnd = Constants.LINK_LINKEND;

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
		webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());
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
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue ov, State oldState, State newState) {

				if (newState == Worker.State.SUCCEEDED) {
					String ways = null;
					try {
						ways = generateLinkQuery_ways();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String nodes = null;
					try {
						nodes = generateLinkQuery_nodes();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("Ways: " + ways);
					System.out.println("Nodes: " + nodes);
					webEngine.executeScript("init(\"" + ways + "\",\"" + nodes + "\")");
				}

			}
		});
	}

	private String generateLinkQuery_ways() throws UnsupportedEncodingException {

		String completeLink = Constants.LINK_COMPLETELINK;
		String result_ways;

		ArrayList<String> ways = new ArrayList<>();
		ways.add("4811958");
		ways.add("130792761");
		ways.add("4811957");
		ways.add("4811807");
		ways.add("4811805");
		ways.add("4811806");
		ways.add("4811804");

		for (String string : ways) {
			completeLink += "way(" + string + ");";
		}

		result_ways = linkStart + URLEncoder.encode(completeLink, "UTF-8") + linkEnd;

		return result_ways;

	}

	private String generateLinkQuery_nodes() throws UnsupportedEncodingException {

		String completeLink = Constants.LINK_COMPLETELINK;
		String result_nodes;

		completeLink = Constants.LINK_COMPLETELINK;

		ArrayList<String> nodes = new ArrayList<>();
		nodes.add("30898199");
		nodes.add("30899103");

		for (String string : nodes) {
			completeLink += "node(" + string + ");";
		}

		result_nodes = linkStart + URLEncoder.encode(completeLink, "UTF-8") + linkEnd;

		return result_nodes;

	}
}
