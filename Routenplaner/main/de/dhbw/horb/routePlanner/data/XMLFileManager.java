package de.dhbw.horb.routePlanner.data;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import de.dhbw.horb.routePlanner.Constants;

public class XMLFileManager {

    private RandomAccessFile graphDataXMLRandomAccessFile = null;
    private RandomAccessFile routesXMLRandomAccessFile = null;
    private RandomAccessFile nodesXMLRandomAccessFile = null;
    private FileLock graphDataXMLLock = null;
    private FileLock routesXMLLock = null;
    private FileLock nodesXMLLock = null;

    public XMLFileManager() {

    }

    public static Boolean fileExists(String filePath) {
	if ((filePath == null) || filePath.isEmpty()) return false;

	File file = new File(filePath);
	if ((file != null) && file.exists() && !file.isDirectory()) return true;
	return false;
    }

    public static String getExtendedXMLFileName(String filePath) {
	if ((filePath == null) || filePath.isEmpty()) return null;

	return new StringBuilder(filePath).insert(filePath.indexOf(".xml"),
		("_" + SettingsManager.getValue(Constants.SETTINGS_COUNTRY, Constants.SETTINGS__DEFAULT_COUNTRY)))
		.toString();
    }

    public void lockRoutesXML() throws IOException {
	this.routesXMLRandomAccessFile = new RandomAccessFile(new File(
		XMLFileManager.getExtendedXMLFileName(Constants.XML_ROUTES)), "rw");
	this.routesXMLLock = this.routesXMLRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    public void lockNodesXML() throws IOException {
	this.nodesXMLRandomAccessFile = new RandomAccessFile(new File(
		XMLFileManager.getExtendedXMLFileName(Constants.XML_NODES)), "rw");
	this.nodesXMLLock = this.nodesXMLRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    public void lockGraphDataXML() throws IOException {
	this.graphDataXMLRandomAccessFile = new RandomAccessFile(new File(
		XMLFileManager.getExtendedXMLFileName(Constants.XML_GRAPHDATA)), "rw");
	this.graphDataXMLLock = this.graphDataXMLRandomAccessFile.getChannel().lock(0, Long.MAX_VALUE, true);
    }

    public FileDescriptor getRoutesFD() {
	FileDescriptor Result = null;
	try {
	    Result = this.routesXMLRandomAccessFile.getFD();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return Result;
    }

    public FileDescriptor getNodesFD() {
	FileDescriptor Result = null;
	try {
	    Result = this.nodesXMLRandomAccessFile.getFD();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return Result;
    }

    public FileDescriptor getGraphDataFD() {
	FileDescriptor Result = null;
	try {
	    Result = this.graphDataXMLRandomAccessFile.getFD();
	} catch (IOException e) {
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
	if (this.routesXMLLock != null) this.routesXMLLock.release();
    }

    public void releaseNodesXML() throws IOException {
	if (this.nodesXMLLock != null) this.nodesXMLLock.release();
    }

    public void releaseGraphDataXML() throws IOException {
	if (this.graphDataXMLLock != null) this.graphDataXMLLock.release();

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
	this.graphDataXMLRandomAccessFile.close();
	this.routesXMLRandomAccessFile.close();
	this.nodesXMLRandomAccessFile.close();
	super.finalize();
    }

}
