package de.dhbw.horb.routePlanner.data;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.dhbw.horb.routePlanner.Constants;

/**
 * Herunterladen und Overpass XML Daten mit einer definierten Abfrage.
 */
public class OverpassDownloader {

    /**
     * Autobahn XML Daten von einem bestimmten Gebiet abfragen.
     * 
     * @param area Das Gebiet z.B.: Deutschland
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void downloadGraphData(String area) throws IOException, ParserConfigurationException, SAXException {

	String query = "[timeout:3600]; area[name=\"" + area + "\"]->.a; (way(area.a)[highway=\"motorway\"];>;"
	        + "way(area.a)[highway=\"motorway_link\"];>;); out;";

	saveInputStreamToFile(getDataFromOverpass(query), new File(XMLFileManager
	        .getExtendedXMLFileName(Constants.XML_GRAPHDATA)));
    }

    /**
     * Input Stream von Overpass in XML Datei speichern.
     * 
     * @param in Daten Stream
     * @param file Datei in die gespeichert werden soll.
     */
    private void saveInputStreamToFile(InputStream in, File file) {
	try {
	    OutputStream out = new FileOutputStream(file);
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	    out.close();
	    in.close();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * @param Datenverbindung mit Overpass aufbauen und Abfrage starten.
     * @return Ergebnis der Anfrage als XML Daten.
     * @throws IOException: Internet Abfrage fehlgeschlagen. Eventuell kein Internet.
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private InputStream getDataFromOverpass(String query) throws IOException, ParserConfigurationException,
	                                                 SAXException {

	URL overpass = new URL(Constants.OVERPASS_API);
	HttpURLConnection connection = (HttpURLConnection) overpass.openConnection();
	connection.setDoInput(true);
	connection.setDoOutput(true);
	connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	connection.setReadTimeout(60 * 60 * 1000);

	DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
	printout.writeBytes("data=" + URLEncoder.encode(query, "utf-8"));
	printout.flush();
	printout.close();

	return connection.getInputStream();
    }
}
