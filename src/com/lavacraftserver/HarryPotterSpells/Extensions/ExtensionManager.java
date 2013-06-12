package com.lavacraftserver.HarryPotterSpells.Extensions;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.reflections.Reflections;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Commands.HCommandExecutor;
import com.lavacraftserver.HarryPotterSpells.Jobs.DisableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;
import com.lavacraftserver.HarryPotterSpells.Jobs.JobManager;

/**
 * A utility class that manages extensions. </br>
 * <b>NOTE:</b> There should never be access to the extension map because disabling individual extensions does not disable listeners/commands/jobs.
 */
public class ExtensionManager implements EnableJob, DisableJob {
	private Map<String, Extension> extensionList = new HashMap<String, Extension>();
	private File extensionFolder;
	private static boolean instantated = false;
	private HPS HPS;
	
	{ // Register the enable and disable jobs in a non-static initialiser
	    JobManager.addEnableJob(this);
	    JobManager.addDisableJob(this);
	}
	
	/**
	 * Constructs the Extension Manager, loading all extensions. </br>
	 * It should be noted that extensions are not enabled until the EnableJob is called.
	 *
	 * @param plugin an instance of {@link HPS}
	 */
	public ExtensionManager(HPS plugin) {
	    HPS = plugin;
	    
	    if(ExtensionManager.instantated)
	        return;
	    
	    ExtensionManager.instantated = true;
		HPS.PM.debug(HPS.Localisation.getTranslation("dbgExtensionLoading"));
		
		extensionFolder = new File(HPS.getDataFolder(), "Extensions");
		if(!extensionFolder.exists())
			extensionFolder.mkdirs();
		
		int commands = 0;
		
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
					HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errCouldNotAddExtensionFile", file.getName()));
					continue;
				}
				
				Reflections reflections = new Reflections(description.getPackage());
				for(Class<? extends Extension> e : reflections.getSubTypesOf(Extension.class)) {
					Extension ex = e.getConstructor(HPS.class).newInstance(HPS);
					ex.setDescription(description);
					ex.initiate(new File(extensionFolder, description.getName()));
					extensionList.put(description.getName().toUpperCase(), ex);
					break;
				}
				
				for(Class<? extends HCommandExecutor> c : reflections.getSubTypesOf(HCommandExecutor.class)) {
					if(HPS.addHackyCommand(c))
						commands++;
				}
				
			} catch (Exception e) {
				HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errExtensionLoading", file.getName()));
				HPS.PM.debug(e);
			}
		}
		
		HPS.PM.debug(HPS.Localisation.getTranslation("dbgExtensionLoaded", extensionList.size(), commands));
	}
	
	private class ExtensionFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
		}
		
	}

	@Override
	public void onDisable(PluginManager pm) {
		HPS.PM.debug(HPS.Localisation.getTranslation("dbgDisablingExtensions"));
		Iterator<Entry<String, Extension>> it = extensionList.entrySet().iterator();
		while(it.hasNext())
			it.next().getValue().disable(pm);
		if(extensionList.size() != 0)
			HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("dbgExtensionsDisabled", extensionList.size()));
	}

	@Override
	public void onEnable(PluginManager pm) {
        HPS.PM.debug(HPS.Localisation.getTranslation("dbgEnablingExtensions"));
		Iterator<Entry<String, Extension>> it = extensionList.entrySet().iterator();
		while(it.hasNext())
			it.next().getValue().disable(pm);
		if(extensionList.size() != 0)
            HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("dbgExtensionsEnabled", extensionList.size()));
	}
	
}
