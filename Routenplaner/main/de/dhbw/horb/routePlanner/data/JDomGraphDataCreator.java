package de.dhbw.horb.routePlanner.data;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class JDomGraphDataCreator {

    private static Map<String, Map<String, String>> nodes;
    private static Map<String, Map<String, String>> ways;
    private static Map<String, List<String>> nodeWaysLink;
    private static XMLOutputter outp;
    private static Element el;
    private static Document xmlDoc;
    private static Element rootNewEl;

    public static void main(String[] args) {
	try {
	    createRouteXML();
	} catch (XMLStreamException | IOException e) {
	    // TODO Automatisch generierter Erfassungsblock
	    e.printStackTrace();
	}
    }

    public static void createNodeXML() throws XMLStreamException, IOException {
	el = new Element(Constants.NEW_NODE_S);
	xmlDoc = new Document(el);
	rootNewEl = xmlDoc.getRootElement();

	Map<String, List<String>> nm = new HashMap<String, List<String>>();

	for (Map.Entry<String, Map<String, String>> entry : StAXMapGraphDataParser.getNodeMap().entrySet()) {
	    String nodeID = entry.getKey();
	    Map<String, String> nodeMap = entry.getValue();

	    String highway = nodeMap.get(Constants.NODE_HIGHWAY);
	    String name = nodeMap.get(Constants.NODE_NAME);

	    if (name != null && highway != null && highway.equals(Constants.NODE_MOTORWAY_JUNCTION)) {
		if (nm.containsKey(name)) {
		    nm.get(name).add(nodeID);
		} else {
		    List<String> listIDs = new ArrayList<String>();
		    listIDs.add(nodeID);
		    nm.put(name, listIDs);
		}
	    }
	}

	for (Map.Entry<String, List<String>> entry : nm.entrySet()) {
	    Element newNode = new Element(Constants.NODE);
	    newNode.setAttribute(new Attribute(Constants.NEW_NODE_NAME, entry.getKey()));
	    newNode.setAttribute(new Attribute(Constants.NEW_NODE_IDS, SupportMethods.strListToCommaStr(entry
		    .getValue())));
	    rootNewEl.addContent(newNode);
	}

	outp = new XMLOutputter();
	outp.setFormat(Format.getPrettyFormat());
	outp.output(xmlDoc, new FileOutputStream(XMLFileManager.getExtendedXMLFileName(Constants.XML_NODES)));
	outp = null;
	rootNewEl = null;
	xmlDoc = null;
	el = null;
    }

    public static void createRouteXML() throws XMLStreamException, IOException {
	el = new Element(Constants.NEW_ROUTE_S);
	xmlDoc = new Document(el);
	rootNewEl = xmlDoc.getRootElement();

	nodes = StAXMapGraphDataParser.getNodeMap();
	ways = StAXMapGraphDataParser.getWayMap();
	buildUpCache();

	Map<String, List<String>> nodesXML = StAXMapGraphDataParser.getNodeXMLMap();
	for (Map.Entry<String, List<String>> entry : nodesXML.entrySet()) {
	    for (String nodeID : entry.getValue()) {
		if (nodeID == null || !SupportMethods.isNumeric(nodeID))
		    continue;
		List<String> waysCont = getWaysContainingID(nodeID);
		if (waysCont == null || waysCont.isEmpty())
		    continue;
		for (String wayID : waysCont) {
		    List<HashMap<String, String>> route = new ArrayList<HashMap<String, String>>();
		    HashSet<String> idHistory = new HashSet<String>();
		    HashMap<String, String> allInfos = getAllInfos(nodeID, wayID);
		    if (allInfos == null)
			continue;

		    route.add(allInfos);
		    idHistory.add(nodeID);
		    recursRoute(route, idHistory);
		}
	    }
	}

	outp = new XMLOutputter();
	outp.setFormat(Format.getPrettyFormat());
	outp.output(xmlDoc, new FileOutputStream(XMLFileManager.getExtendedXMLFileName(Constants.XML_ROUTES)));
	outp = null;
	rootNewEl = null;
	xmlDoc = null;
	el = null;
    }

    private static void buildUpCache() {
	//IMPROVE Sebastian: Performance verbessern

	nodeWaysLink = new HashMap<String, List<String>>();

	for (Map.Entry<String, Map<String, String>> entry : ways.entrySet()) {
	    String wayID = entry.getKey();
	    Map<String, String> wayInfos = entry.getValue();

	    if (entry == null || wayID == null || wayInfos == null)
		continue;

	    List<String> nds = SupportMethods.commaStrToStrList(wayInfos.get(Constants.WAY_NODE));
	    for (String nodeID : nds) {
		if (nodeID == null || (nds.indexOf(nodeID) == (nds.size() - 1)))
		    continue;

		if (nodeWaysLink.containsKey(nodeID)) {
		    nodeWaysLink.get(nodeID).add(wayID);
		} else {
		    List<String> listIDs = new ArrayList<String>();
		    listIDs.add(wayID);
		    nodeWaysLink.put(nodeID, listIDs);
		}
	    }
	}
    }

    private static void recursRoute(List<HashMap<String, String>> route, HashSet<String> idHistory)
	    throws FileNotFoundException, IOException {
	// INVESTIGATE Sebastian: Fehlende Strecken (Löcher?)
	if (route == null || idHistory == null)
	    return;

	HashMap<String, String> nextNode = getNextNode(route.get(route.size() - 1));
	if (nextNode == null || nextNode.isEmpty())
	    return;

	String nextNodeID = nextNode.get(Constants.NODE_ID_EX);

	if (idHistory.contains(nextNodeID))
	    return;

	if (Constants.NODE_MOTORWAY_JUNCTION.equals(nextNode.get(Constants.NODE_HIGHWAY_EX))) {
	    String departureNodeID = route.get(0).get(Constants.NODE_ID_EX);
	    String destinationNodeID = nextNodeID;

	    if (departureNodeID == null || destinationNodeID == null || departureNodeID.equals(destinationNodeID))
		return;

	    route.add(nextNode);
	    saveRoute(route);
	    route.remove(nextNode);
	    return;
	}

	List<String> waysContain = getWaysContainingID(nextNodeID);

	if (waysContain == null || waysContain.isEmpty()) {
	    //	    System.err.println("Kein Weg bekannt für: " + nextNodeID); INVESTIGATE Sebastian: Ist das überhaupt ein Fehler?
	    return;
	}
	for (String wayID : waysContain) {
	    HashMap<String, String> nextNodeAllInfos = getAllInfos(nextNodeID, wayID);
	    if (nextNodeAllInfos == null)
		continue;
	    route.add(nextNodeAllInfos);
	    idHistory.add(nextNodeID);
	    recursRoute(route, idHistory);
	    idHistory.remove(nextNodeID);
	    route.remove(nextNodeAllInfos);
	}
    }

    private static HashMap<String, String> getNextNode(HashMap<String, String> route) {
	String lastNodeID = route.get(Constants.NODE_ID_EX);
	List<String> nodeIDs = SupportMethods.commaStrToStrList(route.get(Constants.WAY_NODE));
	String nextNodeID = nodeIDs.get(nodeIDs.indexOf(lastNodeID) + 1);

	return getAllInfos(nextNodeID, route.get(Constants.WAY_ID_EX));
    }

    private static HashMap<String, String> getAllInfos(String nodeID, String wayID) {
	if (nodeID == null || wayID == null || nodeID.isEmpty() || wayID.isEmpty())
	    return null;
	Map<String, String> nodeInf = nodes.get(nodeID);
	Map<String, String> wayInf = ways.get(wayID);

	if (nodeInf == null || wayInf == null || nodeInf.isEmpty() || wayInf.isEmpty())
	    return null;

	HashMap<String, String> allInfos = new HashMap<String, String>();
	allInfos.putAll(nodeInf);
	allInfos.putAll(wayInf);
	allInfos.put(Constants.NODE_ID_EX, nodeID);
	allInfos.put(Constants.WAY_ID_EX, wayID);
	allInfos.remove(Constants.NODE_HIGHWAY);
	allInfos.put(Constants.NODE_HIGHWAY_EX, nodeInf.get(Constants.NODE_HIGHWAY));
	allInfos.put(Constants.WAY_HIGHWAY_EX, wayInf.get(Constants.WAY_HIGHWAY));
	return allInfos;
    }

    private static List<String> getWaysContainingID(String nodeID) {
	if (nodeID == null)
	    return null;
	return nodeWaysLink.get(nodeID);
    }

    private static void saveRoute(List<HashMap<String, String>> route) throws FileNotFoundException, IOException {
	if (route == null || route.isEmpty())
	    return;

	String departureNodeName = route.get(0).get(Constants.NODE_NAME);
	String departureNodeID = route.get(0).get(Constants.NODE_ID_EX);
	String destinationNodeName = route.get(route.size() - 1).get(Constants.NODE_NAME);
	String destinationNodeID = route.get(route.size() - 1).get(Constants.NODE_ID_EX);
	if (departureNodeName == null || departureNodeID == null || destinationNodeName == null
		|| destinationNodeID == null)
	    return;
	Double distance = 0.0;
	Long duration = 0L;
	String ref = null;
	List<String> wayIDs = new ArrayList<String>();

	Map<String, String> lastNode = null;
	for (Map<String, String> node : route) {
	    if (node == null)
		continue;

	    if (lastNode != null) {
		Double lat1 = Double.valueOf(lastNode.get(Constants.NODE_LATITUDE));
		Double lon1 = Double.valueOf(lastNode.get(Constants.NODE_LONGITUDE));
		Double lat2 = Double.valueOf(node.get(Constants.NODE_LATITUDE));
		Double lon2 = Double.valueOf(node.get(Constants.NODE_LONGITUDE));

		if (lat1 == null || lon1 == null || lat2 == null || lon2 == null)
		    continue;
		Double dist = SupportMethods.fromLatLonToDistanceInKM(lat1, lon1, lat2, lon2);
		if (dist == null || dist == 0.0)
		    continue;
		distance += dist;
		Integer speed = Integer.valueOf(lastNode.get(Constants.WAY_MAXSPEED));
		if (speed == null || speed == 0)
		    continue;
		Long dur = SupportMethods.fromDistanceAndSpeedToMilliseconds(dist, speed);
		if (dur == null || dur == 0L)
		    continue;
		duration += dur;
		if (ref == null) {
		    String r = lastNode.get(Constants.WAY_REF);
		    if (r != null && !r.isEmpty())
			ref = r;
		}
		String wayID = lastNode.get(Constants.WAY_ID_EX);
		if (wayID != null && !wayIDs.contains(wayID))
		    wayIDs.add(wayID);
		lastNode = node;
	    } else {
		lastNode = node;
	    }
	}

	Element newRoute = new Element(Constants.NEW_ROUTE);
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DEPARTURENODEID, departureNodeID));
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DEPARTURENODENAME, departureNodeName));
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DESTINATIONNODEID, destinationNodeID));
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DESTINATIONNODENAME, destinationNodeName));
	if (ref != null)
	    newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_NUMBER, ref));
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DISTANCE, distance.toString()));
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_DURATION, duration.toString()));
	newRoute.setAttribute(new Attribute(Constants.NEW_ROUTE_WAYIDS, SupportMethods.strListToCommaStr(wayIDs)));

	rootNewEl.addContent(newRoute);
    }
}
