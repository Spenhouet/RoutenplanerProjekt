package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class DomStAXMapGraphDataNodesParser {

    /**
     * Node Map: Key: Node ID Value: Map mit keys: latitude, longitude, highway?
     * = motorway_junction
     */
    private Map<String, Map<String, String>> node;

    public DomStAXMapGraphDataNodesParser() {

	XMLInputFactory factory = XMLInputFactory.newInstance();
	try {
	    GraphDataStreamReader nodeSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
		    Constants.XML_GRAPHDATA)));

	    node = new HashMap<String, Map<String, String>>();

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

		} while (run);

		if (id == null || lat == null || lon == null)
		    continue;

		Map<String, String> pos = new HashMap<String, String>();

		pos.put(Constants.NODE_LATITUDE, lat);
		pos.put(Constants.NODE_LONGITUDE, lon);
		if (highway != null && name != null) {
		    pos.put(Constants.NODE_HIGHWAY, highway);
		    pos.put(Constants.NODE_NAME, name);
		}

		node.put(id, pos);
	    }

	} catch (FileNotFoundException | XMLStreamException e) {
	    e.printStackTrace();
	}
    }

    public Map<String, String> getNode(String id) {

	return node.get(id);
    }

    public void addWayIdToNode(String nodeID, String wayID) {
	if (nodeID == null || wayID == null)
	    return;

	if (node.containsKey(nodeID)) {
	    Map<String, String> nm = node.get(nodeID);
	    String ways = nm.get(Constants.WAY);
	    if (ways != null) {
		List<String> lways = SupportMethods.commaStrToStrList(ways);
		lways.add(wayID);
		nm.put(Constants.WAY, SupportMethods.strListToCommaStr(lways));
	    } else {
		nm.put(Constants.WAY, wayID);
	    }
	    node.put(nodeID, nm);
	} else {
	    Map<String, String> nm = new HashMap<String, String>();
	    nm.put(Constants.WAY, wayID);
	    node.put(nodeID, nm);
	}
    }

    public List<String> getWayListForNode(String nodeID) {
	return SupportMethods.commaStrToStrList(node.get(nodeID).get(Constants.WAY));
    }

    public Map<String, String> getMotorwayJunctionsMap() {

	Map<String, String> nodes = new HashMap<String, String>();

	for (String nodeID : node.keySet()) {
	    Map<String, String> nodeMap = node.get(nodeID);
	    String highway = nodeMap.get(Constants.NODE_HIGHWAY);
	    String name = nodeMap.get(Constants.NODE_NAME);
	    if (highway != null && highway.equals(Constants.NODE_MOTORWAY_JUNCTION))
		nodes.put(nodeID, name);
	}

	return nodes;
    }

    public Boolean isMotorwayJunction(String id) {
	Map<String, String> wm = node.get(id);
	String highway = wm.get(Constants.WAY_HIGHWAY);
	return (highway != null && highway.equals(Constants.NODE_MOTORWAY_JUNCTION));
    }
}
