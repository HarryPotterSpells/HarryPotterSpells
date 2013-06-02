package com.lavacraftserver.HarryPotterSpells.Extensions;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.zip.ZipFile;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Jobs.ClearJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.DisableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;

/**
 * A utility class that manages extensions. </br>
 * <b>NOTE:</b> There should never be access to the extension map because disabling individual extensions does not disable listeners/commands/jobs.
 */
public class ExtensionManager implements EnableJob, DisableJob {
	private Map<String, Extension> extensionList = new HashMap<String, Extension>();
	private File extensionFolder;
	private static boolean instantated = false;
	
	/**
	 * Constructs the Extension Manager, loading all extensions. </br>
	 * It should be noted that extensions are not enabled until the EnableJob is called.
	 */
	public ExtensionManager() {
	    if(ExtensionManager.instantated)
	        return;
	    
	    ExtensionManager.instantated = true;
		HPS.PM.log(Level.INFO, "Loading extensions...");
		
		extensionFolder = new File(HPS.Plugin.getDataFolder(), "Extensions");
		if(!extensionFolder.exists())
			extensionFolder.mkdirs();
		
		int commands = 0, clearJobs = 0, enableJobs = 0, disableJobs = 0, listeners = 0;
		
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
				
				String ext = description.getName();
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
					} catch (Exception e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst a clear job in extension " + ext + " to the Job Manager.");
						HPS.PM.debug(e);
					}
				}
				
				for(Class<? extends EnableJob> c : reflections.getSubTypesOf(EnableJob.class)) {
					try {
						HPS.JobManager.addEnableJob(c.newInstance());
						enableJobs++;
					} catch(Exception e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst adding an enable job in extension " + ext + " to the Job Manager.");
						HPS.PM.debug(e);
					}
				}

				for(Class<? extends DisableJob> c : reflections.getSubTypesOf(DisableJob.class)) {
					try {
						HPS.JobManager.addDisableJob(c.newInstance());
						disableJobs++;
					} catch (Exception e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst adding a disable job in extension " + ext + " to the Job Manager");
						HPS.PM.debug(e);
					}
				}
				
				for(Class<? extends CommandExecutor> c : reflections.getSubTypesOf(CommandExecutor.class)) {
					if(HPS.addHackyCommand(c))
						commands++;
				}
				
				for(Class<? extends Listener> c : reflections.getSubTypesOf(Listener.class)) {
					try {
						Bukkit.getPluginManager().registerEvents(c.newInstance(), HPS.Plugin);
						listeners++;
					} catch(Exception e) {
						HPS.PM.log(Level.WARNING, "An error occurred whilst adding a listener in extension " + ext + ".");
						HPS.PM.debug(e);
					}
				}
				
			} catch (Exception e) {
				HPS.PM.log(Level.WARNING, "An error occurred whilst loading " + file.getName() + " to the extension list. This extension may work anyway...");
				HPS.PM.debug(e);
			}
		}
		
		HPS.PM.log(Level.INFO, "Loaded " + extensionList.size() + " extensions with " + commands + " commands and " + listeners + " listeners.");
		HPS.PM.debug("There are also " + clearJobs + " clear jobs, " + enableJobs + " enable jobs and " + disableJobs + " disable jobs.");
	}
	
	private class ExtensionFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}
		
	}

	@Override
	public void onDisable(PluginManager pm) {
		HPS.PM.log(Level.INFO, "Disabling extensions...");
		Iterator<Entry<String, Extension>> it = extensionList.entrySet().iterator();
		while(it.hasNext())
			it.next().getValue().disable(pm);
		HPS.PM.log(Level.INFO, "Disabled " + extensionList.size() + " extensions.");
	}

	@Override
	public void onEnable(PluginManager pm) {
		HPS.PM.log(Level.INFO, "Enabling extensions...");
		Iterator<Entry<String, Extension>> it = extensionList.entrySet().iterator();
		while(it.hasNext())
			it.next().getValue().disable(pm);
		HPS.PM.log(Level.INFO, "Enabled " + extensionList.size() + " extensions.");
	}
	
}
