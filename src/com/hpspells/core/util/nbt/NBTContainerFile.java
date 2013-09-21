package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// HPS modifications: changed static values call, changed stack trace printing, removed old code
public class NBTContainerFile extends NBTContainer {
    File file;
    protected static Method method_NBTRead;
    protected static Method method_NBTWrite;
    static{
        try{
            method_NBTRead = SVPBypass.getMethodByTypeTypes(class_NBTBase, class_NBTBase, DataInput.class);
            method_NBTWrite = SVPBypass.getMethodByTypeTypes(class_NBTBase,void.class,class_NBTBase,DataOutput.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public NBTContainerFile(File file) {
        this.file = file;
    }

    public File getObject() {
        return file;
    }

    @Override
    public NBTBase getTag() {
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(file));
            Object mBase = method_NBTRead.invoke(null,input);
            input.close();
            return NBTBase.wrap(mBase);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setTag(NBTBase base) {
        try {
            if (!file.exists()) {
                new File(file.getParent()).mkdirs();
                file.createNewFile();
            }
            DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
            method_NBTWrite.invoke(null,base.getHandle(), output);
            output.close();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public List<String> getTypes() {
        return new ArrayList<String>();
    }

    @Override
    public void removeTag() {
        file.delete();
    }
}
