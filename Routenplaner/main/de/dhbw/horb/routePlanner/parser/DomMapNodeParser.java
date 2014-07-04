package de.dhbw.horb.routePlanner.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class DomMapNodeParser {

	/**
	 * Node Map: Key: Node ID Value: Map mit keys: latitude, longitude, highway?
	 * = motorway_junction
	 */
	private Map<String, Map<String, String>> node;

	public DomMapNodeParser() {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			GraphDataStreamReader nodeSR = new GraphDataStreamReader(
					factory.createXMLStreamReader(new FileInputStream(
							Constants.XML_GRAPHDATA)));

			node = new HashMap<String, Map<String,String>>();

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

	public void addWayIdToNode(String nodeID, String wayID) {
		if(nodeID == null || wayID == null) return;
		
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
	
	public List<String> getWayListForNode(String nodeID){
		return SupportMethods.commaStrToStrList(node.get(nodeID).get(Constants.WAY));
	}
}
