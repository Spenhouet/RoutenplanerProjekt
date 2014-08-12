package de.dhbw.horb.routePlanner.evaluation.dijkstra;

public class Test {

    public static void main(String[] args) {

	PrioQue testQue = new PrioQue();
	Junction j1 = new Junction();
	Junction j2 = new Junction();
	Junction j3 = new Junction();
	Junction j4 = new Junction();
	Junction j5 = new Junction();

	j1.setDuration((long) 34);
	j2.setDuration((long) 124);
	j3.setDuration((long) 35);
	j4.setDuration((long) 23);
	j5.setDuration((long) 29);

	testQue.add(j1);
	testQue.add(j2);
	testQue.add(j3);
	testQue.add(j4);
	testQue.add(j5);

    }
}
