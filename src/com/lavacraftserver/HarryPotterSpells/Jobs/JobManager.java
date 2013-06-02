package com.lavacraftserver.HarryPotterSpells.Jobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.PluginManager;

/**
 * The manager for all Jobs available.
 */
public class JobManager {
	List<ClearJob> clear = new ArrayList<ClearJob>();
	List<EnableJob> enable = new ArrayList<EnableJob>();
	List<DisableJob> disable = new ArrayList<DisableJob>();
	
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
	
	/**
	 * Adds an enable job to the executor
	 * @param job the enable job
	 */
	public void addEnableJob(EnableJob job) {
		enable.add(job);
	}
	
	/**
	 * Executes all enable jobs
	 * @param pm a plugin manager instance
	 */
	public void executeEnableJobs(PluginManager pm) {
		for(EnableJob job : enable)
			job.onEnable(pm);
	}
	
	/**
	 * Adds a disable job to the executor
	 * @param job the disable job
	 */
	public void addDisableJob(DisableJob job) {
		disable.add(job);
	}
	
	/**
	 * Executed all disable jobs
	 * @param pm a plugin manager instance
	 */
	public void executeDisableJob(PluginManager pm) {
		for(DisableJob job : disable)
			job.onDisable(pm);
	}

}
