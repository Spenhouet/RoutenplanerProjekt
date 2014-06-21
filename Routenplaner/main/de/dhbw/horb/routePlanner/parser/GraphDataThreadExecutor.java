package de.dhbw.horb.routePlanner.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphDataThreadExecutor {

	
	private ExecutorService executor;
	private final int maxThreads = 5; 
	
	public GraphDataThreadExecutor(){
		executor = Executors.newFixedThreadPool(maxThreads);
	}
	
	
	public ExecutorService getExecutor(){
		return this.executor;
	}
	
}
