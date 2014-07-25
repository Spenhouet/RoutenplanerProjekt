package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import de.dhbw.horb.routePlanner.Constants;
import de.dhbw.horb.routePlanner.SupportMethods;

public class DomStAXMapGraphDataWaysParser {
	private Map<String, Map<String, String>> ways;

	public DomStAXMapGraphDataWaysParser(DomStAXMapGraphDataNodesParser nodeMapDom) {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		try {
			GraphDataStreamReader waySR = new GraphDataStreamReader(
					factory.createXMLStreamReader(new FileInputStream(
							Constants.XML_GRAPHDATA)));

			ways = new HashMap<String, Map<String, String>>();

			while (waySR.hasNext()) {
				if (!waySR.nextStartElement())
					continue;

				if (!waySR.isWay())
					continue;

				String id = waySR.getAttributeValue(Constants.WAY_ID);
				
				if (id == null)
					continue;
				
				List<String> listND = new ArrayList<String>();
				String highway = null;
				String maxspeed = null;
				String ref = null;
				
				Boolean run = true;
				do{
					int nextType = waySR.next();
					run =!(nextType == XMLStreamConstants.END_ELEMENT && waySR.getLocalName().equals(Constants.WAY));
					
					if(nextType != XMLStreamConstants.START_ELEMENT) continue;
					
					String localName = waySR.getLocalName();
					if(localName.equals(Constants.WAY_NODE)){
						String refID = waySR.getAttributeValue(Constants.WAY_REF);
						listND.add(refID);
					} else if (localName.equals(Constants.WAY_TAG)){
						String k = waySR.getAttributeValue("k");
						String v = waySR.getAttributeValue("v");
						if(k.equals(Constants.WAY_HIGHWAY)){
							highway = v;
						} else if (k.equals(Constants.WAY_MAXSPEED)){
							maxspeed = v;
						} else if (k.equals(Constants.WAY_REF)){
							ref = v;
						}
					}
				} while(run);
				
				for(int i=0; i<(listND.size()-1); i++)
					nodeMapDom.addWayIdToNode(listND.get(i), id);
				
				Map<String, String> way = new HashMap<String, String>();
				
				way.put(Constants.WAY_HIGHWAY, highway);
				way.put(Constants.WAY_MAXSPEED, maxspeed);
				way.put(Constants.WAY_REF, ref);
				way.put(Constants.WAY_NODE, SupportMethods.strListToCommaStr(listND));
				
				ways.put(id, way);
			}

		} catch (FileNotFoundException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getWay(String id) {

		return ways.get(id);
	}
	
	public Boolean isLink(String id){
		Map<String, String> wm = ways.get(id);
		return wm.get(Constants.WAY_HIGHWAY).equals(Constants.WAY_MOTORWAY_LINK);
	}
	
	public String getNextNodeID(String wayID, String nodeID){
		Map<String, String> wm = ways.get(wayID);
		
		List<String> nodes = SupportMethods.commaStrToStrList(wm.get(Constants.WAY_NODE));
		return nodes.get(nodes.indexOf(nodeID)+1);
	}
}
