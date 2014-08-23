package com.hpspells.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * A class that manages localisation within the plugin
 */
public class Localisation {
    private Properties lang, defaultLang;
    private ArrayList<File> files = new ArrayList<File>();
    private File properties, base, en;
    private boolean once = false;
    private HPS HPS;

    /**
     * Constructs the {@link Localisation} class
     *
     * @param instance an instance of {@link HPS}
     */
    public Localisation(HPS instance) {
        this.HPS = instance;
        load();
    }

    /**
     * Loads or reloads the current language
     */
    public void load() {
        lang = new Properties();
        defaultLang = new Properties();
        base = new File(HPS.getDataFolder(), "Language Files");
        properties = new File(base, HPS.getConfig().getString("language", "us-english") + ".properties");
        en = new File(base, "us-english.properties");
        files.add(properties);
        files.add(en);

        for (File file : files) {
        	this.generateFile(file, file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\") + 1));
        }
        
        try {
            lang.load(new FileInputStream(properties));
            defaultLang.load(new FileInputStream(en));
        } catch (FileNotFoundException e) { // Specifed lang file does not exist. Revert to default once.
            HPS.PM.log(Level.WARNING, "Could not find the language file for language " + HPS.getConfig().getString("language") + ". Reverting to default language...");
            HPS.PM.debug(e);
            if (!once) {
                once = true;
                load();
            }
            return;
        } catch (IOException e) { // Panic and tell the user stuff has gone wrong
            HPS.PM.log(Level.SEVERE, "Could not load the language file. Plugin will not function.", "Disabling plugin...");
            Bukkit.getServer().getPluginManager().disablePlugin(HPS);
            return;
        }

        HPS.PM.debug(getTranslation("dbgLanguageLoaded", properties.getName()));
    }

    /**
     * Gets a string from the currently loaded language file
     *
     * @param key  the translation to get
     * @param args objects used in {@link String#format(String, Object...)}
     * @return the translation or {@code null} if not found
     */
    public String getTranslation(String key, Object... args) {
        return lang.getProperty(key) == null ? String.format(defaultLang.getProperty(key), args) : String.format(lang.getProperty(key), args);
    }

    
    private void generateFile(File file, final String lang) {
    	if (!file.exists()) {
        	try {
        		file.getParentFile().mkdirs();
        		file.createNewFile();

                Files.copy(new InputSupplier<InputStream>() {

                    @Override
                    public InputStream getInput() throws IOException {
                        return HPS.class.getResourceAsStream("/" + lang);
                    }

                }, file);
            } catch (IOException e) {
                HPS.PM.log(Level.WARNING, "Could not copy the language file" + lang + ".");
                HPS.PM.debug(e);
            }
        }
    }
}
