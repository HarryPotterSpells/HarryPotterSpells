package com.lavacraftserver.HarryPotterSpells;

public enum Spell {
	//Harry Potter Spells
	CONFUNDO("Confuse"),
	CONFRINGO("Fireball"),
	WINGARDIUMLEVIOSA("Fly"),
	NOX("Darkness"),
	AVADAKEDAVRA("Kill"),
	EXPELLIARMUS("Disarm"),
	EVANESCO("Vanish"),
	REDUCTO("Explode"),
			
	//Other Spells
	TREE(null),
	TIME(null);
	
	private final String friendlyName;
	
	Spell(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getName() {return friendlyName;}

}
