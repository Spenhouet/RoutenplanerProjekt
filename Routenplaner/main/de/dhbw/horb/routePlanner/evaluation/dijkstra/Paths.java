package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.util.LinkedList;

/**
 * Klasse Route Erweitert LinkedList mit erweiterten Operationen in Bezug auf die Klasse Weg Enth�lt in der LinkedList
 * Datenobjekte vom Typ Weg
 * 
 */
public class Paths extends LinkedList<Way> {

    private static final long serialVersionUID = 1L;
    private Way cheapestWay;
    private Double cheapestWayPrice;

    /**
     * Initialisiert billigsten Weg
     */
    public void initializeCheapestWay() {
	cheapestWayPrice = getLast().getPrice();
	cheapestWay = getLast();
	for (Way way : this) {
	    if (way.getPrice() < cheapestWayPrice) {
		setCheapestWay(way);
	    }
	}
    }

    /**
     * F�gt weg zu Paths hinzu
     * 
     * @param way Weg der hinzugef�gt wird
     */
    public void addWay(Way way) {
	this.add(way);
    }

    /**
     * Setzt billigsten Weg
     * 
     * @param cheapestWay Weg der als Billigster gesetzt wird
     */
    public void setCheapestWay(Way cheapestWay) {
	this.cheapestWay = cheapestWay;
    }

    /**
     * @return cheapestWay Gibt billigsten Weg zur�ck
     */
    public Way getCheapestWay() {
	return cheapestWay;
    }
}
