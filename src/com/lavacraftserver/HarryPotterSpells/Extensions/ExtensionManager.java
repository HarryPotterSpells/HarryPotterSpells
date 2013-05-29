package com.lavacraftserver.HarryPotterSpells.Extensions;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.YamlConfiguration;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.DisableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;

public class ExtensionManager {
	private Map<String, Extension> extensionList = new HashMap<>();
	private File extensionFolder;
	
	public ExtensionManager() {
		HPS.PM.log(Level.INFO, "Loading extensions...");
		
		extensionFolder = new File(HPS.Plugin.getDataFolder(), "extensions");
		if(!extensionFolder.exists())
			extensionFolder.mkdirs();
		
		int commands = 0, clearJobs = 0, enableJobs = 0, disableJobs = 0;
		
		for(File file : extensionFolder.listFiles(new ExtensionFileFilter())) {
			try {
				ZipFile zip = new ZipFile(file);
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(zip.getInputStream((zip.getEntry("extension.yml"))));
				ExtensionDescription description = new ExtensionDescription();
				description.setAuthor(yaml.getString("Author"));
				description.setDescription(yaml.getString("Description"));
				description.setPackage(yaml.getString("Package"));
				description.setName(yaml.getString("Name"));
				description.setVersion(yaml.getString("Version"));
				zip.close();
				
				if(!HPS.PM.hackFile(file)) {
					HPS.PM.log(Level.WARNING, "Could not add file " + file.getName() + " to the extension list. This is probably an I/O error.");
					continue;
				}
				
				Reflections reflections = new Reflections(description.getPackage());
				for(Class<? extends Extension> e : reflections.getSubTypesOf(Extension.class)) {
					Extension ex = e.newInstance();
					ex.setDescription(description);
					ex.initiate(new File(extensionFolder, description.getName()));
					extensionList.put(description.getName().toUpperCase(), ex);
					break;
				}
				
				for(Class<? extends ClearJob> c : reflections.getSubTypesOf(ClearJob.class)) {
					try {
						HPS.JobManager.addClearJob(c.newInstance());
						clearJobs++;
					} catch (InstantiationException | IllegalAccessException e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst a clear job in extension " + file.getName() + " to the Job Manager.");
						HPS.PM.debug(e);
					}
				}
				
				for(Class<? extends EnableJob> c : reflections.getSubTypesOf(EnableJob.class)) {
					try {
						HPS.JobManager.addEnableJob(c.newInstance());
						enableJobs++;
					} catch(InstantiationException | IllegalAccessException e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst adding an enable job in extension " + file.getName() + " to the Job Manager.");
						HPS.PM.debug(e);
					}
				}

				for(Class<? extends DisableJob> c : reflections.getSubTypesOf(DisableJob.class)) {
					try {
						HPS.JobManager.addDisableJob(c.newInstance());
						disableJobs++;
					} catch (InstantiationException | IllegalAccessException e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst adding a disable job in extension " + file.getName() + " to the Job Manager");
						HPS.PM.debug(e);
					}
				}
			} catch (Exception e) {
				HPS.PM.log(Level.WARNING, "An error occurred whilst loading " + file.getName() + " to the extension list. This extension may work anyway...");
				HPS.PM.debug(e);
			}
		}
		
		HPS.PM.log(Level.INFO, "Loaded " + extensionList.size() + " extensions with " + commands + "commands.");
		HPS.PM.debug("There are also " + clearJobs + " clear jobs, " + enableJobs + " enable jobs and " + disableJobs + " disable jobs.");
	}
	
	private class ExtensionFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}
		
	}
	
}
