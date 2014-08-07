package de.dhbw.horb.routePlanner.data;

import java.io.File;
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

    //    public static void main(String[] args) throws Exception {
    //
    //	XMLFileManager fm = new XMLFileManager();
    //
    //	System.out.println("Alle existieren: " + fm.allXMLExists());
    //
    //	System.out.println("Alle sind nicht gesperrt: " + !fm.allXMLLocked());
    //	fm.lockAllXML();
    //	System.out.println("Alle sind gesperrt: " + fm.allXMLLocked());
    //	fm.releaseAllXML();
    //	System.out.println("Alle sind nicht gesperrt: " + !fm.allXMLLocked());
    //    }

    public Boolean graphDataXMLExists() {
	File graphDataXML = new File(Constants.XML_GRAPHDATA);
	if (graphDataXML.exists() && !graphDataXML.isDirectory()) {
	    return true;
	}
	return false;
    }

    public Boolean routesXMLExists() {
	File routesXML = new File(Constants.XML_ROUTES);
	if (routesXML.exists() && !routesXML.isDirectory()) {
	    return true;
	}

	return false;
    }

    public Boolean nodesXMLExists() {
	File nodesXML = new File(Constants.XML_NODES);
	if (nodesXML.exists() && !nodesXML.isDirectory()) {
	    return true;
	}

	return false;
    }

    public Boolean routesAndNodeXMLExists() {

	return (routesXMLExists() && nodesXMLExists());
    }

    public Boolean allXMLExists() {

	return (graphDataXMLExists() && routesAndNodeXMLExists());
    }

    public void lockRoutesXML() throws IOException {
	routesXMLraf = new RandomAccessFile(new File(Constants.XML_ROUTES), "rw");
	routesXMLLock = routesXMLraf.getChannel().lock();
    }

    public void lockNodesXML() throws IOException {
	nodesXMLraf = new RandomAccessFile(new File(Constants.XML_NODES), "rw");
	nodesXMLLock = nodesXMLraf.getChannel().lock();
    }

    public void lockGraphDataXML() throws IOException {
	graphDataXMLraf = new RandomAccessFile(new File(Constants.XML_GRAPHDATA), "rw");
	graphDataXMLLock = graphDataXMLraf.getChannel().lock();
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

    public Boolean routesXMLLocked() {
	File routesXML = new File(Constants.XML_ROUTES);
	return !routesXML.canWrite();
    }

    public Boolean nodesXMLLocked() {
	File nodesXML = new File(Constants.XML_NODES);
	return !nodesXML.canWrite();
    }

    public Boolean graphDataXMLLocked() {
	File graphDataXML = new File(Constants.XML_GRAPHDATA);
	return !graphDataXML.canWrite();
    }

    public Boolean routesAndNodesXMLLocked() {
	return (routesXMLLocked() && nodesXMLLocked());
    }

    public Boolean allXMLLocked() {
	return (graphDataXMLLocked() && routesAndNodesXMLLocked());
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
