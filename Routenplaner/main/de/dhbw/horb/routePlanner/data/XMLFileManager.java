package de.dhbw.horb.routePlanner.data;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import de.dhbw.horb.routePlanner.Constants;

public class XMLFileManager {

    private RandomAccessFile graphDataXMLraf = null;
    private RandomAccessFile routesXMLraf = null;
    private RandomAccessFile nodesXMLraf = null;
    private FileLock graphDataXMLLock = null;
    private FileLock routesXMLLock = null;
    private FileLock nodesXMLLock = null;

    public XMLFileManager() {

    }

    public static Boolean fileExists(String filePath) {
	if (filePath == null || filePath.isEmpty())
	    return false;

	File file = new File(filePath);
	if (file != null && file.exists() && !file.isDirectory()) {
	    return true;
	}
	return false;
    }

    public static String getExtendedXMLFileName(String filePath) {
	if (filePath == null || filePath.isEmpty())
	    return null;

	return new StringBuilder(filePath).insert(filePath.indexOf(".xml"),
		("_" + SettingsManager.getValue(Constants.SETTINGS_COUNTRY, Constants.SETTINGS__DEFAULT_COUNTRY)))
		.toString();
    }

    public void lockRoutesXML() throws IOException {
	routesXMLraf = new RandomAccessFile(new File(XMLFileManager.getExtendedXMLFileName(Constants.XML_ROUTES)), "rw");
	routesXMLLock = routesXMLraf.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    public void lockNodesXML() throws IOException {
	nodesXMLraf = new RandomAccessFile(new File(XMLFileManager.getExtendedXMLFileName(Constants.XML_NODES)), "rw");
	nodesXMLLock = nodesXMLraf.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    public void lockGraphDataXML() throws IOException {
	graphDataXMLraf = new RandomAccessFile(
		new File(XMLFileManager.getExtendedXMLFileName(Constants.XML_GRAPHDATA)), "rw");
	graphDataXMLLock = graphDataXMLraf.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    public FileDescriptor getRoutesFD() {
	FileDescriptor Result = null;
	try {
	    Result = routesXMLraf.getFD();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return Result;
    }

    public FileDescriptor getNodesFD() {
	FileDescriptor Result = null;
	try {
	    Result = nodesXMLraf.getFD();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return Result;
    }

    public FileDescriptor getGraphDataFD() {
	FileDescriptor Result = null;
	try {
	    Result = graphDataXMLraf.getFD();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return Result;
    }

    public void lockRoutesAndNodesXML() throws IOException {
	lockNodesXML();
	lockRoutesXML();
    }

    public void lockAllXML() throws IOException {
	lockGraphDataXML();
	lockRoutesAndNodesXML();
    }

    public void releaseRoutesXML() throws IOException {
	if (routesXMLLock != null) {
	    routesXMLLock.release();
	}
    }

    public void releaseNodesXML() throws IOException {
	if (nodesXMLLock != null) {
	    nodesXMLLock.release();
	}
    }

    public void releaseGraphDataXML() throws IOException {
	if (graphDataXMLLock != null) {
	    graphDataXMLLock.release();
	}

    }

    public void releaseRoutesAndNodesXML() throws IOException {
	releaseRoutesXML();
	releaseNodesXML();
    }

    public void releaseAllXML() throws IOException {
	releaseGraphDataXML();
	releaseRoutesAndNodesXML();
    }

    @Override
    protected void finalize() throws Throwable {
	releaseAllXML();
	graphDataXMLraf.close();
	routesXMLraf.close();
	nodesXMLraf.close();
	super.finalize();
    }

}
