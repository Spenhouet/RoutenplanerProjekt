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

import org.controlsfx.dialog.DialogStyle;
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
     * The constructor. The constructor is called before the initialize() method.
     */
    public RoutePlannerMainController() {}

    @FXML
    void calculateRouteButtonClicked(ActionEvent event) {

	disableCalculateRouteButton();

	String start = this.startComboBox.getValue();
	String end = this.targetComboBox.getValue();

	if ((start != null) && (end != null) && !start.trim().isEmpty() && !end.trim().isEmpty()) {

	    if (this.calculationMethodToggleGroup.getSelectedToggle() == null)
		Dialogs.create().title("Keine Berechnung möglich!")
		.message("Bitte geben Sie eine Berechnungsmethode an.").showError();
	    else if (this.evaluationMethodToggleGroup.getSelectedToggle() == null)
		Dialogs.create().title("Keine Berechnung möglich!")
		.message("Bitte geben Sie einen Berechnungsalgorithmus an.").showError();
	    else if (start == end)
		Dialogs.create().title("Keine Berechnung möglich!").message("Start und Ziel sind identisch.")
		.showError();
	    else {

		//tabPane.getTabs().remove(calculatedRouteTab);

		this.calculationMethod = null;
		this.calculationMethod = getCalculationMethod();

		this.evaluationMethod = null;
		this.evaluationMethod = getEvaluationMethod();

		UIEvaluationInterface.calculateRoute(start, end, this.calculationMethod, this.evaluationMethod,
			this.routePlannerMainApp);
		//webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());

	    }

	} else Dialogs.create().title("Keine Berechnung möglich!")
	.message("Bitte geben Sie sowohl Start als auch Ziel an.").showError();

    }

    /**
     * Method to detect the selected CalculationMethod
     *
     * @return currently selected CalculationMethod as String
     */
    private String getCalculationMethod() {
	String result = null;
	if (this.fastestRouteRadio.isSelected())
	    result = Constants.EVALUATION_CALCULATION_DURATION;
	else if (this.shortestRouteRadio.isSelected())
	    result = Constants.EVALUATION_CALCULATION_DISTANCE;
	else {

	}
	return result;
    }

    /**
     * Method to detect the selected EvaluationMethod
     *
     * @return currently selected EvaluationMethod as String
     */
    private String getEvaluationMethod() {
	String result = null;
	if (this.aStarRouteRadio.isSelected())
	    result = Constants.EVALUATION_METHOD_ASTAR;
	else if (this.dijkstraRouteRadio.isSelected())
	    result = Constants.EVALUATION_METHOD_DIJKSTRA;
	else {

	}
	return result;
    }

    @FXML
    void infoButtonClicked(ActionEvent event) {

	Dialogs.create()
		.message(
			"Dieses Java-Programm entstand im Rahmen der Vorlesung \"Programmieren I + II\" an der DHBW "
			+ "Stuttgart Campus Horb. \n\n\u00a9 2014 Julius Mahlenbrey, Simon Stehle, "
			+ "Sebastian Penhouet und Robin Kinting").style(DialogStyle.CROSS_PLATFORM_DARK)
			.showInformation();

    }

    @FXML
    void closeButtonClicked(ActionEvent event) {
	this.routePlannerMainApp.primaryStage.close();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setRoutePlannerMainApp(RoutePlannerMainApp routePlannerMainApp) {
	this.routePlannerMainApp = routePlannerMainApp;
	new AutoCompleteComboBoxListener<>(this.startComboBox);
	new AutoCompleteComboBoxListener<>(this.targetComboBox);

	//Settings eintragen
	this.countryComboBox.setValue(SettingsManager.getValue(Constants.SETTINGS_COUNTRY, "Deutschland"));
	switch (SettingsManager.getValue(Constants.SETTINGS_EVALUATION_METHOD, "Dijkstra")) {
	case Constants.EVALUATION_METHOD_DIJKSTRA:
	    this.evaluationMethodToggleGroup.selectToggle(this.dijkstraRouteRadio);
	    break;
	case Constants.EVALUATION_METHOD_ASTAR:
	    this.evaluationMethodToggleGroup.selectToggle(this.aStarRouteRadio);
	    break;
	default:
	    break;
	}
	switch (SettingsManager.getValue(Constants.SETTINGS_CALCULATION_METHOD, "Dauer")) {
	case Constants.EVALUATION_CALCULATION_DURATION:
	    this.calculationMethodToggleGroup.selectToggle(this.fastestRouteRadio);
	    break;
	case Constants.EVALUATION_CALCULATION_DISTANCE:
	    this.calculationMethodToggleGroup.selectToggle(this.shortestRouteRadio);
	    break;
	default:
	    break;
	}
	Color farbe_ways = Color.web(SettingsManager.getValue(Constants.SETTINGS_COLOR_WAYS,
		Constants.SETTINGS_COLOR_WAYS_DEFAULT));
	this.waysColorPicker.setValue(farbe_ways);
	Color farbe_nodes = Color.web(SettingsManager.getValue(Constants.SETTINGS_COLOR_NODES,
		Constants.SETTINGS_COLOR_NODES_DEFAULT));
	this.nodesColorPicker.setValue(farbe_nodes);

	this.webEngine = this.testWebView.getEngine();
	this.webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
	    @Override
	    public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, State oldState, State newState) {
		System.out.println("WebView State: " + newState.toString());
		if (newState == Worker.State.SUCCEEDED) {

		    //Script in Website ausführen
		    RoutePlannerMainController.this.webEngine.executeScript("init()");
		    generateLinkQuery(UIEvaluationInterface.allWayIDs, "way", "ways", SettingsManager.getValue(
			    Constants.SETTINGS_COLOR_WAYS, Constants.SETTINGS_COLOR_WAYS_DEFAULT));
		    generateLinkQuery(UIEvaluationInterface.DepDestIDs, "node", "nodes", SettingsManager.getValue(
			    Constants.SETTINGS_COLOR_NODES, Constants.SETTINGS_COLOR_NODES_DEFAULT));

		    //Liste aufbauen
		    RoutePlannerMainController.this.calculatedRouteListView
		    .setItems(UIEvaluationInterface.allDestinationNodeNames);
		    RoutePlannerMainController.this.startLabel.setText(RoutePlannerMainController.this.startComboBox
			    .getValue());
		    RoutePlannerMainController.this.destinationLabel
		    .setText(RoutePlannerMainController.this.targetComboBox.getValue());

		    DecimalFormat f = new DecimalFormat("#0.00");
		    RoutePlannerMainController.this.distanceLabel.setText(f.format(UIEvaluationInterface.distance)
			    + " km");

		    Long ms = SupportMethods.millisecondsToSeconds(UIEvaluationInterface.duration).longValue();
		    Double m = SupportMethods.secondsToMinutes(ms.doubleValue());
		    int hours = (int) Math.floor(m / 60.0);
		    int minutes = (int) Math.floor(m % 60.0);
		    int seconds = (int) Math.floor(SupportMethods.minutesToSeconds(m % 1));
		    String routeDuration = String.format("%d Stunden %02d Minuten %02d Sekunden", hours, minutes,
			    seconds);
		    RoutePlannerMainController.this.durationLabel.setText(routeDuration);
		    RoutePlannerMainController.this.calculatedRouteTab.getStyleClass().removeAll("hidden");
		    SingleSelectionModel<Tab> selectionModel = RoutePlannerMainController.this.tabPane
			    .getSelectionModel();
		    selectionModel.select(RoutePlannerMainController.this.calculatedRouteTab);

		    RoutePlannerMainController.this.wayString = null;
		    RoutePlannerMainController.this.nodeString = null;
		    UIEvaluationInterface.allWayIDs = null;

		    enableCalculateRouteButton();
		}

	    }
	});
	this.evaluationMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    @Override
	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		if (RoutePlannerMainController.this.evaluationMethodToggleGroup.getSelectedToggle() != null)
		    SettingsManager.saveSetting(Constants.SETTINGS_EVALUATION_METHOD, getEvaluationMethod());
	    }
	});
	this.calculationMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    @Override
	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		if (RoutePlannerMainController.this.calculationMethodToggleGroup.getSelectedToggle() != null)
		    SettingsManager.saveSetting(Constants.SETTINGS_CALCULATION_METHOD, getCalculationMethod());
	    }
	});
	this.countryComboBox.getItems().addAll(SupportMethods.commaStrToStrList(Constants.COUNTRY_VERIFIED));
	this.countryComboBox.valueProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String t, String t1) {
		SettingsManager.saveSetting(Constants.SETTINGS_COUNTRY,
			RoutePlannerMainController.this.countryComboBox.getValue());
	    }
	});
	this.waysColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent arg0) {
		SettingsManager.saveSetting(Constants.SETTINGS_COLOR_WAYS,
			RoutePlannerMainController.this.waysColorPicker.getValue().toString());
	    }
	});
	this.nodesColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent arg0) {
		SettingsManager.saveSetting(Constants.SETTINGS_COLOR_NODES,
			RoutePlannerMainController.this.nodesColorPicker.getValue().toString());
	    }
	});
	//webEngine.load(this.getClass().getResource("test.html").toExternalForm());
    }

    private void generateLinkQuery(LinkedList<String> list, String method, String name, String colorString) {

	if ((list == null) || list.isEmpty()) {

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
		    this.webEngine.executeScript("add_layer('" + name + "', '" + completeLink + this.linkEnd + "', '"
			    + rgbColorString + "')");
		    System.out.println("regulär: " + completeLink + this.linkEnd);
		    x = 0;
		    completeLink = Constants.LINK_COMPLETELINK;
		}
	    }

	    if (completeLink != Constants.LINK_COMPLETELINK) {
		this.webEngine.executeScript("add_layer('" + name + "', '" + completeLink + this.linkEnd + "', '"
			+ rgbColorString + "')");
		System.out.println("nicht leer: " + completeLink + this.linkEnd);
	    }

	}
    }

    /**
     * Method to disable the "CalculateRoute"-Button
     */
    public void disableCalculateRouteButton() {

	this.calculateRouteButton.setDisable(true);

    }

    /**
     * Method to enable the "CalculateRoute"-Button
     */
    public void enableCalculateRouteButton() {

	this.calculateRouteButton.setDisable(false);

    }

    @FXML
    void updateDataButtonClicked(ActionEvent event) {

	this.routePlannerMainApp.allXMLsExist = false;
	this.routePlannerMainApp.executeStartupTask();

    }

    public void loadOverpassHTML() {
	this.webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());
    }
}
