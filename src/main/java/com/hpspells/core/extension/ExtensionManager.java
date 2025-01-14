package com.hpspells.core.extension;

import java.io.File;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.bukkit.event.HandlerList;

import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.extension.Extension.State;
import com.hpspells.core.util.FileExtensionFilter;

/**
 * Handles management of all extensions
 */
public class ExtensionManager {
    private HarryPotterSpells HPS;
    private ExtensionLoader extensionLoader;
    private final File extensionFolder;
    private final SortedMap<Extension, Extension.State> extensions = new TreeMap<Extension, State>(new Comparator<Extension>() {

        @Override
        public int compare(Extension o1, Extension o2) {
            return o1.getName().compareTo(o2.getName());
        }

    });

    /**
     * Constructs a new {@link ExtensionManager}
     * 
     * @param instance an instance of {@link HarryPotterSpells}
     */
    public ExtensionManager(HarryPotterSpells instance) {
        HPS = instance;
        extensionFolder = new File(HPS.getDataFolder(), "Extensions");

        if (!extensionFolder.exists()) {
            extensionFolder.mkdir();
        }

        extensionLoader = new ExtensionLoader(instance, extensionFolder);
    }

    /**
     * Loads the extensions located in the extensions folder.
     */
    public void loadExtensions() {
        for (File file : extensionFolder.listFiles(new FileExtensionFilter(".jar"))) {
            Extension extension = extensionLoader.loadExtension(file);
            extensions.put(extension, State.LOADED);
        }
    }

    /**
     * Disables all extensions, loads and then enables the extension list.
     */
    public void reloadExtensions() {
        disableExtensions();
        this.extensions.clear();
        this.extensionLoader.clearLoaders();
        loadExtensions();
        enableExtensions();
    }

    /**
     * Gets the folder for storing {@link Extension}s
     * 
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
                extension.onEnable();
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
                HandlerList.unregisterAll(extension);
                extensions.put(extension, Extension.State.DISABLED);
            }
        }
    }

    /**
     * Gets the {@link Extension.State} of an {@link Extension}
     * 
     * @param extension the extension
     * @return the extension's state
     */
    public Extension.State getState(Extension extension) {
        return extensions.get(extension);
    }

}
