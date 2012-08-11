/**
 * 
 */
package com.lavacraftserver.HarryPotterSpells.api;

@SuppressWarnings("serial")
public class InvalidSpellException extends RuntimeException{

	public InvalidSpellException(){}
	
	public InvalidSpellException(String msg){
		super(msg);
	}
	public InvalidSpellException(Throwable e){
		super(e);
	}
}
