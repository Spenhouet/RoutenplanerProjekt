package de.dhbw.horb.routePlanner.graphData;

public class Edge {

	private String source;
	private String target;
	private Integer number;
	private Integer length;
	
	
	public Edge(String source, String target, Integer number, Integer length){
		
		setSource(source);
		setTarget(target);
		setNumber(number);
		setLength(length);
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
	
	public Integer getNumber() {
		return number;
	}
	
	private void setNumber(Integer number) {
		this.number = number;
	}
	
	public Integer getLength() {
		return length;
	}
	
	private void setLength(Integer length) {
		this.length = length;
	}
	
	
}
