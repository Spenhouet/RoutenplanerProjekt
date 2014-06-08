package de.dhbw.horb.routePlanner.parser;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class GraphDataParser extends GraphDataConstants {
	
	private XMLStreamReader streamReader;
	
	public GraphDataParser(){
		XMLInputFactory factory = XMLInputFactory.newInstance();

		try {
		    streamReader = factory.createXMLStreamReader(new FileReader(CONST_XML_GRAPH_FILE)); 
		} catch (XMLStreamException | IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	public void search() {
		
		try {
			while(streamReader.hasNext()){
				
				int eventTyp = streamReader.next();
				if(eventTyp == XMLStreamReader.START_ELEMENT){
					//TODO: Nach Elementen Suchen
				}
				
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	
}
	
	
	
