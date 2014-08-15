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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.SettingsManager;

/**
 * Controller-Klasse für die Hauptanwendung. Stellt Funktionen der einzelnen JavaFX-Komponenten zur Verfügung.
 * 
 */
public class RoutePlannerMainController {

    public RoutePlannerMainApp routePlannerMainApp;
    public WebEngine webEngine;
    public String linkEnd = Constants.LINK_LINKEND;
    public String wayString = null;
    public String nodeString = null;
    public String calculationMethod;
    public String evaluationMethod;
    public boolean flag = false;

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
    @FXML
    private Button updateDataButton;
    @FXML
    private ColorPicker waysColorPicker;
    @FXML
    private ColorPicker nodesColorPicker;
    @FXML
    public ProgressIndicator calculateRouteProgressIndicator;

    public RoutePlannerMainController() {
    }

    /**
     * Initialisierung aller wichtigen JavaFX-Komponenten.
     * 
     * @param routePlannerMainApp Referenz auf die MainApp
     */
    public void setRoutePlannerMainApp(final RoutePlannerMainApp routePlannerMainApp) {
	this.routePlannerMainApp = routePlannerMainApp;
	new AutoCompleteComboBoxListener<>(startComboBox);
	new AutoCompleteComboBoxListener<>(targetComboBox);
	countryComboBox.setValue(SettingsManager.getValue(Constants.SETTINGS_COUNTRY, "Deutschland"));
	switch (SettingsManager.getValue(Constants.SETTINGS_EVALUATION_METHOD, "Dijkstra")) {
	case Constants.EVALUATION_METHOD_DIJKSTRA:
	    evaluationMethodToggleGroup.selectToggle(this.dijkstraRouteRadio);
	    break;
	case Constants.EVALUATION_METHOD_ASTAR:
	    evaluationMethodToggleGroup.selectToggle(this.aStarRouteRadio);
	    break;
	default:
	    break;
	}
	switch (SettingsManager.getValue(Constants.SETTINGS_CALCULATION_METHOD, "Dauer")) {
	case Constants.EVALUATION_CALCULATION_DURATION:
	    calculationMethodToggleGroup.selectToggle(this.fastestRouteRadio);
	    break;
	case Constants.EVALUATION_CALCULATION_DISTANCE:
	    calculationMethodToggleGroup.selectToggle(this.shortestRouteRadio);
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
		if (newState == Worker.State.SUCCEEDED) {

		    RoutePlannerMainController.this.webEngine.executeScript("init()");
		    generateLinkQuery(UIEvaluationInterface.allWayIDs, "way", "ways", SettingsManager.getValue(
			    Constants.SETTINGS_COLOR_WAYS, Constants.SETTINGS_COLOR_WAYS_DEFAULT));
		    generateLinkQuery(UIEvaluationInterface.DepDestIDs, "node", "nodes", SettingsManager.getValue(
			    Constants.SETTINGS_COLOR_NODES, Constants.SETTINGS_COLOR_NODES_DEFAULT));
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
		    String routeDuration = String.format("%d Stunden, %02d Minuten und %02d Sekunden", hours, minutes,
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
	evaluationMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    @Override
	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		if (RoutePlannerMainController.this.evaluationMethodToggleGroup.getSelectedToggle() != null) {
		    SettingsManager.saveSetting(Constants.SETTINGS_EVALUATION_METHOD, getEvaluationMethod());
		}
	    }
	});
	calculationMethodToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	    @Override
	    public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
		if (RoutePlannerMainController.this.calculationMethodToggleGroup.getSelectedToggle() != null) {
		    SettingsManager.saveSetting(Constants.SETTINGS_CALCULATION_METHOD, getCalculationMethod());
		}
	    }
	});
	countryComboBox.getItems().addAll(SupportMethods.commaStrToStrList(Constants.COUNTRY_VERIFIED));
	countryComboBox.valueProperty().addListener(new ChangeListener<String>() {
	    @Override
	    public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String t, String t1) {
		if (flag == false) {
		    flag = true;
		    Action response = Dialogs.create().title("Änderung des Landes erkannt").masthead(
			    "Damit Routen für " + t1
			            + " berechnet werden können, muss das Programm neu geladen werden. "
			            + "Dabei müssen eventuell auch benötigte Daten heruntergeladen und "
			            + "erstellt werden. Dies kann sehr lange dauern.").message(
			    "Wollen Sie jetzt " + t1 + " auswählen?").actions(Dialog.Actions.OK, Dialog.Actions.CANCEL)
			    .showConfirm();
		    if (response == Dialog.Actions.OK) {
			SettingsManager.saveSetting(Constants.SETTINGS_COUNTRY,
			        RoutePlannerMainController.this.countryComboBox.getValue());
			routePlannerMainApp.checkXMLs();
			routePlannerMainApp.executeStartupTask();
		    } else {
			countryComboBox.setValue(t);
		    }
		}
		flag = false;
	    }
	});
	waysColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent arg0) {
		SettingsManager.saveSetting(Constants.SETTINGS_COLOR_WAYS,
		        RoutePlannerMainController.this.waysColorPicker.getValue().toString());
		Dialogs.create().title("Änderung der Farbe erkannt").masthead(null).message(
		        Constants.ROUTEPLANNER_POPUP_COLOR_CHANGED).showInformation();
	    }
	});
	nodesColorPicker.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent arg0) {
		SettingsManager.saveSetting(Constants.SETTINGS_COLOR_NODES,
		        RoutePlannerMainController.this.nodesColorPicker.getValue().toString());
		Dialogs.create().title("Änderung der Farbe erkannt").masthead(null).message(
		        Constants.ROUTEPLANNER_POPUP_COLOR_CHANGED).showInformation();
	    }
	});
	initializeTooltips();
    }

    /**
     * Bestimmt den aktuell ausgewählten Berechnungsalgorithmus
     * 
     * @return CalculationMethod als String
     */
    private String getCalculationMethod() {
	String result = null;
	if (fastestRouteRadio.isSelected()) {
	    result = Constants.EVALUATION_CALCULATION_DURATION;
	} else if (shortestRouteRadio.isSelected()) {
	    result = Constants.EVALUATION_CALCULATION_DISTANCE;
	}
	return result;
    }

    /**
     * Bestimmt die aktuell ausgewählte Art der Auswertung (kürzeste/schnellste Route).
     * 
     * @return EvaluationMethod als String
     */
    private String getEvaluationMethod() {
	String result = null;
	if (aStarRouteRadio.isSelected()) {
	    result = Constants.EVALUATION_METHOD_ASTAR;
	} else if (dijkstraRouteRadio.isSelected()) {
	    result = Constants.EVALUATION_METHOD_DIJKSTRA;
	}
	return result;
    }

    /**
     * Methode, die aufgerufen wird, um Ways/Nodes auf der Karte zu markieren. Führt Javascript in overpass.html aus.
     * 
     * @param list Liste mit IDs aller zu markierenden Ways/Nodes
     * @param method Angabe, ob Nodes oder Ways markiert werden sollen
     * @param name Name des in die Karte eingefügten Layers, der die Markierungen enthält
     * @param colorString Farbe, in der Ways/Nodes markiert werden sollen
     */
    private void generateLinkQuery(LinkedList<String> list, String method, String name, String colorString) {
	if (list == null || list.isEmpty())
	    return;

	long colorInt = Long.parseLong(colorString.substring(2, 8), 16);
	long r = (colorInt & 0xFF0000) >> 16;
	long g = (colorInt & 0xFF00) >> 8;
	long b = colorInt & 0xFF;
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
		x = 0;
		completeLink = Constants.LINK_COMPLETELINK;
	    }
	}
	if (completeLink != Constants.LINK_COMPLETELINK) {
	    webEngine.executeScript("add_layer('" + name + "', '" + completeLink + linkEnd + "', '" + rgbColorString
		    + "')");
	}
    }

    /**
     * Läd die Datei overpass.html in die WebEngine
     */
    public void loadOverpassHTML() {
	webEngine.load(this.getClass().getResource("overpass.html").toExternalForm());
    }

    /**
     * Initialisiert Tooltips für verschiedene JavaFX-Komponenten
     */
    public void initializeTooltips() {
	closeButton.setTooltip(new Tooltip(Constants.TOOLTIP_CLOSE_BUTTON));
	infoButton.setTooltip(new Tooltip(Constants.TOOLTIP_INFO_BUTTON));
	calculateRouteButton.setTooltip(new Tooltip(Constants.TOOLTIP_CALCULATE_ROUTE_BUTTON));
	updateDataButton.setTooltip(new Tooltip(Constants.TOOLTIP_UPDATE_DATA_BUTTON));
	startComboBox.setTooltip(new Tooltip(Constants.TOOLTIP_START_COMBOBOX));
	targetComboBox.setTooltip(new Tooltip(Constants.TOOLTIP_TARGET_COMBOBOX));
	countryComboBox.setTooltip(new Tooltip(Constants.TOOLTIP_COUNTRY_COMBOBOX));
	waysColorPicker.setTooltip(new Tooltip(Constants.TOOLTIP_WAYS_COLORPICKER));
	nodesColorPicker.setTooltip(new Tooltip(Constants.TOOLTIP_NODES_COLORPICKER));
	fastestRouteRadio.setTooltip(new Tooltip(Constants.TOOLTIP_FASTEST_ROUTE_RADIO));
	shortestRouteRadio.setTooltip(new Tooltip(Constants.TOOLTIP_SHORTEST_ROUTE_RADIO));
	dijkstraRouteRadio.setTooltip(new Tooltip(Constants.TOOLTIP_DIJKSTRA_ROUTE_RADIO));
	aStarRouteRadio.setTooltip(new Tooltip(Constants.TOOLTIP_ASTAR_ROUTE_RADIO));
    }

    /**
     * Löst die Berechnung der Route sowie die Anzeige des Ergebnisses aus. Verursacht Fehler wenn Berechnung aufgrund
     * der Eingabe nicht möglich ist.
     * 
     * @param event
     */
    @FXML
    void calculateRouteButtonClicked(ActionEvent event) {
	disableCalculateRouteButton();
	String start = startComboBox.getValue();
	String end = targetComboBox.getValue();
	if (start != null && end != null && !start.trim().isEmpty() && !end.trim().isEmpty()) {
	    if (calculationMethodToggleGroup.getSelectedToggle() == null) {
		Dialogs.create().title("Keine Berechnung möglich!").message(
		        "Bitte geben Sie eine Berechnungsmethode an.").showError();
		enableCalculateRouteButton();
	    } else if (evaluationMethodToggleGroup.getSelectedToggle() == null) {
		Dialogs.create().title("Keine Berechnung möglich!").message(
		        "Bitte geben Sie einen Berechnungsalgorithmus an.").showError();
		enableCalculateRouteButton();
	    } else if (start.equals(end)) {
		Dialogs.create().title("Keine Berechnung möglich!").message("Start und Ziel sind identisch.")
		        .showError();
		enableCalculateRouteButton();
	    } else {
		calculateRouteProgressIndicator.setOpacity(1.0);
		calculationMethod = null;
		calculationMethod = getCalculationMethod();
		evaluationMethod = null;
		evaluationMethod = getEvaluationMethod();
		UIEvaluationInterface.calculateRoute(start, end, calculationMethod, evaluationMethod,
		        routePlannerMainApp);
	    }
	} else {
	    Dialogs.create().title("Keine Berechnung möglich!").message(
		    "Bitte geben Sie sowohl Start als auch Ziel an.").showError();
	    enableCalculateRouteButton();
	}
    }

    /**
     * Zeigt ein Informations-Fenster über das Programm.
     * 
     * @param event
     */
    @FXML
    void infoButtonClicked(ActionEvent event) {
	Dialogs.create().message(Constants.ROUTEPLANNER_INFO_STRING).style(DialogStyle.CROSS_PLATFORM_DARK)
	        .showInformation();
    }

    /**
     * Schließt die Stage, beendet das Programm.
     * 
     * @param event
     */
    @FXML
    void closeButtonClicked(ActionEvent event) {
	routePlannerMainApp.primaryStage.close();
    }

    /**
     * Deaktiviert den "Route berechnen"-Button
     */
    public void disableCalculateRouteButton() {
	calculateRouteButton.setDisable(true);
    }

    /**
     * Aktiviert den "Route berechnen"-Button
     */
    public void enableCalculateRouteButton() {
	calculateRouteButton.setDisable(false);
    }

    /**
     * Startet das Programm neu und läd alle XML-Dateien neu
     * 
     * @param event
     */
    @FXML
    void updateDataButtonClicked(ActionEvent event) {
	Action response = Dialogs.create().title("Daten aktualisieren").masthead(null).message(
	        Constants.ROUTEPLANNER_POPUP_DATA_UPDATE).actions(Dialog.Actions.OK, Dialog.Actions.CANCEL)
	        .showConfirm();
	if (response == Dialog.Actions.OK) {
	    routePlannerMainApp.allXMLsExist = false;
	    routePlannerMainApp.graphDataExists = false;
	    routePlannerMainApp.executeStartupTask();
	}
    }
}
