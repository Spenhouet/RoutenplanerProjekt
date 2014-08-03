package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class StAXNodeParser {

    private GraphDataStreamReader graphSR;

    private StAXNodeParser(String xmlFile) throws FileNotFoundException, XMLStreamException {

	XMLInputFactory factory = XMLInputFactory.newInstance();
	graphSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(xmlFile)));
    }

    public static StAXNodeParser getStAXNodeParser() {
	try {
	    return new StAXNodeParser(Constants.XML_NODES);
	} catch (XMLStreamException | FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public static Map<String, List<String>> getNodeMap() {
	StAXNodeParser nodeParser = getStAXNodeParser();
	Map<String, List<String>> nodeMap = new HashMap<String, List<String>>();

	while (nodeParser.hasNext()) {
	    Map<String, List<String>> nm = nodeParser.getNextNode();
	    if (nm == null || nm.isEmpty())
		continue;
	    nodeMap.putAll(nm);

	    Map.Entry<String, List<String>> entry = nm.entrySet().iterator().next();

	    String name = entry.getKey();
	    List<String> ids = entry.getValue();
	    if (name == null || ids == null || name.isEmpty() || ids.isEmpty())
		continue;
	    for (String id : ids) {
		nodeMap.put(id, SupportMethods.commaStrToStrList(name));
	    }
	}

	nodeParser.close();
	return nodeMap;
    }

    public List<String> containsName(String name) {

	List<String> names = new ArrayList<String>();

	try {
	    while (graphSR.nextStartElement()) {
		if (!graphSR.isNode())
		    continue;
		String v = getName();

		if (v.toLowerCase().contains(name.toLowerCase()))
		    names.add(v);
	    }

	} catch (NumberFormatException | XMLStreamException e) {
	    e.printStackTrace();
	}

	if (names != null) {

	    names = SupportMethods.sortListCompairedToEquality(names, name);
	}

	close();
	return names;
    }

    public String getNameForID(String id) {
	if (id == null)
	    return null;

	while (hasNext()) {

	    try {
		if (!graphSR.nextStartElement() || !graphSR.isNode())
		    continue;

		List<String> nodes = getNodeIDs();

		if (nodes == null || !nodes.contains(id))
		    continue;

		close();
		return getName();
	    } catch (XMLStreamException e) {
		e.printStackTrace();
	    }
	}
	close();
	return null;
    }

    public List<String> getIDsForName(String name) {
	if (name == null)
	    return null;

	while (hasNext()) {

	    try {
		if (graphSR.nextStartElement() && graphSR.isNode() && name.equals(getName())) {
		    List<String> nodes = getNodeIDs();
		    if (nodes != null) {
			close();
			return nodes;
		    }
		}
	    } catch (XMLStreamException e) {
		e.printStackTrace();
	    }
	}
	close();
	return null;
    }

    public List<String> getNeighbours(String id) {
	while (hasNext()) {

	    List<String> nodes = getNextNodeIDs();
	    if (nodes != null && nodes.contains(id)) {
		nodes.remove(id);
		close();
		return nodes;
	    }
	}
	close();
	return null;
    }

    public String getName() {
	if (graphSR.isNode())
	    return graphSR.getAttributeValue(Constants.NEW_NODE_NAME);
	return null;
    }

    public List<String> getNextNodeIDs() {

	try {
	    if (graphSR.nextStartElement()) {
		return getNodeIDs();
	    }
	} catch (NumberFormatException | XMLStreamException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private List<String> getNodeIDs() {
	if (graphSR.isNode()) {

	    String commaIDs = graphSR.getAttributeValue(Constants.NEW_NODE_IDS);
	    List<String> ids = SupportMethods.commaStrToStrList(commaIDs);

	    if (ids.size() > Constants.NEW_NODE_MAX_IDS)
		return null;

	    return ids;
	}
	return null;
    }

    public Map<String, List<String>> getNextNode() {
	try {
	    if (graphSR.nextStartElement()) {
		return getNode();
	    }
	} catch (XMLStreamException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private Map<String, List<String>> getNode() {
	Map<String, List<String>> nm = new HashMap<String, List<String>>();
	final String name = getName();
	final List<String> ids = getNodeIDs();

	if (name == null || ids == null || name.isEmpty() || ids.isEmpty())
	    return null;

	nm.put(name, ids);
	return nm;
    }

    public Boolean hasNext() {

	try {
	    return graphSR.hasNext();
	} catch (XMLStreamException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public void close() {
	try {
	    graphSR.close();
	} catch (XMLStreamException e) {
	    e.printStackTrace();
	}
    }
}
