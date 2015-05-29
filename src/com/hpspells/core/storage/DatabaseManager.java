package com.hpspells.core.storage;

import java.sql.SQLException;
import java.util.logging.Level;

import com.hpspells.core.HPS;

public class DatabaseManager {
	
	private Database db;
	private MySQL mysql;
	private SQLite sqlite;
	
	private Database.StorageFormat storageFormat;
	
	public DatabaseManager(HPS HPS) {
		String dbFormat = HPS.getConfig().getString("storage.format", "yml");
    	storageFormat = Database.StorageFormat.get(dbFormat);
    	if (storageFormat == Database.StorageFormat.MYSQL) {
    		mysql = new MySQL(
    				HPS.getLogger(), 
    				"Establishing MySQL Connection...", 
    				HPS.getConfig().getString("storage.mysql.host", "localhost"), 
    				HPS.getConfig().getInt("storage.mysql.port", 3306), 
    				HPS.getConfig().getString("storage.mysql.user", ""), 
    				HPS.getConfig().getString("storage.mysql.pass", ""), 
    				HPS.getConfig().getString("storage.mysql.database", "minecraft")
    		);
    		if (mysql.open() == null) {
    			//TODO: Either stop plugin or change to yml storage
    			mysql.printErr("Unable to connect to MySQL database", true);
    		} else {
    			db = mysql;
    			mysql.printInfo("Established MySQL Connection");
    			try {
	    			if (!mysql.tableExists("hps_users")) {
	    				mysql.printInfo("Creating hps_users table");
	    				mysql.prepareStatement(
	    						"CREATE TABLE `hps_users` ("
	    							+ "`uuid` varchar(36),"
									+ "`playername` varchar(255),"
									+ "`spells` varchar(255),"
									+ "`currentpos` int(36) NULL,"
									+ " PRIMARY KEY (uuid) "
								+ ");"
						).executeQuery();
	    			}
	    			if (!mysql.tableExists("hps_spells")) {
	    				mysql.printInfo("Creating hps_spells table");
	    				mysql.prepareStatement(
	    						"CREATE TABLE `hps_spells` ("
	    							+ "`name` varchar(255),"
	    							+ "`PRIMARY KEY (name)`"
	    						+ ");"
	    				).executeQuery();
	    			}
    			} catch (SQLException e) {
					mysql.printErr("Unable to execute query 'CREATE TABLE' in DatabaseManager.java", true);
					HPS.PM.debug(e);
				}
    		}
    	} else if (storageFormat == Database.StorageFormat.SQLITE) {
    		sqlite = new SQLite(
    				HPS.getLogger(), 
    				"Establishing SQLite Connection.", 
    				"database.db", 
    				HPS.getDataFolder().getAbsolutePath()
    		);
    		if (sqlite.open() == null) {
    			//TODO: Either stop plugin or change to yml storage
    			sqlite.printErr("Unable to connect to MySQL database", true);
    		} else {
    			db = sqlite;
    			sqlite.printInfo("Established MySQL Connection");
    			try {
    				if (!sqlite.tableExists("hps_users")) {
        				sqlite.printInfo("Creating hps_users table");
        				sqlite.prepareStatement(
        						"CREATE TABLE `hps_users` ("
    								+ "`uuid` TEXT(36) PRIMARY KEY,"
    								+ "`playername` TEXT(255),"
    								+ "`spells` TEXT(255),"
    								+ "`currentpos` INTEGER"
    							+ ");"
   						).executeQuery();
        			}
    				if (!mysql.tableExists("hps_spells")) {
	    				mysql.printInfo("Creating hps_spells table");
	    				mysql.prepareStatement(
	    						"CREATE TABLE `hps_spells` ("
	    							+ "`name` TEXT(255) PRIMARY KEY"
	    						+ ");"
	    				).executeQuery();
	    			}
				} catch (SQLException e) {
					sqlite.printErr("Unable to create table in database.db", true);
					HPS.PM.debug(e);
				}
    		}
    	} else {
    		HPS.PM.log(Level.INFO, "Using yml as storage format");
    	}
	}
	
	public Database.StorageFormat getStorageFormat() {
		return storageFormat;
	}
	
	public Database getDatabase() {
		return db;
	}
	
	public MySQL getMySQL() {
		return mysql;
	}
	
	public SQLite getSQLite() {
		return sqlite;
	}

}
