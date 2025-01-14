package com.hpspells.core.extension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import com.hpspells.core.HarryPotterSpells;

/**
 * Represents an Extension to HarryPotterSpells
 */
public class Extension implements Listener {
    private HarryPotterSpells HPS;
    private String name, description, authors, version;
    private Logger logger;
    private File dataFolder, configFile;
    private FileConfiguration config = null;
    private ClassLoader classLoader;

    /**
     * Initiates the extension and provides the classloader, HPS instance, and the extension config.
     * 
     * @param loader The classloader that loaded this extension
     * @param instance The HPS instance
     * @param description The extension description file
     */
    void init(URLClassLoader loader, HarryPotterSpells instance, YamlConfiguration description) throws IOException {
        this.classLoader = loader;
        this.HPS = instance;
        this.name = description.getString("name");
        this.description = description.getString("description");
        this.authors = description.getString("authors");
        this.version = description.getString("version");
        
        if(this.name == null || this.version == null) {
        	this.HPS.getLogger().severe("Extension name cannot be null");
        	return;
        }
        
        this.logger = new ExtensionLogger(this);
        this.logger.setParent(HPS.getLogger());
        this.dataFolder = getDataFolder();
        this.configFile = new File(dataFolder, "config.yml");
        if (configFile.exists()) {
            YamlConfiguration.loadConfiguration(configFile);
        }
        // logger.info("Classloader: " + this.getClass().getClassLoader()); //java.net.FactoryURLClassLoader
        // logger.info("Classloader: " + Extension.class.getClassLoader()); //org.bukkit.plugin.java.PluginClassLoader
    }

    // Functions to be overridden by developers
    /**
     * Called when an extension is enabled.
     * 
     * Extension developers should override this method for enable tasks.
     */
    public void onEnable() {
        logger.info("Has been enabled");
    }

    /**
     * Called when an extension is disabled.
     * 
     * Extension developers should override this method for disable tasks.
     */
    public void onDisable() {
        logger.info("Has been disabled");
    }

    /**
     * Used to register listener class if the main class is not the listener.
     * 
     * @param listener Class to register
     */
    public void registerListener(Listener listener) {
        HPS.getServer().getPluginManager().registerEvents(listener, HPS);
    }

    /**
     * Gets the name of this {@link Extension}
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of this {@link Extension}
     * 
     * @return the description
     */
    public String getDescription() {
        return description == null ? "" : description;
    }

    /**
     * Gets the authors of this {@link Extension}
     * 
     * @return the authors
     */
    public String getAuthors() {
        return authors == null ? "" : authors;
    }

    /**
     * Gets the version string of this {@link Extension}
     * 
     * @return the version string
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets the data folder for this {@link Extension}, creating it if it does not exist
     * 
     * @return the data folder
     */
    public File getDataFolder() {
        if (dataFolder != null)
            return dataFolder;

        File file = new File(HPS.ExtensionManager.getExtensionFolder(), getName());

        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    /**
     * Gets the {@link FileConfiguration} for this {@link Extension}, creating it if it does not exist
     * 
     * @return the config
     */
    public FileConfiguration getConfig() {
        if (config == null) {
            saveDefaultConfig();
            reloadConfig();
        }
        return config;
    }

    /**
     * Reloads the config from the file system
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Saves the config retrieved from {@link #getConfig()}
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("extCouldNotSaveConfig", getName()));
            e.printStackTrace();
        }
    }

    /**
     * Saves the default config from the extension to the file system if it doesn't exist
     */
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            try {
                saveResource("config.yml", false);
            } catch (IOException e) {
                HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("extCouldNotCreateConfig", getName()));
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves a resource specified from the project to the datafolder of the extension
     * 
     * @param resourcePath The path to the resource
     * @param replace Whether or not to replace the existing file
     * @throws IOException
     */
    public void saveResource(String resourcePath, boolean replace) throws IOException {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        InputStream in = getResource(resourcePath);
        if (in == null) {
            throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found");
        }

        File outFile = new File(dataFolder, resourcePath);
        int lastIndex = resourcePath.lastIndexOf('/');
        File outDir = new File(dataFolder, resourcePath.substring(0, lastIndex >= 0 ? lastIndex : 0));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            } else {
                logger.log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, ex);
        }
    }

    /**
     * Gets a specified resource from the classloader within the Extension.
     * 
     * @param filename The name of the file to get
     * @return The InputStream of the opened file
     * @throws IOException
     */
    @Nullable
    public InputStream getResource(String filename) throws IOException {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        URL url = ((URLClassLoader) this.getClassLoader()).findResource(filename);
        // URL url = this.getClassLoader().getResource(filename);

        if (url == null) {
            return null;
        }
        HPS.PM.debug("Resource URL: " + url.toString());

        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection.getInputStream();
    }

    /**
     * Gets the {@link State} of this {@link Extension}
     * 
     * @return the state
     */
    public State getState() {
        return HPS.ExtensionManager.getState(this);
    }

    /**
     * Gets the {@link Logger} relevant to the class name.
     * 
     * @return logger for the extension
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Gets the ClassLoader that loaded the extension.
     * 
     * @return the extensions classloader
     */
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * An enum that represents the states an {@link Extension} can be in
     */
    public enum State {
        LOADED, ENABLED, DISABLED
    }

}
