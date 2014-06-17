package de.dhbw.horb.routePlanner.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLStreamException;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;

import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.graphData.Way;
import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

/**
 * Eine Klasse die mit hilfe der StAX cursor API die XML Datei parsed.
 * 
 * @author Sebastian
 */
public class GraphDataParser {

	// TODO bestimmten Knoten und Kante suchen

	/**
	 * Gibt ein Objekt der eigenen Klasse zurück. Dies ist die einzige
	 * Möglichkeit um auf den Parser zuzugreifen.
	 * 
	 * @return Einen <code>GraphDataParser</code> wenn die XML-Datei gefunden
	 *         wurde.
	 */
	public static GraphDataParser getGraphDataParser(String xmlFile) {
		try {
			return new GraphDataParser(xmlFile);
		} catch (XMLStreamException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	private GraphDataStreamReader graphSR;

	private GraphDataParser(String xmlFile) throws FileNotFoundException, XMLStreamException {

		FileInputStream inStream = new FileInputStream(new File(xmlFile));
		graphSR = new GraphDataStreamReader(inStream, new PropertyManager(1));
	}

	public void everyNodeToGui(GraphicalUserInterface gui) throws XMLStreamException {
		while (graphSR.hasNext()) {
			if (graphSR.nextStartElement() && graphSR.isNode()) {
				Node nextNode = getNode(null);
				if (nextNode != null)
					gui.addNode(nextNode);
			}
		}
	}

	public void everyWayToGui(GraphicalUserInterface gui) throws XMLStreamException {
		while (graphSR.hasNext()) {
			graphSR.next();
			if (graphSR.isStartElement()) {
				// Way nextWay = getWay(null);
				// if (nextWay != null)
				// gui.addWay(nextWay);
				graphSR.nextStartElement();
			}
		}
	}

	public Node getNode(Long id) throws XMLStreamException {

		Double lat = null;
		Double lon = null;

		if (id == null) {
			if (graphSR.isNode() && graphSR.getLocalName() != GraphDataConstants.CONST_NODE)
				return null;
			id = Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_ID));
			lat = Double.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_LATITUDE));
			lon = Double.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_LONGITUDE));
		} else {
			while (graphSR.hasNext()) {
				if (graphSR.getLocalName() == GraphDataConstants.CONST_NODE
						&& id == Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_ID))) {
					lat = Double.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_LATITUDE));
					lon = Double.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_LONGITUDE));
					break;
				}
			}
		}
		graphSR.close();
		if (id != null && lat != null && lon != null)
			return (new Node(id, lat, lon));

		return null;
	}

	private Way getWay(Long id) throws NumberFormatException, XMLStreamException {
		// if (id == null) {
		// if (graphSR.getLocalName() != GraphDataConstants.CONST_WAY)
		// return null;
		// id =
		// Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_WAY_ID));
		//
		// } else {
		//
		// }

		//
		// while(nextStartElement() && streamReader.)
		//
		// nextStartElement();
		// getNextCharacter(CONST_WAY_NODE, CONST_WAY_REF
		//
		// if(id != null) return (new Way(id));
		graphSR.close();
		return null;
	}

}
