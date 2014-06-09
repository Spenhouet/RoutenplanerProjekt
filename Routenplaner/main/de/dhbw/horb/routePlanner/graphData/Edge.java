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
		setLength(weight);
	}
	
	
	public String getSource() {
		return source;
	}
	
	private void setSource(String source) {
		this.source = source;
	}
	
	public String getTarget() {
		return target;
	}
	
	private void setTarget(String target) {
		this.target = target;
	}
	
	public int getNumber() {
		return number;
	}
	
	private void setNumber(int number) {
		this.number = number;
	}
	
	public int getLength() {
		return length;
	}
	
	private void setLength(int length) {
		this.length = length;
	}
	
	
}
