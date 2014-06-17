package de.dhbw.horb.routePlanner.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public abstract class GraphDataStreamReader implements XMLStreamReader {

	public GraphDataStreamReader() {
		super();
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

	public void nextStartElement() throws XMLStreamException {
		do {
			next();
		} while (!isStartElement());
	}

}
