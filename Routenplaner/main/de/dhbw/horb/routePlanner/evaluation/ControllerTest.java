package de.dhbw.horb.routePlanner.evaluation;

import java.util.ArrayList;
import java.util.List;

public class ControllerTest {

    public static void main(String[] args) {

	List<String> startnode = new ArrayList<String>();
	List<String> endnode = new ArrayList<String>();

	startnode.add("332");
	startnode.add("456");
	startnode.add("897");

	endnode.add("123");
	endnode.add("897");

	Dijkstra test = new Dijkstra(startnode, endnode);
	test.calculatingRoute();

	//	test.calculatingRoute();

	//	isEqualTest();
    }

    private static void isEqualTest() {
	List<String> startnode = new ArrayList<String>();
	List<String> endnode = new ArrayList<String>();

	startnode.add("332");
	startnode.add("456");
	startnode.add("897");

	endnode.add("123");
	endnode.add("897");

	Dijkstra test = new Dijkstra(startnode, endnode);

	if (test.isEqual(test.getStartnode(), test.getEndnode()))
	    System.out.println("True");
	else
	    System.out.println("False");
    }
}
