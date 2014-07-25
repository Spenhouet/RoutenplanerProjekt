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

public class OverpassDownloader {
    // TODO Methoden überarbeiten
    // TODO Fortschrittsanzeige hinzufügen
    // TODO Eintrag in GUI Menü

    public static void main(String[] args) throws Exception {

	String area = "Deutschland"; // TODO Land über GUI auswählbar
	String query = "[timeout:3600]; area[name=\"" + area + "\"]->.a;"
		+ "(way(area.a)[highway=\"motorway\"];>;"
		+ "way(area.a)[highway=\"motorway_link\"];>;);" + "out;";

	InputStream is = getDataViaOverpass(query);
	System.out.println(is.toString());
	copy(is, new File(Constants.XML_GRAPHDATA));
    }

    private static void copy(InputStream in, File file) {
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
    *
    * @param query the overpass query
    * @return the nodes in the formulated query
    * @throws IOException
    * @throws ParserConfigurationException
    * @throws SAXException
    */
    private static InputStream getDataViaOverpass(String query)
	    throws IOException, ParserConfigurationException, SAXException {
	String hostname = Constants.OVERPASS_API;

	URL osm = new URL(hostname);
	HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
	connection.setDoInput(true);
	connection.setDoOutput(true);
	connection.setRequestProperty("Content-Type",
		"application/x-www-form-urlencoded");
	connection.setReadTimeout(60 * 60 * 1000);

	DataOutputStream printout = new DataOutputStream(
		connection.getOutputStream());
	printout.writeBytes("data=" + URLEncoder.encode(query, "utf-8"));
	printout.flush();
	printout.close();

	return connection.getInputStream();
    }
}
