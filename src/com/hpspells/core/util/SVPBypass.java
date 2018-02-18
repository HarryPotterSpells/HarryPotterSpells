package com.hpspells.core.util;

import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A class containing utilities used to bypass the SVP introduced by Bukkit. <br>
 * This class is an unwanted necessity.
 */
public class SVPBypass {

    /**
     * Gets the current package version used in the SVP. <br>
     * It returns the part in []: org.bukkit.craftbukkit.[1.5.1_R3].CraftServer
     *
     * @return the current package version
     */
    public static String getCurrentPackageVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    /**
     * Gets the current version of the CraftBukkit class, bypassing the Bukkit SVP.
     *
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
     *
     * @param clazz the class relative to the root of the version. <br>
     *              E.G {@code EnchantmentManager} will return the current class that contains the EnchantmentManager.
     * @return the class
     */
    public static Class<?> getCurrentNMSClass(String clazz) {
        try {
            return Class.forName("net.minecraft.server." + getCurrentPackageVersion() + "." + clazz);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Sets a value of an {@link Object} via reflection
     *
     * @param instance  instance the class to use
     * @param fieldName the name of the {@link Field} to modify
     * @param value     the value to set
     * @throws Exception if an error occurred during reflections
     */
    public static void setValue(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * Get a value of an {@link Object}'s {@link Field}
     *
     * @param instance  the target {@link Object}
     * @param fieldName name of the {@link Field}
     * @return the value of {@link Object} instance's {@link Field} with the name of fieldName
     * @throws Exception if an error occurred during reflections
     */
    public static Object getValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Utility function to get a {@link Method} from a class.
     *
     * @param cl     the class to get the method from
     * @param method the name of the method
     * @return the method retrieved by the function
     */
    public static Method getMethod(Class<?> cl, String method) {
        for (Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Gets a method by type types
     *
     * @param clazz the class
     * @param type the type
     * @param paramTypes any param types
     *
     * @return the method
     */
    public static Method getMethodByTypeTypes(Class<?> clazz, Class<?> type, Class<?>... paramTypes) {
        w1: for(Method m:clazz.getDeclaredMethods()){
            if(!m.getReturnType().equals(type)) continue;
            if(m.getParameterTypes().length!=paramTypes.length) continue;
            for(int i=0;i<paramTypes.length;i++) {
                if (!m.getParameterTypes()[i].equals(paramTypes[i])) {
                    continue w1;
                }
            }
            return m;
        }
        w1: for(Method m:clazz.getMethods()){
            if(!m.getReturnType().equals(type)) continue;
            if(m.getParameterTypes().length!=paramTypes.length) continue;
            for(int i=0;i<paramTypes.length;i++) {
                if (!m.getParameterTypes()[i].equals(paramTypes[i])) {
                    continue w1;
                }
            }
            return m;
        }
        return null;
    }

    /**
     * Gets a field by the type
     *
     * @param clazz the class
     * @param type the type
     *
     * @return the field
     */
    public static Field getFieldByType(Class<?> clazz,Class<?> type) {
        for(Field f:clazz.getDeclaredFields()){
            if (f.getType().equals(type)) return f;
        }
        for(Field f:clazz.getFields()){
            if (f.getType().equals(type)) return f;
        }
        return null;
    }

}
