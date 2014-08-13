package de.dhbw.horb.routePlanner.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.evaluation.aStar.AStar;
import de.dhbw.horb.routePlanner.evaluation.dijkstra.Dijkstra;

public class UIEvaluationInterface {

    private static Task<Integer> task;
    public static LinkedList<String> allWayIDs;
    public static LinkedList<String> allNodeIDs;
    public static ArrayList<String> allDestinationNodes;
    public static ObservableList<String> allDestinationNodeNames;

    //    public static RoutePlannerMainApp routePlannerMainApp;

    public static void calculateRoute(final String departure, final String destination, final String calculationMethod,
	    final String evaluationMethod, final RoutePlannerMainApp mainApp) {

	//	routePlannerMainApp = mainApp;

	task = new Task<Integer>() {
	    @Override
	    protected Integer call() throws Exception {
		if (departure == null || destination == null || calculationMethod == null || evaluationMethod == null) {
		    return -1;
		}

		if (calculationMethod != Constants.EVALUATION_CALCULATION_DISTANCE
			&& calculationMethod != Constants.EVALUATION_CALCULATION_DURATION) {
		    System.err.println("Unknown calculation method.");
		    return -2;
		}

		switch (evaluationMethod) {
		case Constants.EVALUATION_METHOD_ASTAR:
		    AStar aStar = new AStar(departure, destination);
		    aStar.calculateWay(calculationMethod);
		    break;

		case Constants.EVALUATION_METHOD_DIJKSTRA:
		    Dijkstra dijkstra = new Dijkstra(departure, destination);
		    dijkstra.calculateRoute(calculationMethod);
		    break;
		default:
		    System.err.println("Unknown evaluation method.");
		}
		return 0;
	    }
	};

	task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	    @Override
	    public void handle(WorkerStateEvent event) {
		mainApp.controller.loadOverpassHTML();
	    }
	});

	//	task.setOnCancelled(new EventHandler<WorkerStateEvent>() {
	//	    @Override
	//	    public void handle(WorkerStateEvent event) {
	//		
	//	    }
	//	});
	//

	Thread th = new Thread(task);
	th.setDaemon(true);
	th.start();

	//	new Thread(new Runnable() {
	//
	//	    @Override
	//	    public void run() {
	//
	//		if (departure == null || destination == null || calculationMethod == null || evaluationMethod == null) {
	//		    return;
	//		}
	//
	//		if (calculationMethod != Constants.EVALUATION_CALCULATION_DISTANCE
	//			&& calculationMethod != Constants.EVALUATION_CALCULATION_DURATION) {
	//		    System.err.println("Unknown calculation method.");
	//		    return;
	//		}
	//
	//		switch (evaluationMethod) {
	//		case Constants.EVALUATION_METHOD_ASTAR:
	//		    AStar aStar = new AStar(departure, destination);
	//		    aStar.calculateWay(calculationMethod);
	//		    break;
	//
	//		case Constants.EVALUATION_METHOD_DIJKSTRA:
	//		    Dijkstra dijkstra = new Dijkstra(departure, destination);
	//		    dijkstra.calculateRoute(calculationMethod);
	//		    break;
	//		default:
	//		    System.err.println("Unknown evaluation method.");
	//		}
	//	    }
	//	}).start();

    }

    /**
     * 
     * @param route Eine Liste aus Maps mit den Eigenschaften:
     * Constants.NEW_ROUTE_DEPARTURENODEID = Abfahrt Knoten ID
     * Constants.NEW_ROUTE_DEPARTURENODENAME = Abfahrt Name
     * Constants.NEW_ROUTE_DESTINATIONNODEID = Ziel Knoten ID
     * Constants.DESTINATIONNODENAME = Ziel Name
     * Constants.NEW_ROUTE_DISTANCE = Strecke in km
     * Constants.NEW_ROUTE_DURATION = Dauer in Sekunden
     * Constants.NEW_ROUTE_WAYIDS = Wege in Komma separiertem String
     * Constants.NEW_ROUTE_NUMBER = Autobahn Nummer 
     * 
     */
    public static void printRoute(List<Map<String, String>> route) {
	//	TODO Simon: Routen in selbigem Format übergeben.

	if (route == null || route.isEmpty()) {
	    //TODO Robin: Fehler ausgeben da keine Route gefunden werden konnte.
	    System.err.println("Keine Route gefunden");
	    return;
	}

	DecimalFormat f = new DecimalFormat("#0.00");

	Double distance = 0.0;
	Double duration = 0.0;
	allWayIDs = new LinkedList<String>();
	allNodeIDs = new LinkedList<String>();
	allDestinationNodes = new ArrayList<String>();
	String departureNodeID = route.get(0).get(Constants.NEW_ROUTE_DEPARTURENODEID);
	String destinationNodeID = route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODEID);

	for (Map<String, String> way : route) {

	    String dist = way.get(Constants.NEW_ROUTE_DISTANCE);
	    String dur = way.get(Constants.NEW_ROUTE_DURATION);
	    List<String> wayIDs = SupportMethods.commaStrToStrList(way.get(Constants.NEW_ROUTE_WAYIDS));

	    if (wayIDs == null || dist == null || dur == null || !SupportMethods.isNumeric(dist)
		    || !SupportMethods.isNumeric(dur) || wayIDs.isEmpty())
		continue;

	    allWayIDs.addAll(wayIDs);
	    allNodeIDs.add(way.get(Constants.NEW_ROUTE_DEPARTURENODEID));
	    allDestinationNodes.add(way.get(Constants.NEW_ROUTE_DEPARTURENODENAME));
	    distance += Double.valueOf(dist);
	    duration += Double.valueOf(dur);
	}

	allDestinationNodes.add(route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODENAME));
	allNodeIDs.add(route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODEID));
	System.out.println("Von: " + route.get(0).get(Constants.NEW_ROUTE_DEPARTURENODENAME) + " mit ID: "
		+ departureNodeID);
	System.out.println("Nach: " + route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODENAME)
		+ " mit ID: " + destinationNodeID);
	System.out.println(f.format(distance) + " km");
	Long ms = SupportMethods.millisecondsToSeconds(duration).longValue();
	Double m = SupportMethods.secondsToMinutes(ms.doubleValue());
	int hours = (int) Math.floor(m / 60.0);
	int minutes = (int) Math.floor(m % 60.0);
	int seconds = (int) Math.floor(SupportMethods.minutesToSeconds(m % 1));
	System.out.printf("Dauer: %d Stunden %02d Minuten %02d Sekunden \n", hours, minutes, seconds);
	System.out.println("Gesamte Liste: " + route);
	System.out.println("Gesamte WayID Liste: " + allWayIDs);
	System.out.println("Gesamte NodeID Liste: " + allNodeIDs);
	System.out.println("Alle DestinationNodes: " + allDestinationNodes);
	allDestinationNodeNames = FXCollections.observableArrayList(allDestinationNodes);

	//TODO Robin Methoden aufrufe hinzufügen

    }
}
