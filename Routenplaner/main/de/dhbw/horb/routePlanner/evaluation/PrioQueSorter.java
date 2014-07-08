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

	//TODO: nodePrice und prioQue Format!!!
	public static LinkedList<Integer> sortNodes(int[] nodePrice, LinkedList<Integer> prioQue) {
		for (int i = 0; i < prioQue.size(); i++) {
			int temp = prioQue.get(i);
			int j;
			for (j = i - 1; j >= 0 && nodePrice[temp] < nodePrice[prioQue.get(j)]; j--) {
				prioQue.set(j + 1, prioQue.get(j));
			}
			prioQue.set(j + 1, temp);
		}
		return prioQue;
	}
}
