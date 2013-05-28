package com.lavacraftserver.HarryPotterSpells.Jobs;

import java.util.ArrayList;
import java.util.List;

public class JobManager {
	List<ClearJob> clear = new ArrayList<>();
	
	public void addClearJob(ClearJob job) {
		clear.add(job);
	}
	
	public void executeClearJobs() {
		for(ClearJob c : clear) {
			c.clear();
		}
	}

}
