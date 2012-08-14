package com.lavacraftserver.HarryPotterSpells;

public enum Spell {
	//Harry Potter Spells
	AGUAMENTI("Water"),
	ALARTEASCENDARE("MobThrow"),
	ALOHOMORA("DoorOpener"),
	AVADAKEDAVRA("Kill"),
	CONFRINGO("Fireball"),
	CONFUNDO("Confuse"),
	DEPRIMO("Pressure"),
	EPISKEY("Heal"),
	EVANESCO("Vanish"),
	EXPELLIARMUS("Disarm"),
	INCENDIO("Fire"),
	MULTICORFORS("WoolChanger"),
	OBSCURO("Blind"),
	ORCHIDEOUS("Flower"),
	REDUCTO("Explode"),
	REPARO("Repair"),
	SECTUMSEMPRA("Bleed"),
	SONORUS("Amplify"),
	SPONGIFY("Sponge"),
	WINGARDIUMLEVIOSA("Fly"),
	
	//Unimplemented
	LUMOS("Light"),
	NOX("Darkness"),


	//Other Spells
	TREE(null),
	TIME(null);
	
	private final String friendlyName;
	
	Spell(String friendlyName) {
		this.friendlyName = friendlyName;
	}
	
	public String getName() {return friendlyName;}

}
