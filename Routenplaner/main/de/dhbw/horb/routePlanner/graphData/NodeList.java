package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.util.StringUtils;

public class NodeList {

	private Map<String, List<String>> nodes;

	public NodeList() {
		nodes = new HashMap<String, List<String>>();
	}

	public void addNode(String name, String id) {
		if (nodes.containsKey(name)) {
			nodes.get(name).add(id);
		} else {
			List<String> listIDs = new ArrayList<String>();
			listIDs.add(id);
			nodes.put(name, listIDs);
		}
	}

	public Boolean hasNode() {
		return !nodes.isEmpty();
	}
	
	public Map<String, String> removeNode(){
		
		Map<String, String> node = new HashMap<String, String>();
		String key = nodes.keySet().iterator().next();
		
//		String strin		
		
		
		
		return node;
	}

}
