package de.dhbw.horb.routePlanner.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import de.dhbw.horb.routePlanner.Constants;

public class SettingsManager {

    /**
     * 
     * @param key Schlüssel mit dem ein Einstellungswert abgefragt werden kann.
     * @param defaultValue Sollte bei der Abfrage etwas schief gehen, wird der defaultValue Wert zurück gegeben.
     * @return Rückgabe der erfragten Value oder der defaultValue Wert.
     */
    public static String getValue(String key, String defaultValue) {
	if (key == null)
	    return defaultValue;

	try {
	    Map<String, String> settingsMap = getAllSettings();
	    if (!settingsMap.containsKey(key))
		return defaultValue;
	    return settingsMap.get(key);

	} catch (XMLStreamException e1) {
	    e1.printStackTrace();
	    return defaultValue;
	} catch (FileNotFoundException e) {
	    return defaultValue;
	}
    }

    /**
     * 
     * @param key Der Schlüssel unter dem der Einstellungswert gespeichert werden soll.
     * @param value Der Einstellungswert.
     * @return Wahr wenn das Speichern erfolgreich war. Falsch wenn es einen Fehler gab.
     */
    public static Boolean saveSetting(String key, String value) {
	if (key == null || value == null)
	    return false;

	Map<String, String> settingsMap = new HashMap<String, String>();
	try {
	    settingsMap = getAllSettings();
	} catch (FileNotFoundException e1) {
	} catch (XMLStreamException e1) {
	    e1.printStackTrace();
	    return false;
	}

	settingsMap.put(key, value);
	return saveSettings(settingsMap);
    }

    /**
     * 
     * @return Eine Map mit allen gespeicherten Einstellungen.
     * @throws XMLStreamException
     * @throws FileNotFoundException Sollte die Datei nicht vorhanden sein.
     */
    private static Map<String, String> getAllSettings() throws XMLStreamException, FileNotFoundException {

	XMLInputFactory factory = XMLInputFactory.newInstance();
	GraphDataStreamReader settingsSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
	        Constants.SETTINGS_XML)));

	Map<String, String> settingsMap = new HashMap<String, String>();

	while (settingsSR.nextStartElement()) {

	    if (settingsSR.getLocalName().trim().equals(Constants.SETTINGS_KVSET)) {

		String key = settingsSR.getAttributeLocalName(0);
		String value = settingsSR.getAttributeValue(0);

		if (key == null || value == null)
		    continue;

		settingsMap.put(key, value);
	    }
	}
	settingsSR.close();
	return settingsMap;
    }

    /**
     * Löscht einen Einstellungswert aus der Einstellungsdatei.
     * 
     * @param key Der Schlüssel für den Einstellungswert
     * @return Wahr wenn das löschen Erfolgreich war. Ansonsten falsch.
     */
    public static Boolean deleteSetting(String key) {
	Map<String, String> settingsMap = new HashMap<String, String>();
	try {
	    settingsMap = getAllSettings();
	} catch (FileNotFoundException e1) {
	} catch (XMLStreamException e1) {
	    e1.printStackTrace();
	    return false;
	}

	if (settingsMap == null || settingsMap.isEmpty() || !settingsMap.containsKey(key))
	    return false;

	settingsMap.remove(key);

	return saveSettings(settingsMap);
    }

    /**
     * Speichert die Settings als XML Datei.
     * 
     * @param settings Die zu speichernden Settings.
     * @return Wahr wenn das Speichern erfolgreich war. Ansonsten falsch.
     */
    private static Boolean saveSettings(Map<String, String> settings) {
	if (settings == null || settings.isEmpty())
	    return false;

	Element setEl = new Element(Constants.SETTINGS);
	Document settingsDoc = new Document(setEl);
	Element root = settingsDoc.getRootElement();

	for (Map.Entry<String, String> entry : settings.entrySet()) {
	    Element el = new Element(Constants.SETTINGS_KVSET);
	    el.setAttribute(new Attribute(entry.getKey(), entry.getValue()));
	    root.addContent(el);
	}

	XMLOutputter outp = new XMLOutputter();
	outp.setFormat(Format.getPrettyFormat());

	try {
	    outp.output(settingsDoc, new FileOutputStream(Constants.SETTINGS_XML));
	    return true;
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
    }

}
