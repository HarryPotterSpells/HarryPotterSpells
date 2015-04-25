package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 14.01.13 17:54
 *
 * @author DPOH-VAR
 */
// HPS modifications: changed static values class, removed unused variables
public class NBTTagByte extends NBTTagNumeric {
    private static Class clazz = SVPBypass.getCurrentNMSClass("NBTTagByte");
    private static Field fieldData;
    private static Method methodClone;

    static {
        try {
            methodClone = SVPBypass.getMethodByTypeTypes(class_NBTBase, NBTBase.class_NBTBase);
            fieldData = SVPBypass.getFieldByType(clazz, byte.class);
            methodClone.setAccessible(true);
            fieldData.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NBTTagByte() {
        this("", (byte) 0);
    }

    public NBTTagByte(String s) {
        this(s, (byte) 0);
    }

    public NBTTagByte(byte b) {
        this("", b);
    }

    public NBTTagByte(String s, byte b) {
        super(getNew(s, b));
    }

    private static Object getNew(String s, byte b) {
        try{
            return clazz.getConstructor(String.class,byte.class).newInstance(s,b);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public NBTTagByte(boolean ignored, Object tag) {
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

    public Byte get() {
        try {
            return (Byte) fieldData.get(handle);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(Number value) {
        try {
            fieldData.set(handle, value.byteValue());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte getTypeId() {
        return 1;
    }

}
