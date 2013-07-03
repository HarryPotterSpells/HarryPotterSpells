package com.hpspells.core.data;

import java.util.SortedSet;

import com.hpspells.core.spell.Spell;

public class PlayerData {
    private SortedSet<Spell> knownSpells;

    public PlayerData(SortedSet<Spell> knownSpells) {
        this.knownSpells = knownSpells;
    }

    public SortedSet<Spell> getKnownSpells() {
        return this.knownSpells;
    }
}
