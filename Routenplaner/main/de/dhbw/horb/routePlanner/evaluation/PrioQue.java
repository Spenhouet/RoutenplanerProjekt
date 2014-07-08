package de.dhbw.horb.routePlanner.evaluation;

import java.util.LinkedList;

/**
 * Klasse PrioQue
 * Erweitert LinkedList für erweiterte Operationen.
 * Beinhaltet Objekte vom Typ Crossroads.
 * Stellt Schlange der noch zu verarbeitenden Kreuzungen dar.
 * @author Simon
 *
 */
public class PrioQue extends LinkedList<Junction> {

    private static final long serialVersionUID = 1L;

    public PrioQue() {

    }

    /**
     * Prüft ob eine Kreuzung schon in der PrioQue vorhanden ist
     */
    @Override
    public boolean contains(Object crossroads) {
	Junction cross = (Junction) crossroads;
	for (String id : cross.getIds()) {
	    for (Junction c : this) {
		for (String id2 : c.getIds()) {
		    if (id == id2)
			return true;
		}
	    }
	}
	return false;
    }
}
