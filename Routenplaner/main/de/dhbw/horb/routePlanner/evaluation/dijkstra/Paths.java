package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.util.LinkedList;

/**
 * Klasse Route
 * Erweitert LinkedList mit erweiterten Operationen in Bezug auf die Klasse Weg 
 * Enthält in der LinkedList Datenobjekte vom Typ Weg
 * 
 * @author Simon
 * 
 */
public class Paths extends LinkedList<Way> {

    private static final long serialVersionUID = 1L;
    private Way cheapestWay;
    private Double cheapestWayPrice;

    public void initializeCheapestWay() {
	cheapestWayPrice = getLast().getPrice();
	cheapestWay = getLast();
	for (Way way : this) {
	    if (way.getPrice() < cheapestWayPrice) {
		setCheapestWay(way);
	    }
	}
    }

    public void addWay(Way way) {
	this.add(way);
    }

    public void setCheapestWay(Way cheapestWay) {
	this.cheapestWay = cheapestWay;
    }

    public Way getCheapestWay() {
	return cheapestWay;
    }
}
