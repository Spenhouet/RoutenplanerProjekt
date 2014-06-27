package de.dhbw.horb.routePlanner.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class RoutePlannerMainController {

	// Reference to the main application.
    private RoutePlannerMainApp routePlannerMainApp;
	
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
    private ComboBox<?> tartgetComboBox;

    @FXML
    private Button calculateRouteButton;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public RoutePlannerMainController() {
    }
    
    @FXML
    void testButtonClicked(ActionEvent event) {
    	WebEngine webEngine = testWebView.getEngine();
    	webEngine.load("http://overpass-api.de/api/convert?data=%28%28way%28238669065%29%3Bway%2826577114%29%3B%29%3B%3E%3B%29%3Bout%3B&target=ol_fixed");
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
    }

    /*
    public void autofillComboBox() {
		// TODO Robin & Julius

		List<String> names;
		String input = "mün";

		names = GraphDataParser.getGraphDataParser(GraphDataConstants.CONST_XML_NODE_HIGHWAY).containsName(input);

		for (String string : names) {
			System.out.println(string);
		}

	}
    */
}
