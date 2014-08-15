package de.dhbw.horb.routePlanner.data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import de.dhbw.horb.routePlanner.Constants;

/**
 * Verwalten der XML Dateien.
 */
public class XMLFileManager {

    private RandomAccessFile graphDataXMLRandomAccessFile = null;
    private RandomAccessFile routesXMLRandomAccessFile = null;
    private RandomAccessFile nodesXMLRandomAccessFile = null;
    private FileLock graphDataXMLLock = null;
    private FileLock routesXMLLock = null;
    private FileLock nodesXMLLock = null;

    public XMLFileManager() {

    }

    /**
     * Überprüfen ob die Datei existiert.
     * 
     * @param filePath Der Pfad zur zu überprüfenden Datei.
     * @return Wahr wenn sie vorhanden ist und falsch wenn nicht.
     */
    public static Boolean fileExists(String filePath) {
	if ((filePath == null) || filePath.isEmpty())
	    return false;

	File file = new File(filePath);
	if ((file != null) && file.exists() && !file.isDirectory())
	    return true;
	return false;
    }

    /**
     * Erweitert einen XML Dateinamen mit einer Länderbezeichnung entsprechend den Einstellungen.
     * 
     * @param filePath Der XML Dateinamen der erweitert werden soll.
     * @return Den erweiterten Dateinamen.
     */
    public static String getExtendedXMLFileName(String filePath) {
	if ((filePath == null) || filePath.isEmpty())
	    return null;

	return new StringBuilder(filePath).insert(filePath.indexOf(".xml"),
	        ("_" + SettingsManager.getValue(Constants.SETTINGS_COUNTRY, Constants.SETTINGS__DEFAULT_COUNTRY)))
	        .toString();
    }

    /**
     * Sperrt die route*.xml für den Zugriff.
     * 
     * @throws IOException
     */
    public void lockRoutesXML() throws IOException {
	this.routesXMLRandomAccessFile = new RandomAccessFile(new File(XMLFileManager
	        .getExtendedXMLFileName(Constants.XML_ROUTES)), "rw");
	this.routesXMLLock = this.routesXMLRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    /**
     * Sperrt die node*.xml für den Zugriff.
     * 
     * @throws IOException
     */
    public void lockNodesXML() throws IOException {
	this.nodesXMLRandomAccessFile = new RandomAccessFile(new File(XMLFileManager
	        .getExtendedXMLFileName(Constants.XML_NODES)), "rw");
	this.nodesXMLLock = this.nodesXMLRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    /**
     * Sperrt die graphData*.xml für den Zugriff.
     * 
     * @throws IOException
     */
    public void lockGraphDataXML() throws IOException {
	this.graphDataXMLRandomAccessFile = new RandomAccessFile(new File(XMLFileManager
	        .getExtendedXMLFileName(Constants.XML_GRAPHDATA)), "rw");
	this.graphDataXMLLock = this.graphDataXMLRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    /**
     * Sperrt die Route und Node XML Datei vor Zugriff.
     * 
     * @throws IOException
     */
    public void lockRoutesAndNodesXML() throws IOException {
	lockNodesXML();
	lockRoutesXML();
    }

    /**
     * Sperrt die alle XML Dateien vor Zugriff.
     * 
     * @throws IOException
     */
    public void lockAllXML() throws IOException {
	lockGraphDataXML();
	lockRoutesAndNodesXML();
    }

    /**
     * Gibt den Zugriff auf die Routes XML wieder frei.
     * 
     * @throws IOException
     */
    public void releaseRoutesXML() throws IOException {
	if (this.routesXMLLock != null)
	    this.routesXMLLock.release();
    }

    /**
     * Gibt den Zugriff auf die Nodes XML wieder frei.
     * 
     * @throws IOException
     */
    public void releaseNodesXML() throws IOException {
	if (this.nodesXMLLock != null)
	    this.nodesXMLLock.release();
    }

    /**
     * Gibt den Zugriff auf die GraphData XML wieder frei.
     * 
     * @throws IOException
     */
    public void releaseGraphDataXML() throws IOException {
	if (this.graphDataXMLLock != null)
	    this.graphDataXMLLock.release();

    }

    /**
     * Gibt den Zugriff auf die Routes und Nodes XML wieder frei.
     * 
     * @throws IOException
     */
    public void releaseRoutesAndNodesXML() throws IOException {
	releaseRoutesXML();
	releaseNodesXML();
    }

    /**
     * Gibt den Zugriff auf alle XML Dateien wieder frei.
     * 
     * @throws IOException
     */
    public void releaseAllXML() throws IOException {
	releaseGraphDataXML();
	releaseRoutesAndNodesXML();
    }

    @Override
    protected void finalize() throws Throwable {
	releaseAllXML();
	this.graphDataXMLRandomAccessFile.close();
	this.routesXMLRandomAccessFile.close();
	this.nodesXMLRandomAccessFile.close();
	super.finalize();
    }

}
