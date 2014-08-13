package de.dhbw.horb.routePlanner.test.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;
import de.dhbw.horb.routePlanner.data.GraphDataStreamReader;

public class UTGraphDataStreamReader extends TestCase {

    private GraphDataStreamReader routeSR;

    @Override
    protected void setUp() throws FileNotFoundException, XMLStreamException {
	XMLInputFactory factory = XMLInputFactory.newInstance();
	this.routeSR = new GraphDataStreamReader(factory.createXMLStreamReader(new FileInputStream(
		"test/de/dhbw/horb/routePlanner/test/testFiles/testGraphDataStreamReader.xml")));
    }

    @Override
    protected void tearDown() {}

    public void testIsNode() {

    }

}
