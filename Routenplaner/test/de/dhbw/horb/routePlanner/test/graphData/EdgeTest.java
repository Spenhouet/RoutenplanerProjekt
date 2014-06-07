package de.dhbw.horb.routePlanner.test.graphData;

import static org.junit.Assert.fail;

import org.junit.Test;

import de.dhbw.horb.routePlanner.graphData.Edge;


public class EdgeTest {

	private String source = "Tübingen";
	private String target = "Horb";
	private int number = 28;
	private int weight = 6;
	
	private Edge edgeClassUnderTest;
	
	
	@Test
	public void constructor() {
		
		edgeClassUnderTest = new Edge(source, target, number, weight);
		
		
		if(source != edgeClassUnderTest.getSource()) fail("Source doesn't match after constructor.");
		if(target != edgeClassUnderTest.getTarget()) fail("Target doesn't match after constructor.");
		if(number != edgeClassUnderTest.getNumber()) fail("Number doesn't match after constructor.");
		if(weight != edgeClassUnderTest.getWeight()) fail("Weight doesn't match after constructor.");
	
	}

}
