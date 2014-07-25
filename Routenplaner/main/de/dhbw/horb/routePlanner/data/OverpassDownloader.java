package de.dhbw.horb.routePlanner.data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class OverpassDownloader {

    private static final String OVERPASS_API = "http://www.overpass-api.de/api/interpreter";

    public static void main(String[] args) throws Exception {

	//	String query = "[timeout:3600]; area[name=\"Deutschland\"]->.a;"
	//		+ "(way(area.a)[highway=\"motorway\"];>;"
	//		+ "way(area.a)[highway=\"motorway_link\"];>;"
	//		+ "way(area.a)[highway=\"trunk\"];>;"
	//		+ "way(area.a)[highway=\"trunk_link\"];>;"
	//		+ "way(area.a)[highway=\"primary\"];>;"
	//		+ "way(area.a)[highway=\"primary_link\"];>;);" + "out;";

	String queryString = readFileAsString("xmlFile/anfrage.txt");
	InputStream is = getDataViaOverpass(queryString);
	System.out.println(is.toString());

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
	String hostname = OVERPASS_API;

	URL osm = new URL(hostname);
	HttpURLConnection connection = (HttpURLConnection) osm.openConnection();
	connection.setDoInput(true);
	connection.setDoOutput(true);
	connection.setRequestProperty("Content-Type",
		"application/x-www-form-urlencoded");
	connection.setReadTimeout(2 * 60 * 1000);//2 minute timeout

	DataOutputStream printout = new DataOutputStream(
		connection.getOutputStream());
	printout.writeBytes("data=" + URLEncoder.encode(query, "utf-8"));
	printout.flush();
	printout.close();

	//DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	//DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	//return docBuilder.parse(connection.getInputStream());
	return connection.getInputStream();
    }

    /**
     * 
     * @param filePath
     * @return
     * @throws java.io.IOException
     */
    private static String readFileAsString(String filePath)
	    throws java.io.IOException {
	StringBuffer fileData = new StringBuffer(1000);
	BufferedReader reader = new BufferedReader(new FileReader(filePath));
	char[] buf = new char[1024];
	int numRead = 0;
	while ((numRead = reader.read(buf)) != -1) {
	    String readData = String.valueOf(buf, 0, numRead);
	    fileData.append(readData);
	    buf = new char[1024];
	}
	reader.close();
	return fileData.toString();
    }

}
