package de.dhbw.horb.routePlanner.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;

public class DomMapNodeParser {

	/**
	 * Node Map: 
	 * Key: Node ID 
	 * Value: Map mit keys: latitude, longitude, highway? = motorway_junction
	 */
	private Map<String, Map<String, String>> node;

	private DomMapNodeParser() {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			GraphDataStreamReader nodeSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
					Constants.XML_GRAPHDATA)));

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

}
