package de.dhbw.horb.routePlanner.test.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.junit.Assert;

import de.dhbw.horb.routePlanner.data.GraphDataStreamReader;

public class UTGraphDataStreamReader extends TestCase {

    private GraphDataStreamReader testSR;

    @Override
    protected void setUp() throws FileNotFoundException, XMLStreamException {
	XMLInputFactory factory = XMLInputFactory.newInstance();
	this.testSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
	        "test/de/dhbw/horb/routePlanner/test/testFiles/testGraphDataStreamReader.xml")));
    }

    @Override
    protected void tearDown() {
    }

    public void testNextStartElement() throws XMLStreamException {
	int count = 0;
	while (testSR.nextStartElement())
	    count++;
	Assert.assertEquals(count, 27);
    }

    public void testIsNode() throws XMLStreamException {
	Boolean hasNode = false;
	while (testSR.nextStartElement())
	    if (testSR.isNode())
		hasNode = true;
	Assert.assertTrue(hasNode);
    }

    public void testIsWay() throws XMLStreamException {
	Boolean hasWay = false;
	while (testSR.nextStartElement())
	    if (testSR.isWay())
		hasWay = true;
	Assert.assertTrue(hasWay);
    }

    public void testIsRoute() throws XMLStreamException {
	Boolean hasRoute = false;
	while (testSR.nextStartElement())
	    if (testSR.isRoute())
		hasRoute = true;
	Assert.assertTrue(hasRoute);
    }

    public void testGetAttributeValue() throws XMLStreamException {
	while (testSR.nextStartElement() && !testSR.isNode());
	Assert.assertEquals(testSR.getAttributeValue("id"), "122317");
	Assert.assertEquals(testSR.getAttributeValue("lat"), "53.5282681");
	Assert.assertEquals(testSR.getAttributeValue("lon"), "10.0232293");
    }

    public void testGetAttributeKV() throws XMLStreamException {
	while (testSR.nextStartElement() && !testSR.isNode());
	testSR.nextStartElement();
	Assert.assertEquals(testSR.getAttributeKV("TMC:cid_58:tabcd_1:Class"), "Point");
    }
}
