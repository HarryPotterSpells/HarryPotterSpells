package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// HPS modifications: changed static values calls, modified stack trace printing, removed old code
public class NBTContainerFileGZip extends NBTContainer {

    File file;
    private static final Class class_NBTCompressedStreamTools = SVPBypass.getCurrentNMSClass("NBTCompressedStreamTools");
    private static final Class class_NBTTagCompound = SVPBypass.getCurrentNMSClass("NBTTagCompound");
    private static Method method_read;
    private static Method method_write;
    static{
        try {
            method_read = SVPBypass.getMethodByTypeTypes(class_NBTCompressedStreamTools,class_NBTTagCompound,InputStream.class);
            method_write = SVPBypass.getMethodByTypeTypes(class_NBTCompressedStreamTools,void.class,class_NBTCompressedStreamTools,OutputStream.class);
        }catch (Throwable e){
            throw new RuntimeException("NBTContainerFileGZip can not init", e);
        }
    }

    public NBTContainerFileGZip(File file) {
        this.file = file;
    }

    public File getObject() {
        return file;
    }

    @Override
    public List<String> getTypes() {
        return new ArrayList<String>();
    }

    @Override
    public NBTBase getTag() {
        try {
            FileInputStream input = new FileInputStream(file);
            Object tag = method_read.invoke(null,input);
            input.close();
            return NBTBase.wrap(tag);
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("IO error", e);
        }
    }

    @Override
    public void setTag(NBTBase base) {
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            FileOutputStream output = new FileOutputStream(file);
            method_write.invoke(null,base.getHandle(),output);
            output.close();
        }catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void removeTag() {
        file.delete();
    }
}
