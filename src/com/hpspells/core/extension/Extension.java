package com.hpspells.core.extension;

import com.hpspells.core.HPS;
import com.hpspells.core.util.ReflectionsReplacement;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Represents an Extension to HarryPotterSpells
 */
public class Extension {
    private HPS HPS;
    private String name, description, authors, version;

    // Functions to be overridden by developers
    public void onEnable() {}
    public void onDisable() {}

    /**
     * Creates a new {@link Extension} instance
     * @param instance an instance of {@link HPS}
     * @param extensionFile the jar file of the extension
     * @param extensionDescriptionFile the extension's description file
     * @return the newly created Extension
     */
    public static Extension create(HPS instance, File extensionFile, YamlConfiguration extensionDescriptionFile) throws Exception {
        ReflectionsReplacement.addFileToClasspath(instance.getHPSClassLoader(), extensionFile);

        Class<?> clazz = Class.forName(extensionDescriptionFile.getString("Main"));
        if(!Extension.class.isAssignableFrom(clazz)) {
            throw new ClassNotFoundException("Could not find main class that extends Extension");
        }

        Extension extension = (Extension) clazz.getConstructor(com.hpspells.core.HPS.class).newInstance(instance);

        extension.HPS = instance;

        extension.name = extensionDescriptionFile.getString("Name");
        extension.description = extensionDescriptionFile.getString("Description");
        extension.authors = extensionDescriptionFile.getString("Authors");
        extension.version = extensionDescriptionFile.getString("Version");

        Validate.notNull(extension.name, "Extension name cannot be null");
        Validate.notNull(extension.version, "Extension name cannot be null");

        return extension;
    }

    /**
     * Gets the name of this {@link Extension}
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of this {@link Extension}
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the authors of this {@link Extension}
     * @return the authors
     */
    public String getAuthors() {
        return authors;
    }

    /**
     * Gets the version string of this {@link Extension}
     * @return the version string
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the data folder for this {@link Extension}, creating it if it does not exist
     * @return the data folder
     */
    public File getDataFolder() {
        File file = new File(HPS.ExtensionManager.getExtensionFolder(), getName());

        if(!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    /**
     * Gets the default {@link FileConfiguration} for this {@link Extension}, creating it if it does not exist
     * @return the config
     */
    public FileConfiguration getConfig() {
        File file = new File(getDataFolder(), "config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("extCouldNotCreateConfig", getName()));
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * Gets the {@link State} of this {@link Extension}
     * @return the state
     */
    public State getState() {
        return HPS.ExtensionManager.getState(this);
    }

    /**
     * An enum that represents the states an {@link Extension} can be in
     */
    public enum State {
        LOADED,
        ENABLED,
        DISABLED
    }


}
