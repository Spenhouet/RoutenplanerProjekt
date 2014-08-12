package de.dhbw.horb.routePlanner.evaluation.dijkstra;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse Crossroads
 * Repräsentiert eine Kreuzung mit verschiedenen Auf- Abfahrten
 * Besitzt Eigenschaft Kosten, mit der die Kreuzung erreicht werden kann
 * @author Simon
 *
 */
public class Junction {

    private String name;
    private Long duration;
    private Double distance;
    private List<String> ids = new ArrayList<String>();
    private List<Junction> neighbours = new ArrayList<Junction>();

    public Junction() {

    }

    public Junction(String name, List<String> ids, Long duration) {
	this.name = name;
	this.ids = ids;
	this.duration = duration;
    }

    public Junction(String name, List<String> ids, Double distance) {
	this.name = name;
	this.ids = ids;
	this.distance = distance;
    }

    public Junction(String name, List<String> ids) {
	this.name = name;
	this.ids = ids;
    }

    public List<String> getIds() {
	return ids;
    }

    public void setId(int index, String id) {
	ids.set(index, id);
    }

    public void setDuration(Long duration) {
	this.duration = duration;
    }

    public Long getDuration() {
	return duration;
    }

    public void setDistance(Double distance) {
	this.distance = distance;
    }

    public Double getDistance() {
	return distance;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }
}
