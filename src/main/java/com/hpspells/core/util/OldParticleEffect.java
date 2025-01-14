package com.hpspells.core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A utility class designed to send particle effects not available in the Bukkit API. <br>
 * This enum has been modified and optimised from the class found <a href="http://forums.bukkit.org/threads/particle-packet-library.138493/">here</a>.
 */
public enum OldParticleEffect {

    HUGE_EXPLOSION("hugeexplosion", 0),
    LARGE_EXPLODE("largeexplode", 1),
    FIREWORKS_SPARK("fireworksSpark", 2),
    BUBBLE("bubble", 3),
    SUSPEND("suspend", 4),
    DEPTH_SUSPEND("depthSuspend", 5),
    TOWN_AURA("townaura", 6),
    CRIT("crit", 7),
    MAGIC_CRIT("magicCrit", 8),
    MOB_SPELL("mobSpell", 9),
    MOB_SPELL_AMBIENT("mobSpellAmbient", 10),
    SPELL("spell", 11),
    INSTANT_SPELL("instantSpell", 12),
    WITCH_MAGIC("witchMagic", 13),
    NOTE("note", 14),
    PORTAL("portal", 15),
    ENCHANTMENT_TABLE("enchantmenttable", 16),
    EXPLODE("explode", 17),
    FLAME("flame", 18),
    LAVA("lava", 19),
    FOOTSTEP("footstep", 20),
    SPLASH("splash", 21),
    LARGE_SMOKE("largesmoke", 22),
    CLOUD("cloud", 23),
    RED_DUST("reddust", 24),
    SNOWBALL_POOF("snowballpoof", 25),
    DRIP_WATER("dripWater", 26),
    DRIP_LAVA("dripLava", 27),
    SNOW_SHOVEL("snowshovel", 28),
    SLIME("slime", 29),
    HEART("heart", 30),
    ANGRY_VILLAGER("angryVillager", 31),
    HAPPY_VILLAGER("happyVillager", 32),
    ICONCRACK("iconcrack", 33),
    TILECRACK("tilecrack", 34);

    private String name;
    private int id;

    OldParticleEffect(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    private static final Map<String, OldParticleEffect> NAME_MAP = new HashMap<String, OldParticleEffect>();
    private static final Map<Integer, OldParticleEffect> ID_MAP = new HashMap<Integer, OldParticleEffect>();

    static {
        for (OldParticleEffect effect : values()) {
            NAME_MAP.put(effect.name, effect);
            ID_MAP.put(effect.id, effect);
        }
    }

    /**
     * Gets a {@link OldParticleEffect} by it's name
     *
     * @param name the name
     * @return the particle effect or {@code null} if not found
     */
    public static OldParticleEffect fromName(String name) {
        if (name == null)
            return null;
        for (Entry<String, OldParticleEffect> e : NAME_MAP.entrySet()) {
            if (e.getKey().equalsIgnoreCase(name))
                return e.getValue();
        }
        return null;
    }

    /**
     * Gets a {@link OldParticleEffect} by it's id
     *
     * @param id the id
     * @return the particle effect or {@code null} if not found
     */
    public static OldParticleEffect fromId(int id) {
        return ID_MAP.get(id);
    }

    /**
     * Sends a particle effect to a player
     */
    public static void sendToPlayer(OldParticleEffect effect, Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        Object packet = createPacket(effect, location, offsetX, offsetY, offsetZ, speed, count);
        sendPacket(player, packet);
    }

    /**
     * Sends a particle effect to a location
     */
    public static void sendToLocation(OldParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        Object packet = createPacket(effect, location, offsetX, offsetY, offsetZ, speed, count);
        for (Player player : Bukkit.getOnlinePlayers())
            sendPacket(player, packet);
    }

    /**
     * Sends a crack effect to a player
     */
    public static void sendCrackToPlayer(boolean icon, int id, byte data, Player player, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        Object packet = createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        sendPacket(player, packet);
    }

    /**
     * Sends a crack effect to a location
     */
    public static void sendCrackToLocation(boolean icon, int id, byte data, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        Object packet = createCrackPacket(icon, id, data, location, offsetX, offsetY, offsetZ, count);
        for (Player player : Bukkit.getOnlinePlayers())
            sendPacket(player, packet);
    }

    /*
     * START PRIVATE UTILITIES
     */

    @SuppressWarnings("deprecation")
	private static Object createPacket(OldParticleEffect effect, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count) throws Exception {
        if (count <= 0)
            count = 1;
        Object packet = SVPBypass.getCurrentNMSClass("Packet63WorldParticles").newInstance();
        SVPBypass.setValue(packet, "a", effect.name);
        SVPBypass.setValue(packet, "b", (float) location.getX());
        SVPBypass.setValue(packet, "c", (float) location.getY());
        SVPBypass.setValue(packet, "d", (float) location.getZ());
        SVPBypass.setValue(packet, "e", offsetX);
        SVPBypass.setValue(packet, "f", offsetY);
        SVPBypass.setValue(packet, "g", offsetZ);
        SVPBypass.setValue(packet, "h", speed);
        SVPBypass.setValue(packet, "i", count);
        return packet;
    }

    @SuppressWarnings("deprecation")
	private static Object createCrackPacket(boolean icon, int id, byte data, Location location, float offsetX, float offsetY, float offsetZ, int count) throws Exception {
        if (count <= 0)
            count = 1;
        Object packet = SVPBypass.getCurrentNMSClass("Packet63WorldParticles").newInstance();
        String modifier = "iconcrack_" + id;
        if (!icon)
            modifier = "tilecrack_" + id + "_" + data;
        SVPBypass.setValue(packet, "a", modifier);
        SVPBypass.setValue(packet, "b", (float) location.getX());
        SVPBypass.setValue(packet, "c", (float) location.getY());
        SVPBypass.setValue(packet, "d", (float) location.getZ());
        SVPBypass.setValue(packet, "e", offsetX);
        SVPBypass.setValue(packet, "f", offsetY);
        SVPBypass.setValue(packet, "g", offsetZ);
        SVPBypass.setValue(packet, "h", 0.1F);
        SVPBypass.setValue(packet, "i", count);
        return packet;
    }

    private static Object getEntityPlayer(Player p) throws Exception {
        Method getHandle = p.getClass().getMethod("getHandle");
        return getHandle.invoke(p);
    }

    private static void sendPacket(Player p, Object packet) throws Exception {
        Object eplayer = getEntityPlayer(p);
        Field playerConnectionField = eplayer.getClass().getField("playerConnection");
        Object playerConnection = playerConnectionField.get(eplayer);
        for (Method m : playerConnection.getClass().getMethods()) {
            if (m.getName().equalsIgnoreCase("sendPacket")) {
                m.invoke(playerConnection, packet);
                return;
            }
        }
    }

}
