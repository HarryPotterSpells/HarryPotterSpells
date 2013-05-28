package com.lavacraftserver.HarryPotterSpells.Jobs;

import java.util.ArrayList;
import java.util.List;

/**
 * The manager for all Jobs available.
 */
public class JobManager {
	List<ClearJob> clear = new ArrayList<>();
	
	/**
	 * Adds a clear job to the executor
	 * @param job the clear job
	 */
	public void addClearJob(ClearJob job) {
		clear.add(job);
	}
	
	/**
	 * Executes all clear jobs
	 */
	public void executeClearJobs() {
		for(ClearJob c : clear)
			c.clear();
	}

}
