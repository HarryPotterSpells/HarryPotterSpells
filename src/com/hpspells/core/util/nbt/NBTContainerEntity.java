package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

// HPS modifications: changed static values class, removed mcpc+ support, modified stack trace printing, removed config checks, removed unused variables, removed old code
public class NBTContainerEntity extends NBTContainer {
    private static final Class classNBTTagCompound = SVPBypass.getCurrentNMSClass("NBTTagCompound");
    private static final Class classEntityTypes = SVPBypass.getCurrentNMSClass("EntityTypes");
    private static final Class classWorld = SVPBypass.getCurrentNMSClass("World");
    private static final Class classCraftWorld = SVPBypass.getCurrentCBClass("CraftWorld");
    private static final Class classEntity = SVPBypass.getCurrentNMSClass("Entity");
    private static final Class classCraftEntity = SVPBypass.getCurrentCBClass("CraftEntity");
    private static final Class classEntityPlayer = SVPBypass.getCurrentNMSClass("EntityPlayer");

    Entity entity;
    static Method method_createEntityFromNBT;
    static Method method_getHandleEntity;
    static Method method_getHandleWorld;
    static Method method_setPassengerOf;
    static Method method_WriteEntity;
    static ArrayList<Method> method_ReadPlayerList = new ArrayList<Method>();
    static Method method_ReadEntity;
    static Method method_ReadPlayer;
    static {
        try{
            method_setPassengerOf = SVPBypass.getMethodByTypeTypes(classEntity,void.class,classEntity);
            method_getHandleEntity = SVPBypass.getMethodByTypeTypes(classCraftEntity,classEntity);
            method_getHandleWorld = SVPBypass.getMethodByTypeTypes(classCraftWorld,classWorld);

            for(Method m:classEntity.getDeclaredMethods()){
                if (m.getParameterTypes().length!=1) continue;
                if (!m.getParameterTypes()[0].equals(classNBTTagCompound)) continue;
                if (m.getName().endsWith("c")) method_ReadEntity = m;
                if (m.getName().endsWith("f")) method_WriteEntity = m;
                if (m.getName().endsWith("e")) method_ReadPlayer = m;
                method_ReadPlayerList.add(m);
            }
            method_createEntityFromNBT = classEntityTypes.getDeclaredMethod("a",classNBTTagCompound,classWorld);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public NBTContainerEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getObject() {
        return entity;
    }

    @Override
    public List<String> getTypes() {
        List<String> s = new ArrayList<String>();
        s.add("entity");
        if (entity instanceof LivingEntity) s.add("living");
        s.add(entity.getType().getName());
        return s;
    }

    @Override
    public NBTTagCompound getTag() {
        NBTTagCompound base = new NBTTagCompound();
        try{
            Object liv = method_getHandleEntity.invoke(entity);

            if (classEntityPlayer.isInstance(liv)){
                method_ReadPlayer.invoke(liv,base.getHandle());
            } else {
                method_ReadEntity.invoke(liv,base.getHandle());
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return base;
    }

    @Override
    public NBTTagCompound getCustomTag() {
        NBTTagCompound tag = getTag();
        if (tag!=null) return tag;
        return tag;
    }

    @Override
    public void setTag(NBTBase base) {
        try {
            Object liv = method_getHandleEntity.invoke(entity);
            method_WriteEntity.invoke(liv, base.clone().getHandle());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setCustomTag(NBTBase base){
        if (!(base instanceof NBTTagCompound)) return;
        NBTTagCompound tag = (NBTTagCompound)base.clone();
        try {
            spawnEntity(tag,entity.getWorld(),entity);
        } catch (Exception e){
            e.printStackTrace();
        }
        setTag(tag);
    }

    public static Object spawnEntity(NBTTagCompound compound,World world,Object ridable){
        try {
            Object cworld = method_getHandleWorld.invoke(world);
            Object entity;
            entity = method_createEntityFromNBT.invoke(null,compound.getHandle(),cworld);
            if (ridable instanceof Entity) {
                ridable = method_getHandleEntity.invoke(ridable);
            }
            if (ridable != null) method_setPassengerOf.invoke(ridable,entity);
            NBTTagCompound compoundRiding = compound.getCompound("Riding");
            if(compoundRiding!=null) {
                spawnEntity(compoundRiding,world,entity);
            }
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
