package de.dhbw.horb.routePlanner.parser;

import java.io.InputStream;

import javax.xml.stream.XMLStreamException;

import com.sun.org.apache.xerces.internal.impl.PropertyManager;
import com.sun.org.apache.xerces.internal.impl.XMLStreamReaderImpl;

public class GraphDataStreamReader extends XMLStreamReaderImpl {

	public GraphDataStreamReader(InputStream arg0, PropertyManager arg1) throws XMLStreamException {
		super(arg0, arg1);
	}

	public boolean isNode() {
		if (getLocalName().trim().equals(GraphDataConstants.CONST_NODE)
				|| getLocalName().trim().equals(GraphDataConstants.CONST_WAY_NODE))
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
