package com.hpspells.core.configuration;

import java.io.File;
import java.io.InputStream;

import com.hpspells.core.HPS;

public class CooldownConfig extends CustomConfiguration {
	
	private static final String[] header = {
		"############################ #",
		"## Cooldown Configuration ## #",
		"############################ #",
		" ",
		"Do not use 0 for no cooldown. Use -1 instead.",
		"All cooldown values are in seconds. DO NOT USE 't'.",
		"Give the permission node 'HarryPotterSpells.nocooldown to bypass cooldowns."
	};

	/**
     * Constructs a new {@link CooldownConfig} without copying any defaults
     *
     * @param instance an instance of {@link HPS}
     * @param file     where to store the custom configuration
     */
	public CooldownConfig(HPS instance, File file) {
		super(instance, file);
	}
	
	/**
     * Constructs a new {@link CustomConfiguration}, copying defaults from an {@link InputStream}
     *
     * @param instance an instance of {@link HPS}
     * @param file     where to store the custom configuration
     * @param stream   an input stream to copy default configuration from
     */
	public CooldownConfig(HPS instance, File file, InputStream stream) {
		super(instance, file, stream, header);
	}
	
}
