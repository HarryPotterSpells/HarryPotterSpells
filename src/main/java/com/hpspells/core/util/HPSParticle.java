package com.hpspells.core.util;

import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;

/**
 * A particle container class for particles used in HPS. 
 * 
 * @author linjck
 *
 */
public class HPSParticle {
	
	private Particle particle;
	private DustOptions options;
	
	public HPSParticle(Particle particle) {
		this(particle, null);
	}
	
	public HPSParticle(Particle particle, DustOptions options) {
		this.particle = particle;
		this.options = options;
	}
	
	public Particle getParticle() {
		return particle;
	}
	
	public void setParticle(Particle particle) {
		this.particle = particle;
	}
	
	public DustOptions getOptions() {
		return options;
	}
	
	public void setOptions(DustOptions options) {
		this.options = options;
	}

}
