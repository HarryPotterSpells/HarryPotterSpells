package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// HPS modifications: removed old code, changed static values class
public class NBTContainerItem extends NBTContainer {

    ItemStack item;

    private static final Class class_CraftItemStack = SVPBypass.getCurrentCBClass("CraftItemStack");
    private static final Class class_ItemStack = SVPBypass.getCurrentNMSClass("ItemStack");
    private static final Class class_NBTTagCompound = SVPBypass.getCurrentNMSClass("NBTTagCompound");
    static Field field_Tag;
    static Field field_Handle;
    static Method method_getHandle;

    static {
        field_Tag = SVPBypass.getFieldByType(class_ItemStack,class_NBTTagCompound);
        field_Handle = SVPBypass.getFieldByType(class_CraftItemStack,class_ItemStack);
        method_getHandle = SVPBypass.getMethodByTypeTypes(class_CraftItemStack, class_ItemStack);
        if(field_Tag!=null) field_Tag.setAccessible(true);
        if(field_Handle!=null) field_Handle.setAccessible(true);
        if(method_getHandle!=null) method_getHandle.setAccessible(true);
    }

    public NBTContainerItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getObject() {
        return item;
    }

    @Override
    public List<String> getTypes() {
        int id = item.getTypeId();
        List<String> s = new ArrayList<String>();
        s.add("item");
        if (id == 387 || id == 386) s.add("item_book");
        else if (id >= 298 && id <= 301) s.add("item_leather");
        else if (id == 397) s.add("item_skull");
        else if (id == 403) s.add("item_enchbook");
        else if (id == 373) s.add("item_potion");
        else if (id == 401) s.add("item_rocket");
        else if (id == 402) s.add("item_firework");
        if ((id >= 267 && id <= 279)
                || (id >= 283 && id <= 286)
                || (id >= 290 && id <= 294)
                || (id >= 298 && id <= 317)
                || id == 261
                || id == 346
                || id == 359
                || id == 256
                || id == 257
                ) s.add("item_repair");
        return s;
    }

    @Override
    public NBTTagCompound getTag() {
        Object is = null;
        try {
            is = method_getHandle.invoke(item);
        } catch (Exception ignored) {
        }
        if (is == null) {
            try {
                is = field_Handle.get(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Object tag = null;
        try {
            tag = field_Tag.get(is);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (tag==null) return null;
        else return (NBTTagCompound) NBTBase.wrap(tag).clone();
    }

    @Override
    public void setTag(NBTBase base) {
        try {
            Object handle = getItemStackHandle();
            if(handle!=null) field_Tag.set(getItemStackHandle(), base.getHandle());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeTag() {
        try {
            field_Tag.set(getItemStackHandle(), null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Object getItemStackHandle() {
        Object is = null;
        try {
            is = method_getHandle.invoke(item);
        } catch (Exception ignored) {
        }
        if (is == null) {
            try {
                is = field_Handle.get(item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return is;
    }
}
