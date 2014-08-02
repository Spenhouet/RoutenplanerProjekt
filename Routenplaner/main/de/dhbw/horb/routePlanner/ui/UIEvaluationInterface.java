package de.dhbw.horb.routePlanner.ui;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;
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
		    evaluation.calculateWay(Constants.NEW_ROUTE_DISTANCE);
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
     * Constants.NEW_ROUTE_DURATION = Dauer in Sekunden
     * Constants.NEW_ROUTE_WAYIDS = Wege in Komma separiertem String
     * Constants.NEW_ROUTE_NUMBER = Autobahn Nummer
     * 
     */
    public static void printRoute(List<Map<String, String>> route) {
	DecimalFormat f = new DecimalFormat("#0.00");

	System.out.println(route);

	Double distance = 0.0;
	Double duration = 0.0;

	for (Map<String, String> way : route) {

	    String dist = way.get(Constants.NEW_ROUTE_DISTANCE);
	    String dur = way.get(Constants.NEW_ROUTE_DURATION);

	    if (dist == null || dur == null || !SupportMethods.isNumeric(dist) || !SupportMethods.isNumeric(dur))
		continue;

	    distance += Double.valueOf(dist);
	    duration += Double.valueOf(dur);
	}

	System.out.print("Von: " + route.get(0).get(Constants.NEW_ROUTE_DEPARTURENODENAME));
	System.out.println(" Nach: " + route.get(route.size() - 1).get(Constants.NEW_ROUTE_DESTINATIONNODENAME));
	System.out.println(f.format(distance) + " km");
	Double m = SupportMethods.secondsToMinutes(duration);
	int hours = (int) Math.floor(m / 60.0);
	int minutes = (int) Math.floor(m % 60.0);
	int seconds = (int) Math.floor(SupportMethods.minutesToSeconds(m % 1));
	System.out.printf("Dauer: %d Stunden %02d Minuten %02d Sekunden", hours, minutes, seconds);

	//	TODO Simon: Routen in selbigem Format übergeben.
	//	TODO Robin: Route einzeichnen, Abfahrten Reihenfolge anzeigen, Dauer und Distanz anzeigen.

    }
}
