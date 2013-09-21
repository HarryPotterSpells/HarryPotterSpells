package com.hpspells.core.util.nbt;

import com.hpspells.core.util.SVPBypass;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

// HPS modifications: change static values class to SVPBypass, removed mcpc+ support, removed location config check, removed unused variables
public class NBTContainerBlock extends NBTContainer {

    private static final Class class_EntityPlayer = SVPBypass.getCurrentNMSClass("EntityPlayer");
    private static final Class class_CraftWorld = SVPBypass.getCurrentCBClass("CraftWorld");
    private static final Class class_TileEntity = SVPBypass.getCurrentNMSClass("TileEntity");
    private static final Class class_Packet = SVPBypass.getCurrentNMSClass("Packet");
    private static final Class class_sCraftPlayer = SVPBypass.getCurrentCBClass("CraftPlayer");
    private static final Class class_PlayerConnection  = SVPBypass.getCurrentNMSClass("PlayerConnection");
    private static Field field_playerConnection;
    private static Method method_sendPacket;
    private static Method method_getTileEntityAt;
    private static Method method_Read;
    private static Method method_Write;
    private static Method method_getUpdatePacket;
    private static Method method_getHandle;
    static{

        try { //both
            method_getUpdatePacket = SVPBypass.getMethodByTypeTypes(class_TileEntity, class_Packet);
            method_getTileEntityAt = SVPBypass.getMethodByTypeTypes(class_CraftWorld, class_TileEntity,int.class,int.class,int.class);
            method_getHandle = class_sCraftPlayer.getMethod("getHandle");
        } catch (NoSuchMethodException e){
            e.printStackTrace();
        }
        try{ // bukkit
            method_Read = class_TileEntity.getMethod("b", class_NBTTagCompound);
            method_Write = class_TileEntity.getMethod("a", class_NBTTagCompound);
            method_sendPacket = class_PlayerConnection.getMethod("sendPacket",class_Packet);
            field_playerConnection = class_EntityPlayer.getField("playerConnection");
        } catch (Exception ignored){
        }
    }


    Block block;

    public NBTContainerBlock(Block block) {
        this.block = block;
    }

    public Block getObject() {
        return block;
    }

    @Override
    public List<String> getTypes() {
        return Arrays.asList("block", "block_" + block.getType().name());
    }

    @Override
    public NBTTagCompound getCustomTag() {
        NBTTagCompound compound = getTag();
        if (compound==null) return null;
        return compound;
    }

    public NBTTagCompound getTag() {
        Object tile;
        NBTTagCompound base;
        base = new NBTTagCompound();
        try{
            tile = method_getTileEntityAt.invoke(block.getWorld(),block.getX(), block.getY(), block.getZ());
            method_Read.invoke(tile, base.getHandle());
        } catch (Exception ignored){
        }
        return base;
    }

    @Override
    public void setTag(NBTBase base) {
        if (!(base instanceof NBTTagCompound)) return;
        Object tile = null;
        try{
            tile = method_getTileEntityAt.invoke(block.getWorld(),block.getX(), block.getY(), block.getZ());
        } catch (Exception e){
            e.printStackTrace();
        }
        if (tile != null) {
            try{
                method_Write.invoke(tile,base.getHandle());
            } catch (Exception e){
                e.printStackTrace();
            }
            int maxDist = Bukkit.getServer().getViewDistance() * 32;
            for (Player p : block.getWorld().getPlayers()) {
                if (p.getLocation().distance(block.getLocation()) < maxDist) {
                    try{
                        Object packet = method_getUpdatePacket.invoke(tile);
                        Object mPlayer = method_getHandle.invoke(p);
                        Object connection = field_playerConnection.get(mPlayer);
                        method_sendPacket.invoke(connection,packet);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void setCustomTag(NBTBase base) {
        if (!(base instanceof NBTTagCompound)) return;
        NBTTagCompound tag = (NBTTagCompound) base.clone();
        NBTTagCompound original = getTag();
        tag.set("x", original.get("x"));
        tag.set("y", original.get("y"));
        tag.set("z", original.get("z"));
        setTag(tag);
    }

}
