package de.dhbw.horb.routePlanner.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;

public class DomMapNodeParser {

	/**
	 * Node Map: Key: Node ID Value: Map mit keys: latitude, longitude, highway?
	 * = motorway_junction
	 */
	private Map<String, Map<String, String>> node;

	public DomMapNodeParser() {

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

				if (id == null || lat == null || lon == null)
					continue;

				Map<String, String> pos = new HashMap<String, String>();

				pos.put(Constants.NODE_LATITUDE, lat);
				pos.put(Constants.NODE_LONGITUDE, lon);

				node.put(id, pos);
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getNode(String id) {

		return node.get(id);
	}
}
