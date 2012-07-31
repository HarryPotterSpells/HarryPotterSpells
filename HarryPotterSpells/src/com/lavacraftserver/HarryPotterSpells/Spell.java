package com.lavacraftserver.HarryPotterSpells;

public enum Spell {
	//Harry Potter Spells
	AGUAMENTI("Water"),
	INCENDIO("Fire"),
	STUPEFY("Push"),
	AVADAKEDAVRA("Kill"),
	CONFUNDUS("Confuse"),
	CONFRINGO("Fireball"),
	EXPELLIARMUS("Disarm"),
	PROTEGO("Protect"),
	IMMOBULUS("Freeze"),
	EXPULSO("Explosion"),
	LUMOS("Light"),
			
	//Other Spells
	LIGHTNING(null),
	TREE(null),
	WEATHER(null),
	TIME(null),
	ARROW(null);
	
	private final String friendlyName;
	
	Spell(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getName() {return friendlyName;}

}
