package com.hpspells.core.extension;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.configuration.file.YamlConfiguration;

import com.hpspells.core.HarryPotterSpells;

public class ExtensionLoader {

    private HarryPotterSpells HPS;
    private File directory;
    private ArrayList<File> files;
    private final List<URLClassLoader> loaders = new CopyOnWriteArrayList<URLClassLoader>();

    public ExtensionLoader(final HarryPotterSpells HPS, final File directory) {
        this.HPS = HPS;
        this.directory = directory;
    }

    /**
     * Loads the extension jar file from disk, reads the extension.yml file. Creates a new instance of Extension and initiates it.
     * 
     * @param file The extension jar file
     * @return An initiated Extension
     */
    @SuppressWarnings("deprecation")
	public Extension loadExtension(final File file) {
        JarFile jar = null;

        try {
            if (!file.exists()) {
                throw new FileNotFoundException(file.getPath() + " does not exist");
            }

            jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("extension.yml");

            if (entry == null) { // The jar is missing a description
                throw new InvalidExtensionException(HPS.Localisation.getTranslation("extMissingDescription", file.getName()));
            }
            YamlConfiguration description = YamlConfiguration.loadConfiguration(new InputStreamReader(jar.getInputStream(entry)));

            URLClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() }, HPS.getHPSClassLoader());
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getString("main"), true, loader);
            } catch (ClassNotFoundException ex) {
                throw new InvalidExtensionException("Cannot find main class `" + description.getString("main") + "'", ex);
            }

            Class<? extends Extension> extensionClass;
            try {
                extensionClass = jarClass.asSubclass(Extension.class);
            } catch (ClassCastException ex) {
                throw new InvalidExtensionException("Cannot cast Extension to class `" + description.getString("main") + "'", ex);
            }

            Extension extension = extensionClass.newInstance();

            extension.init(loader, HPS, description);
            return extension;
        } catch (Exception e) {
            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errExtensionLoading", file.getName()));
            HPS.PM.debug(e);
        } finally {
            if (jar != null) {
                try {
                    jar.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public HarryPotterSpells getPlugin() {
        return HPS;
    }

    public File getDirectory() {
        return directory;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public List<URLClassLoader> getLoaders() {
        return loaders;
    }

    protected void clearLoaders() {
        this.loaders.clear();
    }

}
