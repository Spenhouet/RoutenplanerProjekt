package de.dhbw.horb.routePlanner.evaluation;

import java.util.LinkedList;

/**
 * Klasse PrioQueSorter
 * Sortiert die LinkedList neu
 * Programmiert nach dem Insertionsort-Algorithmus
 * @author Simon
 *
 */
public class PrioQueSorter {

    public static LinkedList<Junction> sort(LinkedList<Junction> prioQue) {

	for (int i = 0; i < prioQue.size(); i++) {
	    Junction temp = prioQue.get(i);
	    int j;
	    for (j = i - 1; j >= 0
		    && temp.getPrice() < prioQue.get(j).getPrice(); j--) {
		prioQue.set(j + 1, prioQue.get(j));
	    }
	    prioQue.set(j + 1, temp);
	}
	return prioQue;
    }
}
