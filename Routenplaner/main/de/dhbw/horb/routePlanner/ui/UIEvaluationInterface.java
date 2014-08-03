package de.dhbw.horb.routePlanner.ui;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.evaluation.aStar.AStar;

public class UIEvaluationInterface {

    public static void calculateRoute(final String departure, final String destination) {

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		String evaluationMethod;
		evaluationMethod = "AStern";

		switch (evaluationMethod) {
		case "AStern":
		    AStar evaluation = new AStar(departure, destination);
		    evaluation.calculateWay(Constants.NEW_ROUTE_DISTANCE);
		    break;

		case "Dijkstra":
		    //	TODO Simon: Aufruf von Dijkstra
		    break;
		default:
		    System.err.println("Unknown evaluation method.");
		}
	    }
	}).start();

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
	    System.err.println("Keine Route gefunden");
	    return;
	}

	DecimalFormat f = new DecimalFormat("#0.00");

	Double distance = 0.0;
	Double duration = 0.0;
	List<String> allWayIDs = new LinkedList<String>();
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
	    distance += Double.valueOf(dist);
	    duration += Double.valueOf(dur);
	}

	System.out.println("Von: " + route.get(0).get(Constants.NEW_ROUTE_DEPARTURENODENAME) + " mit ID: "
		+ departureNodeID);
	System.out.println("Nach: " + route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODENAME)
		+ " mit ID: " + destinationNodeID);
	System.out.println(f.format(distance) + " km");
	Double m = SupportMethods.secondsToMinutes(duration);
	int hours = (int) Math.floor(m / 60.0);
	int minutes = (int) Math.floor(m % 60.0);
	int seconds = (int) Math.floor(SupportMethods.minutesToSeconds(m % 1));
	System.out.printf("Dauer: %d Stunden %02d Minuten %02d Sekunden \n", hours, minutes, seconds);
	System.out.println("Gesamte Liste: " + route);
	System.out.println("Gesamte WayID Liste: " + allWayIDs);

	//TODO Robin Methoden aufrufe hinzufügen
    }
}
