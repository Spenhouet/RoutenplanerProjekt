package de.dhbw.horb.routePlanner.graphData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.dhbw.horb.routePlanner.Constants;

public class NodeMap {

	private Map<String, List<String>> nodes;

	public NodeMap() {
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

	public Map<String, String> removeNode() {

		Map<String, String> node = new HashMap<String, String>();
		String key = nodes.keySet().iterator().next();

		node.put(Constants.NEW_NODE_NAME, key);
		node.put(Constants.NEW_NODE_ID, removeIDsAsCommaString(key));

		return node;
	}

	private String removeIDsAsCommaString(String key) {
		String value = "";
		for (String i : nodes.remove(key)) {
			value += value.isEmpty() ? i : "," + i;
		}
		return value;
	}
}
