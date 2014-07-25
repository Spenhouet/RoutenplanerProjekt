package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class StAXNodeParser {

    public static StAXNodeParser getStAXNodeParser() {
	try {
	    return new StAXNodeParser(Constants.XML_NODES);
	} catch (XMLStreamException | FileNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    private GraphDataStreamReader graphSR;

    private StAXNodeParser(String xmlFile) throws FileNotFoundException,
	    XMLStreamException {

	XMLInputFactory factory = XMLInputFactory.newInstance();
	graphSR = new GraphDataStreamReader(
		factory.createXMLStreamReader(new FileInputStream(xmlFile)));
    }

    public List<String> containsName(String name) {

	List<String> names = new ArrayList<String>();

	try {
	    while (graphSR.nextStartElement()) {
		if (!graphSR.isNode())
		    continue;
		String v = graphSR.getAttributeValue(Constants.NEW_NODE_NAME);

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
		return graphSR.getAttributeValue(Constants.NEW_NODE_NAME);
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
		if (graphSR.nextStartElement()
			&& graphSR.isNode()
			&& name.equals(graphSR
				.getAttributeValue(Constants.NEW_NODE_NAME))) {
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
}
