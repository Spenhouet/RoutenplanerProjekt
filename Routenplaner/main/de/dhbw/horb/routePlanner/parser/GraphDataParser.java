package de.dhbw.horb.routePlanner.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import de.dhbw.horb.routePlanner.graphData.Edge;
import de.dhbw.horb.routePlanner.graphData.Node;
import de.dhbw.horb.routePlanner.ui.GraphicalUserInterface;

/**
 * Eine Klasse die mit hilfe der StAX cursor API die XML Datei parsed.
 * @author Sebastian
 */
public class GraphDataParser extends GraphDataConstants {
	
//	TODO bestimmten Knoten und Kante suchen
	
	
	/**
	 * Gibt ein Objekt der eigenen Klasse zurück. Dies ist die einzige Möglichkeit um auf den Parser zuzugreifen.
	 * @return Einen <code>GraphDataParser</code> wenn die XML-Datei gefunden wurde.
	 */
	public static GraphDataParser getGraphDataParser(){
		try {
			return new GraphDataParser();
		} catch (XMLStreamException | FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private XMLStreamReader streamReader;
	
	private GraphDataParser() throws FileNotFoundException, XMLStreamException{
		XMLInputFactory factory = XMLInputFactory.newInstance();
		streamReader = factory.createXMLStreamReader(new FileReader(CONST_XML_GRAPH_FILE)); 
	}
	
	
	public void everyNodeToGui(GraphicalUserInterface gui) throws XMLStreamException {
		while(streamReader.hasNext()){
			if(nextStartElement()){
				Node nextNode = getNode();
				if(nextNode != null) gui.addNode(nextNode);
			}
		}
	}
			
	public void everyEdgeToGui(GraphicalUserInterface gui) throws XMLStreamException {
		while(streamReader.hasNext()){
			if(nextStartElement()){
				Edge nextEdge = getEdge();
				if(nextEdge != null) gui.addEdge(nextEdge);
			}
		}
	}
		
	private Node getNode() throws XMLStreamException{
		if(streamReader.getLocalName() != CONST_NODE) return null;
						
		String id =  getAttributeValue(CONST_NODE_ID);
		Double lat = Double.valueOf(getNextCharacter(CONST_NODE_DATA, CONST_NODE_KEY, CONST_NODE_LATITUDE));
		Double lon = Double.valueOf(getNextCharacter(CONST_NODE_DATA, CONST_NODE_KEY, CONST_NODE_LONGITUDE));

		if(id != null && lat != null && lon != null) return (new Node(id, lat, lon));
		
		return null;
	}
	
	private Edge getEdge() throws NumberFormatException, XMLStreamException {		
		if(streamReader.getLocalName() != CONST_EDGE) return null;
								
		String source = getAttributeValue(CONST_EDGE_SOURCE);
		String target = getAttributeValue(CONST_EDGE_TARGET);
		String identifier = getNextCharacter(CONST_EDGE_DATA, CONST_EDGE_KEY, CONST_EDGE_IDENTIFIER);
		Double length = Double.valueOf(getNextCharacter(CONST_EDGE_DATA, CONST_EDGE_KEY, CONST_EDGE_LENGTH));
		
		if(source != null && target != null && identifier != null && length != null) return (new Edge(source, target, identifier, length));
		
		return null;
	}
	
	private String getAttributeValue(String AttributeLocalName){
		for (int x = 0; x < streamReader.getAttributeCount(); x++) 
			if(streamReader.getAttributeLocalName(x).trim().equals(AttributeLocalName)) return streamReader.getAttributeValue(x);
		
		return null;
	}
	
	private String getNextCharacter(String localName, String attributeLocalName, String attributeValue) throws XMLStreamException{
		if(nextStartElement() && streamReader.getLocalName().trim().equals(localName) && getAttributeValue(attributeLocalName).trim().equals(attributeValue) && streamReader.next() == XMLStreamReader.CHARACTERS)
			return streamReader.getText();
		
		return null;
	}
	
	private boolean nextStartElement() throws XMLStreamException{
		if(streamReader.hasNext() && (streamReader.next() == XMLStreamReader.START_ELEMENT || nextStartElement())) return true;
		
		return false;
	}	
}
	
	
	
