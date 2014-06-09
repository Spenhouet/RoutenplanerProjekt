package de.dhbw.horb.routePlanner.test.graphData;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.dhbw.horb.routePlanner.graphData.Edge;


public class EdgeTest {

	private String source = "Tübingen";
	private String target = "Horb";
	private String identifier = "A 28";
	private Double length = 6.0;
	
	private Edge edgeClassUnderTest;
	
	
	@Test
	public void constructor() {
		
		edgeClassUnderTest = new Edge(source, target, identifier, length);
		
		
		if(source != edgeClassUnderTest.getSource()) fail("Source doesn't match after constructor.");
		if(target != edgeClassUnderTest.getTarget()) fail("Target doesn't match after constructor.");
		if(identifier != edgeClassUnderTest.getIdentifier()) fail("Identifier doesn't match after constructor.");
		if(length != edgeClassUnderTest.getLength()) fail("Weight doesn't match after constructor.");
	
	}

}
