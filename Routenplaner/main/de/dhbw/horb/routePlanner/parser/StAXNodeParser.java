package de.dhbw.horb.routePlanner.parser;

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

	private StAXNodeParser(String xmlFile) throws FileNotFoundException, XMLStreamException {

		XMLInputFactory factory = XMLInputFactory.newInstance();
		graphSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(xmlFile)));
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
		
		close();
		return names;
	}

	public List<String> getNextNodeIDs() {

		try {
			if (graphSR.nextStartElement() && graphSR.isNode()) {

				String commaIDs = graphSR.getAttributeValue(Constants.NEW_NODE_IDS);
				List<String> ids = SupportMethods.commaStrToStrList(commaIDs);

				if (ids.size() > Constants.NEW_NODE_MAX_IDS)
					return null;

				return ids;
			}
		} catch (NumberFormatException | XMLStreamException e) {
			e.printStackTrace();
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
	
	public void close(){
		try {
			graphSR.close();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	
	
	//TODO: Methode 2: ist id enthalten
	
	public List<String> getNeighbours(String id){
		while(hasNext()){
			
			List<String> nodes = getNextNodeIDs();
			if(nodes != null && nodes.contains(id)){
				nodes.remove(id);
				close();
				return nodes;
			}
		}
		close();
		return null;
	}
}
