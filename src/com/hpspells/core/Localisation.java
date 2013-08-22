package com.hpspells.core;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

/**
 * A class that manages localisation within the plugin
 */
public class Localisation {
    private Properties lang;
    private File properties, base, defaultt;
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
        base = new File(HPS.getDataFolder(), "Language Files");
        properties = new File(base, HPS.getConfig().getString("Language", "us-english") + ".properties");
        defaultt = new File(base, "us-english.properties");

        if (!defaultt.exists()) {
            try {
                defaultt.getParentFile().mkdirs();
                defaultt.createNewFile();

                Files.copy(new InputSupplier<InputStream>() {

                    @Override
                    public InputStream getInput() throws IOException {
                        return HPS.class.getResourceAsStream("/us-english.properties");
                    }

                }, defaultt);
            } catch (IOException e) {
                HPS.PM.log(Level.WARNING, "Could not copy the default language file.");
                HPS.PM.debug(e);
            }
        }

        try {
            lang.load(new FileInputStream(properties));
        } catch (FileNotFoundException e) { // Specifed lang file does not exist. Revert to default once.
            HPS.PM.log(Level.WARNING, "Could not find the language file for language " + HPS.getConfig().getString("Language", "us-english") + ". Reverting to default language...");
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
        return String.format(lang.getProperty(key), args);
    }

}
