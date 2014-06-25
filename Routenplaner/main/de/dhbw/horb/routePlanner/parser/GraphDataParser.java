package de.dhbw.horb.routePlanner.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.graphData.Way;
import de.dhbw.horb.routePlanner.ui.Controller;
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

	private GraphDataParser(String xmlFile) throws FileNotFoundException,
			XMLStreamException {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		graphSR = new GraphDataStreamReader(
				factory.createXMLStreamReader(new FileInputStream(xmlFile)));
	}

	public void everyWayToGui(final GraphicalUserInterface gui) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Node start = null;
				Node end = null;

				try {
					while (graphSR.hasNext()) {

						if (graphSR.nextStartElement() && graphSR.isEdge()) {
							if (graphSR.nextStartElement() && graphSR.isNode()) {
								start = new Node(
										Long.valueOf(graphSR
												.getAttributeValue(GraphDataConstants.CONST_EDGE_ID)),
										Double.valueOf(graphSR
												.getAttributeValue(GraphDataConstants.CONST_EDGE_LATITUDE)),
										Double.valueOf(graphSR
												.getAttributeValue(GraphDataConstants.CONST_EDGE_LONGITUDE)));
							}
							if (graphSR.nextStartElement() && graphSR.isNode()) {
								end = new Node(
										Long.valueOf(graphSR
												.getAttributeValue(GraphDataConstants.CONST_EDGE_ID)),
										Double.valueOf(graphSR
												.getAttributeValue(GraphDataConstants.CONST_EDGE_LATITUDE)),
										Double.valueOf(graphSR
												.getAttributeValue(GraphDataConstants.CONST_EDGE_LONGITUDE)));
							}
							gui.addEdge(new Edge(start, end));
						}
					}
				} catch (XMLStreamException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public void writeEdgeXML() throws XMLStreamException {

		final long[] idCount = new long[1];

		XMLOutputFactory factory = XMLOutputFactory.newInstance();

		try {
			final XMLStreamWriter writer = factory
					.createXMLStreamWriter(new FileOutputStream(
							GraphDataConstants.CONST_XML_EDGE), "UTF-8");

			writer.writeStartDocument("UTF-8", "1.0");

			while (graphSR.hasNext()) {
				if (graphSR.nextStartElement() && graphSR.isWay()) {

					final Way nextWay = getWay(null);

					Controller.executor.getExecutor().submit(new Runnable() {

						@Override
						public void run() {

							while (nextWay != null && nextWay.hasEdge()) {

								try {
									idCount[0]++;
									Edge e = nextWay.removeFirstEdge();
									writer.writeStartElement(GraphDataConstants.CONST_EDGE);
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_ID,
											String.valueOf(idCount[0]));
									writer.writeStartElement(GraphDataConstants.CONST_EDGE_NODE);
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_ID,
											String.valueOf(e.getStartNode()
													.getID()));
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_LATITUDE,
											String.valueOf(e.getStartNode()
													.getLatitude()));
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_LONGITUDE,
											String.valueOf(e.getStartNode()
													.getLongitude()));
									writer.writeEndElement();
									writer.writeStartElement(GraphDataConstants.CONST_EDGE_NODE);
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_ID,
											String.valueOf(e.getEndNode()
													.getID()));
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_LATITUDE,
											String.valueOf(e.getEndNode()
													.getLatitude()));
									writer.writeAttribute(
											GraphDataConstants.CONST_EDGE_LONGITUDE,
											String.valueOf(e.getEndNode()
													.getLongitude()));
									writer.writeEndElement();
									writer.writeEndElement();

								} catch (XMLStreamException e) {
									e.printStackTrace();
								}

							}
						}
					});
				}
			}

			writer.writeEndDocument();
			writer.flush();
			writer.close();

		} catch (XMLStreamException | IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> containsName(String name) {

		List<String> names = new ArrayList<String>();
		Long id = null;

		try {
			while (graphSR.nextStartElement()) {
				String k = graphSR.getAttributeValue("k");
				if (graphSR.isTag()
						&& k.trim().equals(
								GraphDataConstants.CONST_NODE_TAG_NAME)) {
					String v = graphSR.getAttributeValue("v");
					if (!names.contains(v)
							&& v.toLowerCase().contains(name.toLowerCase()))
						names.add(v);
				}
			}

		} catch (NumberFormatException | XMLStreamException e) {
			e.printStackTrace();
		}

		return names;
	}

	public Node getNode(Long id) throws XMLStreamException {

		Double lat = null;
		Double lon = null;

		if (id == null) {
			if (!graphSR.isNode())
				return null;
			id = Long.valueOf(graphSR
					.getAttributeValue(GraphDataConstants.CONST_NODE_ID));
			lat = Double.valueOf(graphSR
					.getAttributeValue(GraphDataConstants.CONST_NODE_LATITUDE));
			lon = Double
					.valueOf(graphSR
							.getAttributeValue(GraphDataConstants.CONST_NODE_LONGITUDE));
		} else {
			while (graphSR.nextStartElement()) {
				if (graphSR.isNode()
						&& id.equals(Long.valueOf(graphSR
								.getAttributeValue(GraphDataConstants.CONST_NODE_ID)))) {
					lat = Double
							.valueOf(graphSR
									.getAttributeValue(GraphDataConstants.CONST_NODE_LATITUDE));
					lon = Double
							.valueOf(graphSR
									.getAttributeValue(GraphDataConstants.CONST_NODE_LONGITUDE));
					break;
				}
			}
		}
		graphSR.close();
		if (id != null && lat != null && lon != null)
			return (new Node(id, lat, lon));

		return null;
	}

	private Way getWay(Long id) throws NumberFormatException,
			XMLStreamException {
		Way newWay = null;

		if (id == null) {
			if (!graphSR.isWay())
				return null;

			id = Long.valueOf(graphSR
					.getAttributeValue(GraphDataConstants.CONST_WAY_ID));
			if (id == null)
				return null;
			newWay = new Way(id);
			while (graphSR.nextStartElement() && graphSR.isNode()) {
				newWay.addNode(Long.valueOf(graphSR
						.getAttributeValue(GraphDataConstants.CONST_WAY_REF)));
			}

		} else {
			// TODO get way from id
		}

		graphSR.close();
		return (newWay);
	}

}
