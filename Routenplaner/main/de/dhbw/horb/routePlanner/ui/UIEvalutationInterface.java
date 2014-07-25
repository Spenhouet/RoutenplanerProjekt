package de.dhbw.horb.routePlanner.ui;

import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.data.StAXNodeParser;

public class UIEvalutationInterface {

    public static void calculateRoute(final String startName,
	    final String endName) {

	new Thread(new Runnable() {

	    @Override
	    public void run() {
		//		DELETE only test
		System.out
			.println("Start: " + startName + "  Ende: " + endName);

		List<String> startIDs = StAXNodeParser.getStAXNodeParser()
			.getIDsForName(startName);
		List<String> endIDs = StAXNodeParser.getStAXNodeParser()
			.getIDsForName(endName);

		//		DELETE only test
		System.out.println("Start ID: " + startIDs + "  Ende ID: "
			+ endIDs);

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
