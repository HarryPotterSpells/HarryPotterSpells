package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;

import java.lang.reflect.Field;


/**
 * 14.01.13 17:54
 *
 * @author DPOH-VAR
 */
// HPS modifications: changed static values class
public class NBTTagDouble extends NBTTagNumeric {
    private static Class clazz = SVPBypass.getCurrentNMSClass("NBTTagDouble");
    private static Field fieldData;

    static {
        try {
            fieldData = SVPBypass.getFieldByType(clazz, double.class);
            fieldData.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NBTTagDouble() {
        this("", (double) 0);
    }

    public NBTTagDouble(String s) {
        this(s, (double) 0);
    }

    public NBTTagDouble(double b) {
        this("", b);
    }

    public NBTTagDouble(String s, double b) {
        super(getNew(s, b));
    }

    private static Object getNew(String s, double b) {
        try{
            return clazz.getConstructor(String.class,double.class).newInstance(s,b);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public NBTTagDouble(boolean ignored, Object tag) {
        super(tag);
        if (!clazz.isInstance(tag)) throw new IllegalArgumentException();
    }

    public boolean equals(Object o) {
        if (o instanceof NBTBase) o = ((NBTBase) o).getHandle();
        return handle.equals(o);
    }

    public int hashCode() {
        return handle.hashCode();
    }

    public Double get() {
        try {
            return (Double) fieldData.get(handle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(Number value) {
        try {
            fieldData.set(handle, value.doubleValue());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte getTypeId() {
        return 6;
    }

}
