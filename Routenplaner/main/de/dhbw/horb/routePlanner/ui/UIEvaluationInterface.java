package de.dhbw.horb.routePlanner.ui;

import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.data.StAXNodeParser;
import de.dhbw.horb.routePlanner.evaluation.aStar.AStar;

public class UIEvaluationInterface {

    public static void calculateRoute(final String departure, final String destination) {

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		//		DELETE only test
		System.out.println("Start: " + departure + "  Destination: " + destination);

		List<String> departureIDs = StAXNodeParser.getStAXNodeParser().getIDsForName(departure);
		List<String> destinationIDs = StAXNodeParser.getStAXNodeParser().getIDsForName(destination);

		//		DELETE only test
		System.out.println("Departure ID: " + departureIDs + "  Destination ID: " + destinationIDs);

		String evaluationMethod;
		evaluationMethod = "AStern";

		switch (evaluationMethod) {
		case "AStern":
		    AStar evaluation = new AStar(departure, destination);
		    evaluation.calculateWay();
		    break;

		case "Dijkstra":

		    break;
		default:
		    System.err.println("Unknown evaluation method.");
		}

		//		TODO Simon: Aufruf von Dijkstra
	    }
	}).start();

    }

    /**
     * 
     * @param route Eine Liste aus Maps mit den Eigenschaften:
     * Constants.NEW_ROUTE_DEPARTURENODEID = Abfahrt Knoten ID
     * Constants.NEW_ROUTE_DESTINATIONNODEID = Ziel Knoten ID
     * Constants.NEW_ROUTE_DISTANCE = Strecke in km
     * Constants.NEW_ROUTE_DURATION = Dauer in Millisekunden
     * Constants.NEW_ROUTE_WAYIDS = Wege in Komma separiertem String
     * Constants.NEW_ROUTE_NUMBER = Autobahn Nummer
     * 
     */
    public static void printRoute(List<Map<String, String>> route) {
	//	TODO Simon: Routen in selbigem Format übergeben.
	//	TODO Robin: Route einzeichnen, Abfahrten Reihenfolge anzeigen, Dauer und Distanz anzeigen.

    }
}
