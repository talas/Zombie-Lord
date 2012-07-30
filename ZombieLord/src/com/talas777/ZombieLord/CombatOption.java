package com.talas777.ZombieLord;

public class CombatOption {

	// NOTE: combat options like item, defend and escape are hardcoded.. see ZombieLord.java
	public final CombatAction associatedAction;
	public final String name;
	public final boolean isCombatAction;
	
	public CombatOption(String name){
		this.name = name;
		this.isCombatAction = false;
		this.associatedAction = null;
	}
	
	public CombatOption(String name, CombatAction action){
		this.name = name;
		this.isCombatAction = true;
		this.associatedAction = action;
	}
}
