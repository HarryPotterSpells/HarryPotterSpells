package com.hpspells.core.configuration;

import java.io.File;
import java.io.InputStream;

import com.hpspells.core.HarryPotterSpells;

public class SpellConfig extends CustomConfiguration {
	
	private static final String[] header = {
			"######################### #",
			"## Spell Configuration ## #",
			"######################### #",
			" ",
			"When configuring spells, seconds are assumed. If you'd like to specify ticks (1/20 of a second), append a 't' to the value.",
			"For example, 600t = 600 ticks = 30 seconds",
			"while 600 (without the t) = 600 seconds"
	};

	/**
     * Constructs a new {@link SpellConfig} without copying any defaults
     *
     * @param instance an instance of {@link HarryPotterSpells}
     * @param file     where to store the custom configuration
     */
	public SpellConfig(HarryPotterSpells instance, File file) {
		super(instance, file);
	}
	
	/**
     * Constructs a new {@link CustomConfiguration}, copying defaults from an {@link InputStream}
     *
     * @param instance an instance of {@link HarryPotterSpells}
     * @param file     where to store the custom configuration
     * @param stream   an input stream to copy default configuration from
     */
    public SpellConfig(HarryPotterSpells instance, File file, InputStream stream) {
        super(instance, file, stream, header);
    }

}
