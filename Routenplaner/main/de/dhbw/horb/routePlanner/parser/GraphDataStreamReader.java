package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

public class GraphDataStreamReader extends StreamReaderDelegate {

	public GraphDataStreamReader(XMLStreamReader streamReader)
			throws XMLStreamException {
		super(streamReader);
	}
	
	public boolean isNode() {
		if (getLocalName().trim().equals(GraphDataConstants.CONST_NODE)
				|| getLocalName().trim().equals(
						GraphDataConstants.CONST_WAY_NODE))
			return true;
		return false;
	}

	public boolean isWay() {
		if (getLocalName().trim().equals(GraphDataConstants.CONST_WAY))
			return true;
		return false;
	}

	public boolean isTag() {
		if (getLocalName().trim().equals(GraphDataConstants.CONST_WAY_TAG))
			return true;
		return false;
	}

	public boolean nextStartElement() throws XMLStreamException {
		while (hasNext()) {
			if (next() == START_ELEMENT) {
				return true;
			}
		}
		return false;
	}

	public String getAttributeValue(String AttributeLocalName) {
		for (int x = 0; x < getAttributeCount(); x++)
			if (getAttributeLocalName(x).trim().equals(AttributeLocalName))
				return getAttributeValue(x);
		
		return null;
	}

}
