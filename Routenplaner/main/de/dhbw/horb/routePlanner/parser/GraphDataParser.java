package de.dhbw.horb.routePlanner.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.graphData.Way;
import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

/**
 * Eine Klasse die mit hilfe der StAX cursor API die XML Datei parsed.
 * 
 * @author Sebastian
 */
public class GraphDataParser {

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

		XMLInputFactory factory = XMLInputFactory.newInstance();
		graphSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileReader(xmlFile)));
	}

	public void everyWayToGui(final GraphicalUserInterface gui) throws XMLStreamException {
		
		ExecutorService executor = Executors.newFixedThreadPool(3);
		while (graphSR.hasNext()) {
			if (graphSR.nextStartElement() && graphSR.isWay()) {
				
				final Way nextWay = getWay(null);

				executor.submit(new Runnable() {
					
					@Override
					public void run() {
						
						while (nextWay != null && nextWay.hasEdge())
							gui.addEdge(nextWay.removeFirstEdge());
					}
				});				
			}
		}
	}

	public Node getNode(Long id) throws XMLStreamException {

		Double lat = null;
		Double lon = null;

		if (id == null) {
			if (!graphSR.isNode())
				return null;
			id = Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_ID));
			lat = Double.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_LATITUDE));
			lon = Double.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_LONGITUDE));
		} else {
			while (graphSR.nextStartElement()) {
				if (graphSR.isNode()
						&& id.equals(Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_NODE_ID)))) {
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
		Way newWay = null;

		if (id == null) {
			if (!graphSR.isWay())
				return null;

			id = Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_WAY_ID));
			if (id == null)
				return null;
			newWay = new Way(id);
			while (graphSR.nextStartElement() && graphSR.isNode()) {
				newWay.addNodeID(Long.valueOf(graphSR.getAttributeValue(GraphDataConstants.CONST_WAY_REF)));
			}

		} else {
			// TODO get way from id
		}

		graphSR.close();
		return (newWay);
	}

}
