package de.dhbw.horb.routePlanner.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.graphData.Way;
import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

/**
 * Eine Klasse die mit hilfe der StAX cursor API die XML Datei parsed.
 * 
 * @author Sebastian
 */
public class GraphDataParser implements GraphDataConstants {

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

	private XMLStreamReader streamReader;

	private GraphDataParser(String xmlFile) throws FileNotFoundException,
			XMLStreamException {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		streamReader = factory.createXMLStreamReader(new FileReader(xmlFile));
	}

	public void everyNodeToGui(GraphicalUserInterface gui)
			throws XMLStreamException {
		while (streamReader.hasNext()) {
			if (nextStartElement()) {
				Node nextNode = getNode(null);
				if (nextNode != null)
					gui.addNode(nextNode);
			}
		}
	}

	public void everyWayToGui(GraphicalUserInterface gui)
			throws XMLStreamException {
		while (streamReader.hasNext()) {
			streamReader.next();
			if (nextStartElement()) {
				Way nextWay = getWay(null);
//				if (nextWay != null)
//					gui.addWay(nextWay);
			}
		}
	}

	private Node getNode(Long id) throws XMLStreamException {

		Double lat = null;
		Double lon = null;

		if (id == null) {
			if (streamReader.getLocalName() != CONST_NODE)
				return null;
			id = Long.valueOf(getAttributeValue(CONST_NODE_ID));
			lat = Double.valueOf(getAttributeValue(CONST_NODE_LATITUDE));
			lon = Double.valueOf(getAttributeValue(CONST_NODE_LONGITUDE));
		} else {
			while (streamReader.hasNext()) {
				if (streamReader.getLocalName() == CONST_NODE
						&& id == Long.valueOf(getAttributeValue(CONST_NODE_ID))) {
					lat = Double
							.valueOf(getAttributeValue(CONST_NODE_LATITUDE));
					lon = Double
							.valueOf(getAttributeValue(CONST_NODE_LONGITUDE));
					break;
				}
			}
		}
		streamReader.close();
		if (id != null && lat != null && lon != null)
			return (new Node(id, lat, lon));

		return null;
	}

	private Way getWay(Long id) throws NumberFormatException,
			XMLStreamException {
		if (id == null) {
			if (streamReader.getLocalName() != CONST_WAY)
				return null;
			id = Long.valueOf(getAttributeValue(CONST_WAY_ID));

		} else {

		}

		//
		// while(nextStartElement() && streamReader.)
		//
		// nextStartElement();
		// getNextCharacter(CONST_WAY_NODE, CONST_WAY_REF
		//
		// if(id != null) return (new Way(id));
		streamReader.close();
		return null;
	}

	private String getAttributeValue(String AttributeLocalName) {
		for (int x = 0; x < streamReader.getAttributeCount(); x++)
			if (streamReader.getAttributeLocalName(x).trim()
					.equals(AttributeLocalName))
				return streamReader.getAttributeValue(x);

		return null;
	}

	private String getNextCharacter(String localName,
			String attributeLocalName, String attributeValue)
			throws XMLStreamException {
		if (nextStartElement()
				&& streamReader.getLocalName().trim().equals(localName)
				&& getAttributeValue(attributeLocalName).trim().equals(
						attributeValue)
				&& streamReader.next() == XMLStreamReader.CHARACTERS)
			return streamReader.getText();

		return null;
	}

	private boolean nextStartElement() throws XMLStreamException {
		if (streamReader.hasNext()
				&& (streamReader.next() == XMLStreamReader.START_ELEMENT || nextStartElement()))
			return true;

		return false;
	}
}
