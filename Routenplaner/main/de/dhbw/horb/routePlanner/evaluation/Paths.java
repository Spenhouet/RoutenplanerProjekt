package de.dhbw.horb.routePlanner.evaluation;

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
    private Junction startnode;
    private Junction endnode;
    private Way cheapestWay;
    private int cheapestWayPrice;

    public Paths(Junction startnode, Junction endnode) {
	this.startnode = startnode;
	this.endnode = endnode;
    }

    public void initializeCheapestWay() {
	cheapestWayPrice = getLast().getPrice();
	cheapestWay = getLast();
	for (Way way : this) {
	    if (way.getPrice() < cheapestWayPrice
		    && way.getLastNode() == endnode) {
		setCheapestWayPrice(way.getPrice());
		setCheapestWay(way);
	    }
	}
    }

    public void setCheapestWayPrice(int cheapestWayPrice) {
	this.cheapestWayPrice = cheapestWayPrice;
    }

    public int getCheapestWayPrice() {
	return cheapestWayPrice;
    }

    public void setCheapestWay(Way cheapestWay) {
	this.cheapestWay = cheapestWay;
    }

    public Way getCheapestWay() {
	return cheapestWay;
    }
}
