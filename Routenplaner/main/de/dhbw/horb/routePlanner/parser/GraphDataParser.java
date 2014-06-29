package de.dhbw.horb.routePlanner.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.graphData.Way;

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
		graphSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(xmlFile)));
	}
	
	
	public void test() {
		try {
			while(graphSR.hasNext()){
				
				if(graphSR.next() == graphSR.END_ELEMENT){
					
					String end = this.graphSR.getLocalName();
					
					
					
					
				}
				
				
				
			}
		} catch (XMLStreamException e) {
			// TODO Automatisch generierter Erfassungsblock
			e.printStackTrace();
		}
		
	}
	

	public List<String> containsName(String name) {

		List<String> names = new ArrayList<String>();

		try {
			while (graphSR.nextStartElement()) {
				String k = graphSR.getAttributeValue("k");
				if (graphSR.isTag() && k.trim().equals(Constants.NODE_TAG_NAME)) {
					String v = graphSR.getAttributeValue("v");
					if (!names.contains(v) && v.toLowerCase().contains(name.toLowerCase()))
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
			id = Long.valueOf(graphSR.getAttributeValue(Constants.NODE_ID));
			lat = Double.valueOf(graphSR.getAttributeValue(Constants.NODE_LATITUDE));
			lon = Double.valueOf(graphSR.getAttributeValue(Constants.NODE_LONGITUDE));
		} else {
			while (graphSR.nextStartElement()) {
				if (graphSR.isNode()
						&& id.equals(Long.valueOf(graphSR.getAttributeValue(Constants.NODE_ID)))) {
					lat = Double.valueOf(graphSR.getAttributeValue(Constants.NODE_LATITUDE));
					lon = Double.valueOf(graphSR.getAttributeValue(Constants.NODE_LONGITUDE));
					break;
				} else if (graphSR.isWay()){
					break;
				}
			}
		}
		graphSR.close();
		if (id != null && lat != null && lon != null)
			return (new Node(id, lat, lon));

		return null;
	}

	public Way getWay(Long id) throws NumberFormatException, XMLStreamException {
		Way newWay = null;

		if (id == null) {
			if (!graphSR.isWay())
				return null;

			id = Long.valueOf(graphSR.getAttributeValue(Constants.WAY_ID));
			if (id == null)
				return null;
			newWay = new Way(id);
			while (graphSR.nextStartElement() && graphSR.isNode()) {
				newWay.addNode(Long.valueOf(graphSR.getAttributeValue(Constants.WAY_REF)));
			}

		} else {
			// TODO get way from id
		}

		graphSR.close();
		return (newWay);
	}

}
