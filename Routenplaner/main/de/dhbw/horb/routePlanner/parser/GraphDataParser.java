package de.dhbw.horb.routePlanner.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;

import de.dhbw.horb.routePlanner.graphData.GraphMap;
import de.dhbw.horb.routePlanner.graphData.Node;

public class GraphDataParser extends GraphDataConstants {
	
	private XMLStreamReader streamReader;
	
	
	public GraphDataParser() throws FileNotFoundException, XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		streamReader = factory.createXMLStreamReader(new FileReader(CONST_XML_GRAPH_FILE)); 

	}
	
//	TODO alle Kanten und bestimmte Kanten (von Knoten aus)?
	
	
	public void everyNode(GraphMap gMap) throws XMLStreamException {
		while(streamReader.hasNext()){
			if(nextStartElement()){

				gMap.addNode(getNode());//TODO
			}
		}
	}
	
//	TODO bestimmter Knoten suchen
	
	private Node getNode() throws XMLStreamException{
//		TODO schöner machen
		String id = null;
		Double lat = null;
		Double lon = null;
		
		if(streamReader.getLocalName() != CONST_NODE) return null;
				
		for (int x = 0; x < streamReader.getAttributeCount(); x++) {
			if(streamReader.getAttributeLocalName(x).trim().equals(CONST_NODE_ID)) id = streamReader.getAttributeValue(x);
		}	
				
		if(nextStartElement() && streamReader.getLocalName().trim().equals(CONST_NODE_DATA)){
			
			for (int x = 0; x < streamReader.getAttributeCount(); x++) {
				if(streamReader.getAttributeLocalName(x).trim().equals(CONST_NODE_KEY) && streamReader.getAttributeValue(x).trim().equals(CONST_NODE_LATITUDE)) {
					int event = streamReader.next();
					if(event == XMLStreamReader.CHARACTERS)	lat = Double.valueOf(streamReader.getText());
					break;
				}
			}	
		}
		
		if(nextStartElement() && streamReader.getLocalName().trim().equals(CONST_NODE_DATA)){
			
			for (int x = 0; x < streamReader.getAttributeCount(); x++) {
				if(streamReader.getAttributeLocalName(x).trim().equals(CONST_NODE_KEY) && streamReader.getAttributeValue(x).trim().equals(CONST_NODE_LONGITUDE)) {
					int event = streamReader.next();
					if(event == XMLStreamReader.CHARACTERS)	lon = Double.valueOf(streamReader.getText());
					break;
				}
			}	
		}

		if(id != null && lat != null && lon != null) return (new Node(id, lat, lon));
		
		return null;
	}
	
	private boolean nextStartElement() throws XMLStreamException{
		if(!streamReader.hasNext()) return false;
		int eventTyp = streamReader.next();
		if(eventTyp == XMLStreamReader.START_ELEMENT || nextStartElement()) return true;
		return false;
	}
	
//	-------------------------------------------------------------------
//	Only for test //TODO delete
	public final static String getEventTypeString(int eventType) {
	    switch (eventType) {
	        case XMLStreamReader.START_ELEMENT:
	            return "START_ELEMENT";

	        case XMLStreamReader.END_ELEMENT:
	            return "END_ELEMENT";

	        case XMLStreamReader.PROCESSING_INSTRUCTION:
	            return "PROCESSING_INSTRUCTION";

	        case XMLStreamReader.CHARACTERS:
	            return "CHARACTERS";

	        case XMLStreamReader.COMMENT:
	            return "COMMENT";

	        case XMLStreamReader.START_DOCUMENT:
	            return "START_DOCUMENT";

	        case XMLStreamReader.END_DOCUMENT:
	            return "END_DOCUMENT";

	        case XMLStreamReader.ENTITY_REFERENCE:
	            return "ENTITY_REFERENCE";

	        case XMLStreamReader.ATTRIBUTE:
	            return "ATTRIBUTE";

	        case XMLStreamReader.DTD:
	            return "DTD";

	        case XMLStreamReader.CDATA:
	            return "CDATA";

	        case XMLStreamReader.SPACE:
	            return "SPACE";
	    }
	    return "UNKNOWN_EVENT_TYPE , " + eventType;
	}
	
}
	
	
	
