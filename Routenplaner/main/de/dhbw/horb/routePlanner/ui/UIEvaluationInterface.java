package de.dhbw.horb.routePlanner.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import org.controlsfx.dialog.Dialogs;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.evaluation.aStar.AStar;
import de.dhbw.horb.routePlanner.evaluation.dijkstra.Dijkstra;

public class UIEvaluationInterface {

    private static Task<Integer> task;
    public static LinkedList<String> allWayIDs;
    public static LinkedList<String> allNodeIDs;
    public static LinkedList<String> DepDestIDs;
    public static ArrayList<String> allDestinationNodes;
    public static ObservableList<String> allDestinationNodeNames;
    public static Double distance;
    public static Double duration;
    static List<Map<String, String>> route;

    public static void calculateRoute(final String departure, final String destination, final String calculationMethod,
	    final String evaluationMethod, final RoutePlannerMainApp mainApp) {

	task = new Task<Integer>() {
	    @Override
	    protected Integer call() throws Exception {
		route = null;
		if ((departure == null) || (destination == null) || (calculationMethod == null)
			|| (evaluationMethod == null)) {
		    this.cancel();
		    return -1;
		}

		if ((calculationMethod != Constants.EVALUATION_CALCULATION_DISTANCE)
			&& (calculationMethod != Constants.EVALUATION_CALCULATION_DURATION)) {
		    System.err.println("Unknown calculation method.");
		    this.cancel();
		    return -2;
		}

		switch (evaluationMethod) {
		case Constants.EVALUATION_METHOD_ASTAR:
		    AStar aStar = new AStar(departure, destination);
		    route = aStar.calculateWay(calculationMethod);
		    if ((route == null) || route.isEmpty()) {
			System.err.println("AStar: Keine Route gefunden");
			this.cancel();
		    }
		    break;

		case Constants.EVALUATION_METHOD_DIJKSTRA:
		    Dijkstra dijkstra = new Dijkstra(departure, destination);
		    route = dijkstra.calculateRoute(calculationMethod);
		    if ((route == null) || route.isEmpty()) {
			System.err.println("Dijkstra: Keine Route gefunden");
			this.cancel();
		    }
		    break;
		default:
		    System.err.println("Unknown evaluation method.");
		}

		distance = 0.0;
		duration = 0.0;
		allWayIDs = new LinkedList<String>();
		allNodeIDs = new LinkedList<String>();
		DepDestIDs = new LinkedList<String>();
		allDestinationNodes = new ArrayList<String>();
		String departureNodeID = route.get(0).get(Constants.NEW_ROUTE_DEPARTURENODEID);
		//String departureNodeName = route.get(0).get(Constants.NEW_ROUTE_DEPARTURENODENAME);
		String destinationNodeID = route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODEID);
		//String destinationNodeName = route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODENAME);
		DepDestIDs.add(departureNodeID);
		DepDestIDs.add(destinationNodeID);

		for (Map<String, String> way : route) {

		    String dist = way.get(Constants.NEW_ROUTE_DISTANCE);
		    String dur = way.get(Constants.NEW_ROUTE_DURATION);
		    List<String> wayIDs = SupportMethods.commaStrToStrList(way.get(Constants.NEW_ROUTE_WAYIDS));

		    if ((wayIDs == null) || (dist == null) || (dur == null) || !SupportMethods.isNumeric(dist)
			    || !SupportMethods.isNumeric(dur) || wayIDs.isEmpty()) continue;

		    allWayIDs.addAll(wayIDs);
		    allNodeIDs.add(way.get(Constants.NEW_ROUTE_DEPARTURENODEID));
		    allDestinationNodes.add(way.get(Constants.NEW_ROUTE_DEPARTURENODENAME));
		    distance += Double.valueOf(dist);
		    duration += Double.valueOf(dur);
		}

		allDestinationNodes.add(route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODENAME));
		allNodeIDs.add(route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODEID));
		allDestinationNodeNames = FXCollections.observableArrayList(allDestinationNodes);

		return 0;
	    }
	};

	task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		mainApp.controller.loadOverpassHTML();
	    }
	});

	task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		Dialogs.create()
			.title("Keine Berechnung möglich!")
			.message(
				"Bei der Berechnung der Route ist ein Fehler aufgetreten. Es wurde keine Route gefunden.")
			.showError();
		mainApp.controller.enableCalculateRouteButton();
	    }
	});

	Thread th = new Thread(task);
	th.setDaemon(true);
	th.start();

    }

}
