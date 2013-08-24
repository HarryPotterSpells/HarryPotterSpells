package com.hpspells.core.extension;

import com.hpspells.core.HPS;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Handles management of all extensions
 */
public class ExtensionManager {
    private HPS HPS;
    private final File extensionFolder;
    private final SortedMap<Extension, Extension.State> extensions = new TreeMap(new Comparator<Extension>() {

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
        for(Map.Entry<Extension, Extension.State> entry : extensions.entrySet()) {
            if(entry.getValue() == Extension.State.LOADED) {
                entry.getKey().onEnable();
                extensions.put(entry.getKey(), Extension.State.ENABLED);
            }
        }
    }

    /**
     * Disables all {@link Extension}s with the {@link Extension.State#ENABLED} state
     */
    public void disableExtensions() {
        for(Map.Entry<Extension, Extension.State> entry : extensions.entrySet()) {
            if(entry.getValue() == Extension.State.ENABLED) {
                entry.getKey().onDisable();
                extensions.put(entry.getKey(), Extension.State.DISABLED);
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
