package de.dhbw.horb.routePlanner.graphData;

public class Edge {

	private String source;
	private String target;
	private int number;
	private int length;
	
	
	public Edge(String source, String target, int number, int weight){
		
		setSource(source);
		setTarget(target);
		setNumber(number);
		setWeight(weight);
	}
	
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getTarget() {
		return target;
	}
	
	public void setTarget(String target) {
		this.target = target;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public int getWeight() {
		return length;
	}
	
	public void setWeight(int length) {
		this.length = length;
	}
	
	
}
