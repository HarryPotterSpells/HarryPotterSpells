package com.hpspells.core.configuration;

import com.hpspells.core.HarryPotterSpells;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Represents a custom configuration file in YAML format
 */
public class CustomConfiguration {
    private FileConfiguration fileConfiguration = null;
    private File file = null;
    private HarryPotterSpells HPS;

    /**
     * Constructs a new {@link CustomConfiguration} without copying any defaults
     *
     * @param instance an instance of {@link HarryPotterSpells}
     * @param file     where to store the custom configuration
     */
    public CustomConfiguration(HarryPotterSpells instance, File file) {
        this(instance, file, null, null);
    }

    /**
     * Constructs a new {@link CustomConfiguration}, copying defaults from an {@link InputStream}
     *
     * @param instance an instance of {@link HarryPotterSpells}
     * @param file     where to store the custom configuration
     * @param stream   an input stream to copy default configuration from
     * @param header   String array of text as header
     */
	@SuppressWarnings("deprecation")
	public CustomConfiguration(HarryPotterSpells instance, File file, InputStream stream, String[] header) {
        this.HPS = instance;
        this.file = file;
        this.fileConfiguration = YamlConfiguration.loadConfiguration(file);
        if (stream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            fileConfiguration.setDefaults(defConfig);
            fileConfiguration.options().copyDefaults(true);
        } 
        if (header != null) {
        	String head = "";
            for (String line : header) {
            	head = head + line + "\n";
            }
            fileConfiguration.options().header(head);
            fileConfiguration.options().copyHeader(true);
        }
        save();
    }
    
    /**
     * Gets the custom configuration file
     *
     * @return the {@link FileConfiguration} represented by this class
     */
    public FileConfiguration get() {
        return fileConfiguration;
    }

    /**
     * Saves change made to this custom configuration file to disk
     */
    public void save() {
        try {
            get().save(file);
        } catch (IOException ex) {
            HPS.PM.log(Level.SEVERE, HPS.Localisation.getTranslation("ccnCouldNotSave", file.getName()));
            HPS.PM.debug(ex);
        }
    }

}
