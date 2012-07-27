package com.talas777.ZombieLord;

import java.util.HashMap;

/**
 * A class to hold all the data of an ongoing battle
 * @author talas
 *
 */
public class Combat {

	MonsterSetup setup;
	
	
	// each player character(actually monsters too) has to wait a given time between each action.
	// time is only subtracted when no action is occuring (kinda common i think in rpgs?)
	
	/**
	 * How long (in msec) each combat participant (identified by name) has to wait to make a new action.
	 */
	public HashMap<String,Integer> actionTimer;
	
	//TODO: health, mana and state tracking
	
	
	public boolean isReady(String combatant){ // throws NullPointerException
		return actionTimer.get(combatant) <= 0;
	}
	
	
	public Combat(MonsterSetup setup){
		this.setup = setup;
	}
}
