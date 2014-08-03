package de.dhbw.horb.routePlanner.test.graphData;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.dhbw.horb.routePlanner.SupportMethods;
import de.dhbw.horb.routePlanner.data.DomStAXMapGraphDataNodesParser;
import de.dhbw.horb.routePlanner.data.DomStAXMapGraphDataWaysParser;
import de.dhbw.horb.routePlanner.data.Route;

/**
 * Test der Routen Klasse.
 * Verwendete Daten:
 * 	Start Knoten 	-> ID: 131118 	Breitengrad: 53.4249469  	Längengrad: 9.9278571
 * 	Ziel Knoten  	-> ID: 131123 	Breitengrad: 53.4393385 	Längengrad: 9.9219272
 * 	Weg  		-> ID: 67264596 Maximal Geschwindigkeit: 130 km/h	Nummer: A 45
 */
public class UTRoute {

    private DomStAXMapGraphDataNodesParser nodeMapDom;
    private DomStAXMapGraphDataWaysParser wayMapDom;
    private Route testRoute;
    private String departureNodeID = "131118";
    private String destinationNodeID = "131123";
    private String wayID = "67264596";

    @Before
    public void setUp() {
	nodeMapDom = new DomStAXMapGraphDataNodesParser();
	wayMapDom = new DomStAXMapGraphDataWaysParser(nodeMapDom);

	Assert.assertNull(testRoute);
	testRoute = new Route(departureNodeID, nodeMapDom, wayMapDom);
	Assert.assertNotNull(testRoute);
    }

    @Test
    public void addNode() {

	Assert.assertEquals(departureNodeID, testRoute.getDepartureNodeID());
	Assert.assertFalse(testRoute.hadRun());
	String oldDestID = testRoute.getDestinationNodeID();

	testRoute.addNode(destinationNodeID, wayID);

	String newDestID = testRoute.getDestinationNodeID();
	Assert.assertNotEquals(oldDestID, newDestID);
	Assert.assertEquals(destinationNodeID, newDestID);

	List<String> wayIDs = SupportMethods.commaStrToStrList(testRoute
		.getWayIDsAsCommaString());
	Assert.assertTrue(wayIDs.contains(wayID));

	Assert.assertEquals("A 45", testRoute.getNumber());
	Assert.assertEquals(1.648, testRoute.getDistance(), 0.001);
	Assert.assertEquals(SupportMethods.millisecondsToSeconds(SupportMethods
		.fromDistanceAndSpeedToMilliseconds(1.648, 130).doubleValue()),
		testRoute.getDurationInSeconds(), 0.99);
    }

    @Test
    public void runStatus() {
	Assert.assertFalse(testRoute.hadRun());
	testRoute.firstRun();
	Assert.assertTrue(testRoute.hadRun());
    }
}
