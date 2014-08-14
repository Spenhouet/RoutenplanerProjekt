package de.dhbw.horb.routePlanner.evaluation.dijkstra;

public class ControllerTest {

    public static void main(String[] args) {

	String startnode = "Horb am Neckar (30)";
	String endnode = "Villingen-Schwenningen (35)";

	Dijkstra dijkstra = new Dijkstra(startnode, endnode);
	dijkstra.calculateRoute("Dauer");
	dijkstra.printNodes();
    }
}
