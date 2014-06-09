package de.dhbw.horb.routePlanner.graphData;

public class Edge {

	private String source;
	private String target;
	private String identifier;
	private Double length;
	
	
	public Edge(String source, String target, String identifier, Double length){
		
		setSource(source);
		setTarget(target);
		setIdentifier(identifier);
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
	
	public String getIdentifier() {
		return identifier;
	}
	
	private void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public Double getLength() {
		return length;
	}
	
	private void setLength(Double length) {
		this.length = length;
	}
	
	
}
