package de.dhbw.horb.routePlanner.data;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import de.dhbw.horb.routePlanner.Constants;

/**
 * 
 * Erweiterung des StreamReader mit für oft gebrauchten Funktionen.
 */
public class GraphDataStreamReader extends StreamReaderDelegate {

    public GraphDataStreamReader(XMLStreamReader streamReader) throws XMLStreamException {
	super(streamReader);
    }

    /**
     * Überprüfung ob der Lokale Name ein Knoten ist.
     * 
     * @return Wahr wenn dies zutrifft und falsch wenn es nicht zutrifft.
     */
    public boolean isNode() {
	if (getLocalName().trim().equals(Constants.NODE) || getLocalName().trim().equals(Constants.WAY_NODE)
		|| getLocalName().trim().equals(Constants.NEW_NODE)) return true;
	return false;
    }

    /**
     * Überprüfung ob der Lokale Name ein Weg ist.
     * 
     * @return Wahr wenn dies zutrifft und falsch wenn es nicht zutrifft.
     */
    public boolean isWay() {
	if (getLocalName().trim().equals(Constants.WAY)) return true;
	return false;
    }

    /**
     * Überprüfung ob der Lokale Name eine Route ist.
     * 
     * @return Wahr wenn dies zutrifft und falsch wenn es nicht zutrifft.
     */
    public boolean isRoute() {
	if (getLocalName().trim().equals(Constants.NEW_ROUTE)) return true;
	return false;
    }

    /**
     * Sprint solange zum nächsten Element bis es ein Startelement oder das Ende der Datei ist.
     * 
     * @return Wahr wenn es erfolgreich an einem Startelement steht und falsch wenn es kein Startelement mehr gab.
     * @throws XMLStreamException
     */
    public boolean nextStartElement() throws XMLStreamException {
	while (hasNext())
	    if (next() == START_ELEMENT) return true;
	return false;
    }

    /**
     * Gibt den Attribut Wert für einen bestimmten Attribut Lokal Namen zurück wenn es diesen gibt.
     * 
     * @param AttributeLocalName
     *            Der Attribut Lokal Name für den der Attribut Wert abgefragt werden soll.
     * @return Der Attribut Wert als String.
     */
    public String getAttributeValue(String AttributeLocalName) {
	for (int x = 0; x < getAttributeCount(); x++)
	    if (getAttributeLocalName(x).trim().equals(AttributeLocalName)) return getAttributeValue(x);

	return null;
    }

    /**
     * Gibt für folgende Struktur: tag k="" v=""s den Wert für v für entsprechenden Schlüssel k zurück.
     * 
     * @param inK
     *            Schlüssel k
     * @return Wert v
     * @throws XMLStreamException
     */
    public String getAttributeKV(String inK) throws XMLStreamException {
	if (getLocalName().equals(Constants.NODE_TAG)) {
	    String k = getAttributeValue("k");
	    String v = getAttributeValue("v");
	    if (k.equals(inK)) return v;
	}
	return null;
    }
}
