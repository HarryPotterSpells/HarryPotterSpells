package com.hpspells.core.spell;

import com.hpspells.core.HPS;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.util.MiscUtilities;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.logging.Level;

/**
 * An abstract class representing a Spell
 */
public abstract class Spell {
    public HPS HPS;

    public Spell(HPS instance) {
        this.HPS = instance;
    }

    /**
     * Called when a spell is cast.
     *
     * @param p the player who cast the spell
     * @return {@code true} if the spell was a success
     */
    public abstract boolean cast(Player p);

    /**
     * Teaches the spell to a target.
     *
     * @param sender the person who wants to teach
     * @param target the person who will be taught
     */
    public void teach(Player sender, Player target) {
        if (target != null) {
            if (playerKnows(target))
                HPS.PM.warn(sender, HPS.Localisation.getTranslation("cmdTeaKnowsThat", target.getName()));
            else {
                teach(target);
                HPS.PM.tell(sender, HPS.Localisation.getTranslation("cmdTeaTaughtOne", target.getName(), getName()));
            }
        } else
            HPS.PM.warn(sender, HPS.Localisation.getTranslation("cmdPlayerNotFound"));
    }

    /**
     * Teaches the spell to a player.
     *
     * @param p the player
     */
    public void teach(Player p) {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        List<String> list = psc.getStringListOrEmpty(p.getName());
        list.add(getName());
        psc.get().set(p.getName(), list);
        psc.save();
    }

    /**
     * Gets whether a player knows this spell.
     *
     * @param p the player
     * @return {@code true} if the player knows this spell
     */
    public boolean playerKnows(Player p) {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        List<String> list = psc.getStringListOrEmpty(p.getName());
        return list.contains(getName());
    }

    /**
     * Makes a player forget the spell.
     *
     * @param p the player
     */
    public void unTeach(Player p) {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        List<String> list = psc.getStringListOrEmpty(p.getName());
        list.remove(getName());
        psc.get().set(p.getName(), list);
        psc.save();
    }

    /**
     * Gets the permission required to teach the spell and cast the spell.
     * 
     * @return spell permission
     */
    public Permission getPermission() {
        return new Permission("harrypotterspells.spell." + this.getName().toLowerCase().replaceAll(" ", "-"), PermissionDefault.TRUE);
    }

    /**
     * Gets the name of this spell.
     *
     * @return the spell's name
     */
    public String getName() {
        SpellInfo info = this.getClass().getAnnotation(SpellInfo.class);
        return info == null ? this.getClass().getSimpleName() : info.name();
    }

    /**
     * Gets the description of this spell.
     *
     * @return the description
     */
    public String getDescription() {
        SpellInfo info = this.getClass().getAnnotation(SpellInfo.class);
        return info == null ? "" : HPS.Localisation.getTranslation(info.description());
    }

    /**
     * The annotation required for a spell to be used. <br>
     * This annotation contains all the information needed for using a spell.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface SpellInfo {
        String name() default ""; // "" defaults to class name

        String description() default "";

        int range() default 25;

        boolean goThroughWalls() default false;

        int cooldown() default 60;

        Material icon() default Material.STICK;
    }

    /**
     * Gets the icon for this spell.
     *
     * @return the icon as a {@link Material}
     */
    public Material getIcon() {
        SpellInfo info = this.getClass().getAnnotation(SpellInfo.class);
        return info == null ? Material.STICK : info.icon();
    }

    /**
     * Gets whether the spell can be cast through walls.
     *
     * @return {@code true} if the spel can be cast through walls.
     */
    public boolean canBeCastThroughWalls() {
        SpellInfo info = this.getClass().getAnnotation(SpellInfo.class);
        return info == null ? false : info.goThroughWalls();
    }

    /**
     * Gets the range of the spell.
     *
     * @return the range
     */
    public int getRange() {
        SpellInfo info = this.getClass().getAnnotation(SpellInfo.class);
        return info == null ? 25 : info.range();
    }

    /**
     * Gets the cool down of the spell for a player.
     *
     * @return the cool down
     */
    public int getCoolDown(Player p) {
        FileConfiguration cdConfig = HPS.ConfigurationManager.getConfig(ConfigurationType.COOLDOWN).get();
        SpellInfo info = this.getClass().getAnnotation(SpellInfo.class);
        if (info == null)
            return 60;
        
        String spellName = info.name().toLowerCase().replace(" ", "-");
        if (p.hasPermission(HPS.SpellManager.NO_COOLDOWN_ALL_1) || p.hasPermission(HPS.SpellManager.NO_COOLDOWN_ALL_2)
                || p.hasPermission("harrypotterspells.nocooldown." + spellName))
            return 0;

        int cooldown;
        if (cdConfig.contains("cooldowns." + spellName))
            cooldown = cdConfig.getInt("cooldowns." + spellName);
        else
            cooldown = info.cooldown();

        return cooldown == -1 ? info.cooldown() : cooldown;
    }

    /**
     * A utility method used to shorten the retrieval of something from the spells configuration
     * file.
     *
     * @param key the key to the value relative to {@code spells.[spell name].}
     * @param defaultt the nullable value to return if nothing was found
     * @return the object found at that location
     */
    public Object getConfig(String key, @Nullable Object defaultt) {
        FileConfiguration spellConfig = HPS.ConfigurationManager.getConfig(ConfigurationType.SPELL).get();
        key = "spells." + getName().toLowerCase().replace(" ", "-") + "." + key;
        return defaultt == null ? spellConfig.get(key) : spellConfig.get(key, defaultt);
    }

    /**
     * Gets a time from the spells configuration as formatted by the following table: <br>
     * Default: seconds <br>
     * {@code endsWith("t")}: ticks
     *
     * @param key the key to the value relative to {@code spells.[spellname].}
     * @param defaultt the nullable ticks to return if nothing was found
     * @return a {@code long} with the amount of ticks the time specified
     */
    public long getTime(String key, @Nullable long defaultt) {
        Object time = getConfig(key, "");
        if (time instanceof String) {
            String durationString = (String) time;
            if (durationString.equals("")) {
                return defaultt;
            }
            int duration = 0;
            if (durationString.endsWith("t")) {
                String ticks = durationString.substring(0, durationString.length() - 1);
                duration = Integer.parseInt(ticks);
            } else {
                duration = Integer.parseInt(durationString) * 20; // convert to ticks
            }
            return duration;
        }
        if (time instanceof Integer) {
            return ((Number) time).longValue() * 20; // convert to ticks
        }
        return defaultt;
    }
    
    /**
     * Will attempt to convert an Object to a double. Logs a warning if not able to.
     * Primarily used for {@link #getConfig(String, Object)}
     * 
     * @param toHandle The value to convert
     * @param defaultt The value to default to on error.
     * @return the double value of toHandle
     */
    public double handleDouble(Object toHandle, @Nullable double defaultt) {
        Double result = defaultt;
        try {
            result = MiscUtilities.handleDouble(toHandle);
        } catch (NumberFormatException e) {
            HPS.PM.log(Level.WARNING, "Error when handling double value for Spell: " + getName());
            HPS.PM.log(Level.WARNING, "Expected double but instead got: " + toHandle.toString());
        }
        return result;
    }

}
