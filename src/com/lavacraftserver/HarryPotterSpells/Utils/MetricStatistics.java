package com.lavacraftserver.HarryPotterSpells.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.API.SpellPostCastEvent;
import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class MetricStatistics implements EnableJob, Listener {
    private static int spellsCast = 0, successes = 0, failures = 0;
    private static Map<String, Integer> typesOfSpellCast = new HashMap<String, Integer>();
    
    @Override
    public void onEnable(PluginManager pm) {
        pm.registerEvents(this, HPS.Plugin);
    }
    
    public static int getSpellsCast() {
        return spellsCast;
    }
    
    public static int getAmountOfTimesCast(Spell spell) {
        return typesOfSpellCast.get(spell.getName()) == null ? 0 : typesOfSpellCast.get(spell.getName());
    }
    
    public static int getSuccesses() {
        return successes;
    }
    
    public static int getFailures() {
        return failures;
    }
    
    @EventHandler
    public void postSpellCast(SpellPostCastEvent e) {
        spellsCast++;
        typesOfSpellCast.put(e.getSpell().getName(), typesOfSpellCast.get(e.getSpell().getName()) == null ? 1 : typesOfSpellCast.get(e.getSpell().getName()) + 1);
        if(e.wasSuccessful())
            successes++;
        else
            failures++;
    }

}
