package de.dhbw.horb.routePlanner.ui;

import java.text.DecimalFormat;
import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.controlsfx.dialog.Dialogs;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.SettingsManager;

public class RoutePlannerMainController {

    // Reference to the main application.
    public RoutePlannerMainApp routePlannerMainApp;
    public WebEngine webEngine;
    public String linkEnd = Constants.LINK_LINKEND;
    public String wayString = null;
    public String nodeString = null;
    public String calculationMethod;
    public String evaluationMethod;

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
    private ComboBox<String> countryComboBox;

    @FXML
    private Button calculateRouteButton;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab calculatedRouteTab;

    @FXML
    private RadioButton fastestRouteRadio;

    @FXML
    private RadioButton shortestRouteRadio;

    @FXML
    private ToggleGroup calculationMethodToggleGroup;

    @FXML
    private RadioButton dijkstraRouteRadio;

    @FXML
    private RadioButton aStarRouteRadio;

    @FXML
    private ToggleGroup evaluationMethodToggleGroup;

    @FXML
    private Label startLabel;

    @FXML
    private Label destinationLabel;

    @FXML
    private Label distanceLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private ListView<String> calculatedRouteListView;

    //    @FXML
    //    private CheckBox showWaysCheckBox;
    //
    //    @FXML
    //    private CheckBox showNodesCheckBox;

    @FXML
    private Button updateDataButton;

    @FXML
    private ColorPicker waysColorPicker;

    @FXML
    private ColorPicker nodesColorPicker;

    /**
     * The constructor. The constructor is called before the initialize()
     * method.
     */
    public RoutePlannerMainController() {
    }

    @FXML
    void testButtonClicked(ActionEvent event) {
	//webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());
    }

    @FXML
    void calculateRouteButtonClicked(ActionEvent event) {

	disableCalculateRouteButton();

	String start = startComboBox.getValue();
	String end = targetComboBox.getValue();

	if (start != null && end != null && !start.trim().isEmpty() && !end.trim().isEmpty()) {

	    if (calculationMethodToggleGroup.getSelectedToggle() == null) {

		Dialogs.create().title("Keine Berechnung m�glich!")
			.message("Bitte geben Sie eine Berechnungsmethode an.").showError();

	    } else {

		if (evaluationMethodToggleGroup.getSelectedToggle() == null) {

		    Dialogs.create().title("Keine Berechnung m�glich!")
			    .message("Bitte geben Sie einen Berechnungsalgorithmus an.").showError();

		} else {

		    //tabPane.getTabs().remove(calculatedRouteTab);

		    calculationMethod = null;
		    calculationMethod = getCalculationMethod();

		    evaluationMethod = null;
		    evaluationMethod = getEvaluationMethod();

		    UIEvaluationInterface.calculateRoute(start, end, calculationMethod, evaluationMethod,
			    routePlannerMainApp);
		    //webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());

		}

	    }

	} else {

	    Dialogs.create().title("Keine Berechnung m�glich!")
		    .message("Bitte geben Sie sowohl Start als auch Ziel an.").showError();

	}

    }

    /**
     * Method to detect the selected CalculationMethod
     * @return currently selected CalculationMethod as String
     */
    private String getCalculationMethod() {
	String result = null;
	if (fastestRouteRadio.isSelected()) {
	    result = Constants.EVALUATION_CALCULATION_DURATION;
	} else {
	    if (shortestRouteRadio.isSelected()) {
		result = Constants.EVALUATION_CALCULATION_DISTANCE;
	    } else {

	    }
	}
	return result;
    }

    /**
     * Method to detect the selected EvaluationMethod
     * @return currently selected EvaluationMethod as String
     */
    private String getEvaluationMethod() {
	String result = null;
	if (aStarRouteRadio.isSelected()) {
	    result = Constants.EVALUATION_METHOD_ASTAR;
	} else {
	    if (dijkstraRouteRadio.isSelected()) {
		result = Constants.EVALUATION_METHOD_DIJKSTRA;
	    } else {

	    }
	}
	return result;
    }

    @FXML
    void infoButtonClicked(ActionEvent event) {

	routePlannerMainApp.executeStartupTask();

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

	//Settings eintragen
	countryComboBox.setValue(SettingsManager.getValue(Constants.SETTINGS_COUNTRY, "Deutschland"));
	switch (SettingsManager.getValue(Constants.SETTINGS_EVALUATION_METHOD, "Dijkstra")) {
	case Constants.EVALUATION_METHOD_DIJKSTRA:
	    evaluationMethodToggleGroup.selectToggle(dijkstraRouteRadio);
	    break;
	case Constants.EVALUATION_METHOD_ASTAR:
	    evaluationMethodToggleGroup.selectToggle(aStarRouteRadio);
	    break;
	default:
	    break;
	}
	switch (SettingsManager.getValue(Constants.SETTINGS_CALCULATION_METHOD, "Dauer")) {
	case Constants.EVALUATION_CALCULATION_DURATION:
	    calculationMethodToggleGroup.selectToggle(fastestRouteRadio);
	    break;
	case Constants.EVALUATION_CALCULATION_DISTANCE:
	    calculationMethodToggleGroup.selectToggle(shortestRouteRadio);
	    break;
	default:
	    break;
	}
	Color farbe_ways = Color.web(SettingsManager.getValue(Constants.SETTINGS_COLOR_WAYS,
		Constants.SETTINGS_COLOR_WAYS_DEFAULT));
	waysColorPicker.setValue(farbe_ways);
	Color farbe_nodes = Color.web(SettingsManager.getValue(Constants.SETTINGS_COLOR_NODES,
		Constants.SETTINGS_COLOR_NODES_DEFAULT));
	nodesColorPicker.setValue(farbe_nodes);

	webEngine = testWebView.getEngine();
	webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
	    @Override
	    public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, State oldState, State newState) {
		System.out.println("WebView State: " + newState.toString());
		if (newState == Worker.State.SUCCEEDED) {

		    //Script in Website ausf�hren
		    webEngine.executeScript("init()");
		    generateLinkQuery(UIEvaluationInterface.allWayIDs, "way", "ways", SettingsManager.getValue(
			    Constants.SETTINGS_COLOR_WAYS, Constants.SETTINGS_COLOR_WAYS_DEFAULT));
		    generateLinkQuery(UIEvaluationInterface.DepDestIDs, "node", "nodes", SettingsManager.getValue(
			    Constants.SETTINGS_COLOR_NODES, Constants.SETTINGS_COLOR_NODES_DEFAULT));

		    //TODO Liste aufbauen
		    calculatedRouteListView.setItems(UIEvaluationInterface.allDestinationNodeNames);
		    startLabel.setText(startComboBox.getValue());
		    destinationLabel.setText(targetComboBox.getValue());

		    DecimalFormat f = new DecimalFormat("#0.00");
		    distanceLabel.setText(f.format(UIEvaluationInterface.distance) + " km");

		    Long ms = SupportMethods.millisecondsToSeconds(UIEvaluationInterface.duration).longValue();
		    Double m = SupportMethods.secondsToMinutes(ms.doubleValue());
		    int hours = (int) Math.floor(m / 60.0);
		    int minutes = (int) Math.floor(m % 60.0);
		    int seconds = (int) Math.floor(SupportMethods.minutesToSeconds(m % 1));
		    String routeDuration = String.format("%d Stunden %02d Minuten %02d Sekunden", hours, minutes,
			    seconds);
		    durationLabel.setText(routeDuration);
		    calculatedRouteTab.getStyleClass().removeAll("hidden");
		    SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
		    selectionModel.select(calculatedRouteTab);

		    wayString = null;
		    nodeString = null;
		    UIEvaluationInterface.allWayIDs = null;

		    enableCalculateRouteButton();
		}

	    }
	});
	evaluationMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		if (evaluationMethodToggleGroup.getSelectedToggle() != null) {
		    SettingsManager.saveSetting(Constants.SETTINGS_EVALUATION_METHOD, getEvaluationMethod());
		}
	    }
	});
	calculationMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		if (calculationMethodToggleGroup.getSelectedToggle() != null) {
		    SettingsManager.saveSetting(Constants.SETTINGS_CALCULATION_METHOD, getCalculationMethod());
		}
	    }
	});
	countryComboBox.getItems().addAll(SupportMethods.commaStrToStrList(Constants.COUNTRY_VERIFIED));
	countryComboBox.valueProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String t, String t1) {
		SettingsManager.saveSetting(Constants.SETTINGS_COUNTRY, countryComboBox.getValue());
	    }
	});
	waysColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent arg0) {
		SettingsManager.saveSetting(Constants.SETTINGS_COLOR_WAYS, waysColorPicker.getValue().toString());
	    }
	});
	nodesColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	    public void handle(ActionEvent arg0) {
		SettingsManager.saveSetting(Constants.SETTINGS_COLOR_NODES, nodesColorPicker.getValue().toString());
	    }
	});
	//webEngine.load(this.getClass().getResource("test.html").toExternalForm());
    }

    private void generateLinkQuery(LinkedList<String> list, String method, String name, String colorString) {

	if (list == null || list.isEmpty()) {

	} else {

	    long colorInt = Long.parseLong(colorString.substring(2, 8), 16);
	    long r = (colorInt & 0xFF0000) >> 16;
	    long g = (colorInt & 0xFF00) >> 8;
	    long b = (colorInt & 0xFF);

	    StringBuffer rgbColorString = new StringBuffer();
	    rgbColorString.append("rgb(");
	    rgbColorString.append(r);
	    rgbColorString.append(",");
	    rgbColorString.append(g);
	    rgbColorString.append(",");
	    rgbColorString.append(b);
	    rgbColorString.append(")");

	    String completeLink = Constants.LINK_COMPLETELINK;

	    int x = 0;
	    for (String string : list) {
		completeLink += method + "(" + string + ");";
		x++;
		if (x > 99) {
		    webEngine.executeScript("add_layer('" + name + "', '" + completeLink + linkEnd + "', '"
			    + rgbColorString + "')");
		    System.out.println("regul�r: " + completeLink + linkEnd);
		    x = 0;
		    completeLink = Constants.LINK_COMPLETELINK;
		}
	    }

	    if (completeLink != Constants.LINK_COMPLETELINK) {
		webEngine.executeScript("add_layer('" + name + "', '" + completeLink + linkEnd + "', '"
			+ rgbColorString + "')");
		System.out.println("nicht leer: " + completeLink + linkEnd);
	    }

	}
    }

    /**
     * Method to disable the "CalculateRoute"-Button
     */
    public void disableCalculateRouteButton() {

	calculateRouteButton.setDisable(true);

    }

    /**
     * Method to enable the "CalculateRoute"-Button
     */
    public void enableCalculateRouteButton() {

	calculateRouteButton.setDisable(false);

    }

    @FXML
    void updateDataButtonClicked(ActionEvent event) {

	routePlannerMainApp.allXMLsExist = false;
	routePlannerMainApp.executeStartupTask();

    }

    public void loadOverpassHTML() {
	webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());
    }
}
