package com.lavacraftserver.HarryPotterSpells.Extensions;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipFile;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;

public class ExtensionManager {
	private Map<String, Extension> extensionList = new HashMap<>();
	private File extensionFolder;
	
	public ExtensionManager() {
		HPS.PM.log(Level.INFO, "Loading extensions...");
		
		extensionFolder = new File(HPS.Plugin.getDataFolder(), "extensions");
		if(!extensionFolder.exists())
			extensionFolder.mkdirs();
		
		int commands = 0, clearJobs = 0;
		
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
					HPS.JobManager.addClearJob(c.newInstance());
					clearJobs++;
				}
				
				for(Class<? extends CommandExecutor> clazz : reflections.getSubTypesOf(CommandExecutor.class)) {
					if(HPS.addHackyCommand(clazz))
						commands++;
				}
				
			} catch (Exception e) {
				HPS.PM.log(Level.WARNING, "An error occurred whilst loading " + file.getName() + " to the extension list. Report this error if you want. This extension may work anyway...");
				if(HPS.Plugin.getConfig().getBoolean("DebugMode", false))
					e.printStackTrace();
			}
		}
		
		HPS.PM.log(Level.INFO, "Loaded " + extensionList.size() + " extensions with " + commands + "commands.");
		HPS.PM.debug("There are also " + clearJobs + " clear jobs.");
	}
	
	private class ExtensionFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}
		
	}
	
}
