package de.dhbw.horb.routePlanner.parser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphDataThreadExecutor {

	private ExecutorService executor;

	public GraphDataThreadExecutor(int threads) {
		
		if(threads == 1){
			executor = Executors.newSingleThreadExecutor();
		} else if (threads > 1){
			executor = Executors.newFixedThreadPool(threads);
		}
	}

	public ExecutorService getExecutor() {
		return this.executor;
	}

}
