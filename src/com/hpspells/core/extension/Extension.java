package com.hpspells.core.extension;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Represents an Extension to HarryPotterSpells
 */
public class Extension {
    private String name, description, authors, version;

    // Prevent initialisation
    private Extension() {}

    /**
     * Creates a new {@link Extension} instance
     * @param extensionFile the jar file of the extension
     * @param extensionDescriptionFile the extension's description file
     * @return the newly created Extension
     */
    public static Extension create(File extensionFile, YamlConfiguration extensionDescriptionFile) {
        Extension extension = new Extension();

        return null;
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

}
