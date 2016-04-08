package com.hpspells.core.extension;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.bukkit.configuration.file.YamlConfiguration;

import com.hpspells.core.HPS;
import com.hpspells.core.extension.Extension.State;

/**
 * Handles management of all extensions
 */
public class ExtensionManager {
    private HPS HPS;
    private final File extensionFolder;
    private final SortedMap<Extension, Extension.State> extensions = new TreeMap<Extension, State>(new Comparator<Extension>() {

        @Override
        public int compare(Extension o1, Extension o2) {
            return o1.getName().compareTo(o2.getName());
        }

    });

    /**
     * Constructs a new {@link ExtensionManager}
     * @param instance an instance of {@link HPS}
     */
    public ExtensionManager(HPS instance) {
        HPS = instance;
        extensionFolder = new File(HPS.getDataFolder(), "Extensions");

        if(!extensionFolder.exists()) {
            extensionFolder.mkdir();
        }
    }

    /**
     * Disables all extensions and then reloads the extension list.
     */
    public void reloadExtensions() {
        disableExtensions();
        extensions.clear();

        for(File file : extensionFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory() && pathname.getName().endsWith(".jar");
            }
        })) {
            try {
            	HPS.PM.debug("File name: " + file.getName());
                ZipFile zip = new ZipFile(file);
                ZipEntry description = zip.getEntry("extension.yml");

                if(description == null) { // The jar is missing a description
                    HPS.PM.log(Level.INFO, HPS.Localisation.getTranslation("extMissingDescription", file.getName()));
                    continue;
                }
                extensions.put(Extension.create(HPS, file, YamlConfiguration.loadConfiguration(zip.getInputStream(description))), Extension.State.LOADED);
                zip.close();
            } catch (Exception e) {
                HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errExtensionLoading", file.getName()));
                HPS.PM.debug(e);
            }
        }
    }

    /**
     * Gets the folder for storing {@link Extension}s
     * @return the folder
     */
    public File getExtensionFolder() {
        return extensionFolder;
    }

    /**
     * Enables all {@link Extension}s with the {@link Extension.State#LOADED} state
     */
    public void enableExtensions() {
    	for (Extension extension : extensions.keySet()) {
    		if (extensions.get(extension) == Extension.State.LOADED) {
    			extension.load();
                HPS.getServer().getPluginManager().registerEvents(extension, HPS);
                extensions.put(extension, Extension.State.ENABLED);
    		}
    	}
    }

    /**
     * Disables all {@link Extension}s with the {@link Extension.State#ENABLED} state
     */
    public void disableExtensions() {
    	for (Extension extension : extensions.keySet()) {
    		if (extensions.get(extension) == Extension.State.ENABLED) {
    			extension.onDisable();
                extensions.put(extension, Extension.State.DISABLED);
    		}
    	}
    }

    /**
     * Gets the {@link Extension.State} of an {@link Extension}
     * @param extension the extension
     * @return the extension's state
     */
    public Extension.State getState(Extension extension) {
        return extensions.get(extension);
    }

}
