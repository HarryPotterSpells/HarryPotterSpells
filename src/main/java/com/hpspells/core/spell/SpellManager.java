package com.hpspells.core.spell;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.google.common.collect.Iterables;
import com.hpspells.core.HarryPotterSpells;
import com.hpspells.core.api.event.SpellEvent;
import com.hpspells.core.api.event.SpellPostCastEvent;
import com.hpspells.core.api.event.SpellPreCastEvent;
import com.hpspells.core.configuration.ConfigurationManager.ConfigurationType;
import com.hpspells.core.configuration.PlayerSpellConfig;
import com.hpspells.core.spell.Spell.SpellInfo;
import com.hpspells.core.util.ReflectionsReplacement;

/**
 * A class that manages spells and holds lots of spell related utilities
 */
public class SpellManager {
    private HashMap<String, HashMap<Spell, Long>> cooldowns = new HashMap<String, HashMap<Spell, Long>>();
    private Comparator<Spell> spellComparator = new Comparator<Spell>() {

        @Override
        public int compare(Spell o1, Spell o2) {
            return o1.getName().compareTo(o2.getName());
        }

    };
    private SortedSet<Spell> spellList = new TreeSet<Spell>(spellComparator);
    private Map<String, Integer> currentSpell = new HashMap<String, Integer>();
    private HarryPotterSpells HPS;

    public final Permission NO_COOLDOWN_ALL_1 = new Permission("harrypotterspells.nocooldown", PermissionDefault.OP), NO_COOLDOWN_ALL_2 = new Permission("harrypotterspells.nocooldown.*");

    /**
     * Constructs the {@link SpellManager}, adding all core spells to the Spell List
     *
     * @param plugin an instance of {@link HarryPotterSpells}
     */
    public SpellManager(HarryPotterSpells instance) {
        this.HPS = instance;
        HPS.getServer().getPluginManager().addPermission(NO_COOLDOWN_ALL_1);
        HPS.getServer().getPluginManager().addPermission(NO_COOLDOWN_ALL_2);

        try {
            for (Class<?> clazz : ReflectionsReplacement.getSubtypesOf(Spell.class, "com.hpspells.core.spell", HPS.getHPSClassLoader(), Spell.class)) {
                if (clazz.getAnnotation(SpellInfo.class) == null)
                    continue;

                Spell spell;
                try {
                    spell = (Spell) clazz.getConstructor(HarryPotterSpells.class).newInstance(HPS);

                    if (Listener.class.isAssignableFrom(clazz)) {
                        HPS.getServer().getPluginManager().registerEvents((Listener) spell, HPS);
                    }
                    
                    HPS.getServer().getPluginManager().addPermission(spell.getPermission());

                } catch (Exception e) {
                    HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errSpells", clazz.getSimpleName()));
                    HPS.PM.debug(e);
                    continue;
                }
                spellList.add(spell);
            }
        } catch (Exception e) {
            HPS.PM.log(Level.WARNING, HPS.Localisation.getTranslation("errReflectionsReplacementSpelll"));
            HPS.PM.debug(e);
        }
    }

    /**
     * Gets the current spell position a player is on
     *
     * @param player the player
     * @return the current spell position they are on
     */
    public Integer getCurrentSpellPosition(Player player) {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        List<String> spellsTheyKnow = psc.getStringListOrEmpty(player.getName());

        if (spellsTheyKnow.isEmpty())
            return null;

        if (!currentSpell.containsKey(player.getName()))
            return 0;

        return currentSpell.get(player.getName());
    }

    /**
     * Gets the current spell a player is on
     *
     * @param player the player
     * @return the current spell they are on
     */
    public Spell getCurrentSpell(Player player) {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        Integer cur = getCurrentSpellPosition(player);
        List<String> spells = psc.getStringListOrEmpty(player.getName());
        if (spells.isEmpty())
            return null;
        return cur == null ? null : getSpell(Iterables.get(new TreeSet<String>(spells), cur.intValue()));
    }

    /**
     * Sets the current spell position a player is on
     *
     * @param player the player
     * @param id     the new current spell position the player is on
     * @return the spell they have changed to
     * @throws IllegalArgumentException if the id parameter is invalid
     */
    public Spell setCurrentSpellPosition(Player player, int id) throws IllegalArgumentException {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        List<String> spellsTheyKnow = psc.getStringListOrEmpty(player.getName());
        if (spellsTheyKnow == null || id >= spellsTheyKnow.size() || id < 0)
            throw new IllegalArgumentException("id was invalid");
        currentSpell.put(player.getName(), id);
        return getCurrentSpell(player);
    }

    /**
     * Sets the current spell a player is on
     *
     * @param player the player
     * @param spell  the new spell the player is on
     * @return the spell they have changed to for chaining
     * @throws IllegalArgumentException if the spell parameter is invalid
     */
    public Spell setCurrentSpell(Player player, Spell spell) throws IllegalArgumentException {
        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);
        Integer spellIndex = getIndex(new TreeSet<String>(psc.getStringListOrEmpty(player.getName())), spell.getName());
        if (spellIndex == null)
            throw new IllegalArgumentException("player does not know that spell");
        setCurrentSpellPosition(player, spellIndex);
        return getCurrentSpell(player);
    }

    /**
     * Gets a spell by name
     *
     * @param name the spell to get
     * @return the spell or {@code null} if not found
     */
    public Spell getSpell(String name) {
        for (Spell spell : spellList)
            if (spell.getName().equalsIgnoreCase(name))
                return spell;
        return null;
    }

    /**
     * Adds a spell to the spell list
     *
     * @param spell the spell
     */
    public void addSpell(Spell spell) {
        spellList.add(spell);
    }

    /**
     * Gets a list of all spells <br>
     * <b>Note:</b> this returns a {@link SortedSet}. If you do not need to use
     * a sorted set then cast it as a {@link Set}!
     *
     * @return the list
     */
    public SortedSet<Spell> getSpells() {
        return spellList;
    }

    /**
     * Checks if a string corrosponds to a spell
     *
     * @param name the name to test
     * @return {@code true} if the spell exists
     */
    public boolean isSpell(String name) {
        return getSpell(name) != null;
    }

    /**
     * Casts a spell cleverly. Checks permissions, triggering {@link SpellEvent}'s , sending effects and applying cooldown.
     * 
     * Note: Does not check if player has a wand in their inventory
     *
     * @param player the player who is casting
     * @param spell  the spell that they are casting
     */
    public void cleverCast(Player player, Spell spell) {
        if (!player.hasPermission("harrypotterspells.cast") || !spell.playerKnows(player))
            return;

        PlayerSpellConfig psc = (PlayerSpellConfig) HPS.ConfigurationManager.getConfig(ConfigurationType.PLAYER_SPELL);

        List<String> spellList = psc.getStringListOrEmpty(player.getName());
        if (spellList == null || spellList.isEmpty()) {
            HPS.PM.tell(player, "You don't know any spells.");
            return;
        }

        SpellPreCastEvent sce = new SpellPreCastEvent(spell, player);
        Bukkit.getServer().getPluginManager().callEvent(sce);
        if (!sce.isCancelled()) {
            Boolean cast = true;
            String playerName = player.getName();
            if (needsCooldown(playerName, spell)) {
                HPS.PM.dependantMessagingTell((CommandSender) player, HPS.Localisation.getTranslation("cldWait", spell.getCoolDown(player) - ((System.currentTimeMillis() - cooldowns.get(playerName).get(spell)) / 1000)).toString());
                cast = false;
            }
            boolean successful = false;
            if (cast) {
                successful = spell.cast(player);
                if (HPS.getConfig().getBoolean("spell-particle-toggle")) {
                    Location l = player.getLocation();
                    l.setY(l.getY() + 1);
                    player.getWorld().playEffect(l, Effect.ENDER_SIGNAL, 0);
                }
            }
            Bukkit.getServer().getPluginManager().callEvent(new SpellPostCastEvent(spell, player, successful));

            if (cast && successful && spell.getCoolDown(player) > 0) {
                setCoolDown(playerName, spell);
            }
        }
    }

    /**
     * Gets if a cooldown is needed by a player for a certain spell.
     *
     * @param playerName The name of the player you're checking if needs a cooldown
     * @param spell      The spell you're checking for
     * @return
     */
    public boolean needsCooldown(String playerName, Spell spell) {
        if (cooldowns.containsKey(playerName) && cooldowns.get(playerName).containsKey(spell)) {
            long currentTime = System.currentTimeMillis();
            long lastTime = cooldowns.get(playerName).get(spell);
            long difference = (currentTime - lastTime) / 1000;
            return difference < spell.getCoolDown(Bukkit.getPlayer(playerName));
        }
        return false;
    }

    /**
     * Sets the cooldown of a certain spell for a certain player for right now
     *
     * @param playerName name of the player you're setting a cooldown for
     * @param spell      the spell you're setting the cooldown for
     */
    public void setCoolDown(String playerName, Spell spell) {
        setCooldown(playerName, spell, null);
    }

    /**
     * @param playerName         The name of the player you're setting a cooldown for
     * @param spell              The spell you're setting the cooldown for
     * @param timeInMilliseconds The amount of additional time you wish for the cooldown to last, can be negative.
     */
    public void setCooldown(String playerName, Spell spell, @Nullable Long timeInMilliseconds) {
        if (timeInMilliseconds == null) {
            timeInMilliseconds = 0L;
        }
        if (cooldowns.containsKey(playerName)) {
            cooldowns.get(playerName).put(spell, System.currentTimeMillis() + timeInMilliseconds);
        } else {
            cooldowns.put(playerName, new HashMap<Spell, Long>());
            cooldowns.get(playerName).put(spell, System.currentTimeMillis() + timeInMilliseconds);
        }
    }

	/*
     * START PRIVATE UTILITIES
	 */

    private Integer getIndex(Set<? extends Object> set, Object value) {
        int result = 0;
        for (Object entry : set) {
            if (entry.equals(value))
                return result;
            result++;
        }
        return null;
    }

}
