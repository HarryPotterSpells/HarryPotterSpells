package com.lavacraftserver.HarryPotterSpells.Utils;

import com.lavacraftserver.HarryPotterSpells.HPS;

/**
 * A class containing utilities used to bypass the SVP introduced by Bukkit. <br>
 * This class is an unwanted necessity.
 */
public class SVPBypass {
    
    /**
     * Gets the current package version used in the SVP. <br>
     * It returns the part in []: org.bukkit.craftbukkit.[1.5.1_R3].CraftServer
     * @return the current package version
     */
    public static String getCurrentPackageVersion() {
        return HPS.Plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
    
    /**
     * Gets the current version of the CraftBukkit class, bypassing the Bukkit SVP.
     * @param clazz the class relative to the root of the version. <br>
     *              E.G {@code entity.CraftArrow} will return the current class that contains the CraftArrow.
     * @return the class
     */
    public static Class<?> getCurrentCBClass(String clazz) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getCurrentPackageVersion() + "." + clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * Gets the current version of the NMS (net.minecraft.server) class, bypassing the Bukkit SVP.
     * @param clazz the class relative to the root of the version. <br>
     *              E.G {@code EnchantmentManager} will return the current class that contains the EntityManager.
     * @return the class
     */
    public static Class<?> getCurrentNMSClass(String clazz) {
        try {
            return Class.forName("net.minecraft.server." + getCurrentPackageVersion() + "." + clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
