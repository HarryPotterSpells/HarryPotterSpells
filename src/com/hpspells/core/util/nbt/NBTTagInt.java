package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;

import java.lang.reflect.Field;

/**
 * 14.01.13 17:54
 *
 * @author DPOH-VAR
 */
// HPS modifications: changed static values, removed unnecessary casts
public class NBTTagInt extends NBTTagNumeric {
    private static Class clazz = SVPBypass.getCurrentNMSClass("NBTTagInt");
    private static Field fieldData;

    static {
        try {
            fieldData = SVPBypass.getFieldByType(clazz, int.class);
            fieldData.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NBTTagInt() {
        this("", 0);
    }

    public NBTTagInt(String s) {
        this(s, 0);
    }

    public NBTTagInt(int b) {
        this("", b);
    }

    public NBTTagInt(String s, int b) {
        super(getNew(s, b));
    }

    private static Object getNew(String s, int b) {
        try{
            return clazz.getConstructor(String.class,int.class).newInstance(s,b);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public NBTTagInt(boolean ignored, Object tag) {
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

    public Integer get() {
        try {
            return (Integer) fieldData.get(handle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void set(Number value) {
        try {
            fieldData.set(handle, value.intValue());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte getTypeId() {
        return 3;
    }

}
