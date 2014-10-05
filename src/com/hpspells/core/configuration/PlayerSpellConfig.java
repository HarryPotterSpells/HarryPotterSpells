package com.hpspells.core.configuration;

import com.hpspells.core.HPS;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that manages the {@code PlayerSpellConfig.yml} file. <br>
 * This file is used to store all the spells that players know.
 */
public class PlayerSpellConfig extends CustomConfiguration {

    /**
     * Constructs a new {@link PlayerSpellConfig} without copying any defaults
     *
     * @param instance an instance of {@link HPS}
     * @param file     where to store the custom configuration
     */
    public PlayerSpellConfig(HPS instance, File file) {
        super(instance, file);
    }

    /**
     * Constructs a new {@link CustomConfiguration}, copying defaults from an {@link InputStream}
     *
     * @param instance an instance of {@link HPS}
     * @param file     where to store the custom configuration
     * @param stream   an input stream to copy default configuration from
     */
    public PlayerSpellConfig(HPS instance, File file, InputStream stream) {
        super(instance, file, stream, null);
    }

    /**
     * The current version specifying the format of the {@code PlayerSpellConfig.yml}
     */
    public static final double CURRENT_VERSION = 1.1d;

    /**
     * A utility function that is meant to be used instead of {@link FileConfiguration#getStringList(String)}. <br>
     * This is needed because the Bukkit version returns {@code null} when no String list is found.
     *
     * @param string the path to the String list
     * @return the string list at that location or an empty {@link ArrayList}
     */
    public List<String> getStringListOrEmpty(String string) {
        return get().getStringList(string) == null ? new ArrayList<String>() : get().getStringList(string);
    }

}
