package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import de.dhbw.horb.routePlanner.Constants;

public class GraphDataStreamReader extends StreamReaderDelegate {

	public GraphDataStreamReader(XMLStreamReader streamReader) throws XMLStreamException {
		super(streamReader);
	}

	public boolean isNode() {
		if (getLocalName().trim().equals(Constants.NODE) || getLocalName().trim().equals(Constants.WAY_NODE)
				|| getLocalName().trim().equals(Constants.NEW_NODE))
			return true;
		return false;
	}

	public boolean isWay() {
		if (getLocalName().trim().equals(Constants.WAY))
			return true;
		return false;
	}

	public boolean isTag() {
		if (getLocalName().trim().equals(Constants.WAY_TAG))
			return true;
		return false;
	}

	public boolean isRoute() {
		if (getLocalName().trim().equals(Constants.NEW_ROUTE))
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

	public String getAttributeKV(String inK) throws XMLStreamException {
		if (getLocalName().equals(Constants.NODE_TAG)) {
			String k = getAttributeValue("k");
			String v = getAttributeValue("v");
			if (k.equals(inK)) {
				return v;
			}
		}
		return null;
	}

}
