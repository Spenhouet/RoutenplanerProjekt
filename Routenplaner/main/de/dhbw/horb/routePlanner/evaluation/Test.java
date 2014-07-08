package de.dhbw.horb.routePlanner.evaluation;

public class Test {

    static PrioQue testQue = new PrioQue();
    static Junction j1 = new Junction();
    static Junction j2 = new Junction();
    static Junction j3 = new Junction();
    static Junction j4 = new Junction();

    public static void main(String[] args) {

	j1.setPrice(45);
	j2.setPrice(32);
	j3.setPrice(156);
	j4.setPrice(43);
	testQue.add(j1);
	testQue.add(j2);
	testQue.add(j3);
	testQue.add(j4);

	PrioQueSorter.sort(testQue);
    }
}
