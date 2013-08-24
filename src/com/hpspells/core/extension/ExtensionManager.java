package com.hpspells.core.extension;

import com.hpspells.core.HPS;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Handles management of all extensions
 */
public class ExtensionManager {
    private HPS HPS;
    private final File extensionFolder;
    private final Set<Extension> extensions = new TreeSet<Extension>(new Comparator<Extension>() {

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

                extensions.add(Extension.create(file, YamlConfiguration.loadConfiguration(zip.getInputStream(description))));
                zip.close();
            } catch (IOException e) {
                HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errExtensionLoading", file.getName()));
                HPS.PM.debug(e);
            }
        }
    }

    public void enableExtensions() {

    }

    public void disableExtensions() {

    }

}
