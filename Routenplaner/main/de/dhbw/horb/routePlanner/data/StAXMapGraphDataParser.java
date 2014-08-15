package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

/**
 * Erzeugen von Maps als Cache aus den drei XML Dateien.
 * 
 */
public class StAXMapGraphDataParser {

    /**
     * 
     * @return Eine Map mit allen "nodes" aus graphData*.xml
     * 
     *         <ul>
     *         <li><b>Key</b>: Node ID
     *         <li><b>Value</b>: Eine weitere Map mit allen weiteren Infos:
     *         </ul>
     * 
     *         <ul>
     *         <li><b>Key</b>: Constants.NODE_LATITUDE <b>Value</b>: latitude
     *         <li><b>Key</b>: Constants.NODE_LONGITUDE <b>Value</b>: longitude
     *         <li><b>Key</b>: Constants.NODE_HIGHWAY <b>Value</b>: highway Art (motorway_junktion)
     *         <li><b>Key</b>: Constants.NODE_NAME <b>Value</b>: Name des Knotens
     *         </ul>
     * 
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public static Map<String, Map<String, String>> getNodeMap() throws FileNotFoundException, XMLStreamException {

	XMLInputFactory factory = XMLInputFactory.newInstance();
	GraphDataStreamReader nodeSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
	        XMLFileManager.getExtendedXMLFileName(Constants.XML_GRAPHDATA))));

	Map<String, Map<String, String>> nodes = new HashMap<String, Map<String, String>>();

	while (nodeSR.hasNext()) {
	    if (!nodeSR.nextStartElement())
		continue;

	    if (nodeSR.isWay())
		break;
	    else if (!nodeSR.isNode())
		continue;

	    String id = nodeSR.getAttributeValue(Constants.NODE_ID);
	    String lat = nodeSR.getAttributeValue(Constants.NODE_LATITUDE);
	    String lon = nodeSR.getAttributeValue(Constants.NODE_LONGITUDE);
	    String highway = null;
	    String name = null;
	    String ref = null;

	    Boolean run = true;
	    do {
		int nextType = nodeSR.next();
		run = !(nextType == XMLStreamConstants.END_ELEMENT && nodeSR.getLocalName().equals(Constants.NODE));

		if (nextType != XMLStreamConstants.START_ELEMENT)
		    continue;

		if (highway == null)
		    highway = nodeSR.getAttributeKV(Constants.NODE_HIGHWAY);

		if (name == null)
		    name = nodeSR.getAttributeKV(Constants.NODE_NAME);

		if (ref == null)
		    ref = nodeSR.getAttributeKV(Constants.NODE_REF);

	    } while (run);

	    if (id == null || lat == null || lon == null)
		continue;

	    Map<String, String> pos = new HashMap<String, String>();

	    pos.put(Constants.NODE_LATITUDE, lat);
	    pos.put(Constants.NODE_LONGITUDE, lon);
	    if (highway != null && name != null) {
		pos.put(Constants.NODE_HIGHWAY, highway);

		if (ref != null) {
		    name = name + " (" + ref + ")";
		}

		pos.put(Constants.NODE_NAME, name);
	    }

	    nodes.put(id, pos);
	}

	nodeSR.close();
	return nodes;
    }

    /**
     * 
     * @return Eine Map mit allen "ways" aus graphData*.xml
     * 
     *         <ul>
     *         <li><b>Key</b>: Way ID
     *         <li><b>Value</b>: Alle weiteren Information des Wegs in einer weiteren Map:
     *         </ul>
     * 
     *         <ul>
     *         <li><b>Key</b>: Constants.WAY_HIGHWAY <b>Value</b>: Weg Art (motorway / motorway_link)
     *         <li><b>Key</b>: Constants.WAY_MAXSPEED <b>Value</b>: Maximalgeschwindigkeit in km/h (null, none, z.B.:
     *         "120")
     *         <li><b>Key</b>: Constants.WAY_REF <b>Value</b>: Autobahnnummer (A8)
     *         <li><b>Key</b>: Constants.WAY_NODE <b>Value</b>: Kommastring mit allen Knoten IDs die auf dem Weg liegen
     *         in richtiger Reihenfolge.
     *         </ul>
     * 
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public static Map<String, Map<String, String>> getWayMap() throws FileNotFoundException, XMLStreamException {

	XMLInputFactory factory = XMLInputFactory.newInstance();

	GraphDataStreamReader waySR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
	        XMLFileManager.getExtendedXMLFileName(Constants.XML_GRAPHDATA))));

	Map<String, Map<String, String>> ways = new HashMap<String, Map<String, String>>();

	while (waySR.hasNext()) {
	    if (!waySR.nextStartElement())
		continue;

	    if (!waySR.isWay())
		continue;

	    String id = waySR.getAttributeValue(Constants.WAY_ID);

	    if (id == null)
		continue;

	    List<String> listND = new ArrayList<String>();
	    String highway = null;
	    String maxspeed = null;
	    String ref = null;

	    Boolean run = true;
	    do {
		int nextType = waySR.next();
		run = !(nextType == XMLStreamConstants.END_ELEMENT && waySR.getLocalName().equals(Constants.WAY));

		if (nextType != XMLStreamConstants.START_ELEMENT)
		    continue;

		String localName = waySR.getLocalName();
		if (localName.equals(Constants.WAY_NODE)) {
		    String refID = waySR.getAttributeValue(Constants.WAY_REF);
		    listND.add(refID);
		} else if (localName.equals(Constants.WAY_TAG)) {
		    String k = waySR.getAttributeValue("k");
		    String v = waySR.getAttributeValue("v");
		    if (k.equals(Constants.WAY_HIGHWAY)) {
			highway = v;
		    } else if (k.equals(Constants.WAY_MAXSPEED)) {
			maxspeed = v;
		    } else if (k.equals(Constants.WAY_REF)) {
			ref = v;
		    }
		}
	    } while (run);

	    Map<String, String> way = new HashMap<String, String>();

	    if (maxspeed == null || maxspeed.isEmpty() || !SupportMethods.isNumeric(maxspeed)) {
		if (highway != null && highway.equals(Constants.WAY_MOTORWAY_LINK)) {
		    maxspeed = "60";
		} else if (highway != null && highway.equals(Constants.WAY_MOTORWAY)) {
		    maxspeed = "120";
		} else {
		    maxspeed = "120";
		}
	    }
	    way.put(Constants.WAY_HIGHWAY, highway);
	    way.put(Constants.WAY_MAXSPEED, maxspeed);
	    way.put(Constants.WAY_REF, ref);
	    way.put(Constants.WAY_NODE, SupportMethods.strListToCommaStr(listND));

	    ways.put(id, way);
	}

	waySR.close();
	return ways;
    }

    /**
     * 
     * @return Eine Map mit allen Beziehungen zwischen Kreuzungsnamen und den Node IDs.
     *         <p>
     *         Von Kreuzungsnamen zu Knoten IDs
     *         <ul>
     *         <li><b>Key</b>: Kreuzungsname
     *         <li><b>Value</b>: Liste alle zugehörigen Knoten IDs
     *         </ul>
     *         <p>
     *         Von Knoten ID zu Kreuzungsname
     *         <ul>
     *         <li><b>Key</b>: Knoten ID
     *         <li><b>Value</b>: Liste mit einem Element: Dem zugehörigen Kreuzungsnamen
     *         </ul>
     * 
     * @throws XMLStreamException
     * @throws FileNotFoundException
     */
    public static Map<String, List<String>> getNodeXMLMap() throws XMLStreamException, FileNotFoundException {
	XMLInputFactory factory = XMLInputFactory.newInstance();
	GraphDataStreamReader nodeSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
	        XMLFileManager.getExtendedXMLFileName(Constants.XML_NODES))));

	Map<String, List<String>> nodeMap = new HashMap<String, List<String>>();

	while (nodeSR.hasNext()) {
	    if (nodeSR.nextStartElement() && nodeSR.isNode()) {

		String name = nodeSR.getAttributeValue(Constants.NEW_NODE_NAME);
		List<String> ids = SupportMethods.commaStrToStrList(nodeSR.getAttributeValue(Constants.NEW_NODE_IDS));

		if (name == null || ids == null || name.isEmpty() || ids.isEmpty()
		        || ids.size() > Constants.NEW_NODE_MAX_IDS)
		    continue;
		Map<String, List<String>> nm = new HashMap<String, List<String>>();
		nm.put(name, ids);
		nodeMap.putAll(nm);

		for (String id : ids) {
		    nodeMap.put(id, SupportMethods.commaStrToStrList(name));
		}
	    }
	}

	nodeSR.close();
	return nodeMap;
    }

    /**
     * 
     * @return Eine Map mit allen Infos zu einer Route.
     * 
     *         <ul>
     *         <li><b>Key</b>: Abfahrts Knoten ID
     *         <li><b>Value</b>: Alle weiteren Information der Route in einer Map.
     *         </ul>
     * 
     *         <ul>
     *         <li><b>Key</b>: Constants.NEW_ROUTE_DEPARTURENODEID <b>Value</b>: Abfahrt Knoten ID
     *         <li><b>Key</b>: Constants.NEW_ROUTE_DEPARTURENODENAME <b>Value</b>: Abfahrt Knoten Name
     *         <li><b>Key</b>: Constants.NEW_ROUTE_DESTINATIONNODEID <b>Value</b>: Ankunft Knoten ID
     *         <li><b>Key</b>: Constants.NEW_ROUTE_DESTINATIONNODENAME <b>Value</b>: Ankunft Knoten Name
     *         <li><b>Key</b>: Constants.NEW_ROUTE_NUMBER <b>Value</b>: Autobahnnummer (A8)
     *         <li><b>Key</b>: Constants.NEW_ROUTE_DISTANCE <b>Value</b>: Strecke in km
     *         <li><b>Key</b>: Constants.NEW_ROUTE_DURATION <b>Value</b>: Dauer in Millisekunden
     *         <li><b>Key</b>: Constants.NEW_ROUTE_WAYIDS <b>Value</b>: Alle auf der Strecke liegenden Weg IDs
     *         </ul>
     * 
     * @throws FileNotFoundException
     * @throws XMLStreamException
     */
    public static Map<String, List<Map<String, String>>> getRouteXMLMap() throws FileNotFoundException,
	                                                                 XMLStreamException {

	XMLInputFactory factory = XMLInputFactory.newInstance();

	GraphDataStreamReader routeSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
	        XMLFileManager.getExtendedXMLFileName(Constants.XML_ROUTES))));

	Map<String, List<Map<String, String>>> routes = new HashMap<String, List<Map<String, String>>>();

	while (routeSR.hasNext()) {
	    if (!routeSR.nextStartElement() || !routeSR.isRoute())
		continue;

	    Map<String, String> values = new HashMap<String, String>();

	    String departureNodeID = null;

	    for (int x = 0; x < routeSR.getAttributeCount(); x++) {

		String key = routeSR.getAttributeLocalName(x);
		String value = routeSR.getAttributeValue(x);
		if (key == null || value == null)
		    continue;

		if (key.equals(Constants.NEW_ROUTE_DEPARTURENODEID))
		    departureNodeID = value;

		values.put(key, value);
	    }

	    if (departureNodeID == null || values == null)
		continue;

	    if (routes.containsKey(departureNodeID)) {
		routes.get(departureNodeID).add(values);

	    } else {

		List<Map<String, String>> listRoute = new ArrayList<Map<String, String>>();
		listRoute.add(values);
		routes.put(departureNodeID, listRoute);
	    }
	}

	routeSR.close();
	return routes;
    }

}
