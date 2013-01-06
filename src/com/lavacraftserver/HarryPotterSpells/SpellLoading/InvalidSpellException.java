package com.lavacraftserver.HarryPotterSpells.SpellLoading;

public class InvalidSpellException extends RuntimeException{
	private static final long serialVersionUID = -8005285221350599809L;
	public InvalidSpellException(){}
	
	public InvalidSpellException(String msg){
		super(msg);
	}
	public InvalidSpellException(Throwable e){
		super(e);
	}
}
