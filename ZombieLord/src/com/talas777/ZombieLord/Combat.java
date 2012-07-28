package com.talas777.ZombieLord;

import java.util.HashMap;

/**
 * A class to hold all the data of an ongoing battle
 * @author talas
 *
 */
public class Combat {

	MonsterSetup setup;
	Party party;
	/**
	 * how long the players have to wait to take the initial action.
	 */
	public final int baseActionDelay;
	
	// each player character(actually monsters too) has to wait a given time between each action.
	// time is only subtracted when no action is occuring (kinda common i think in rpgs?)
	
	/**
	 * How long (in msec) each combat participant (identified by name) has to wait to make a new action.
	 */
	public HashMap<String,Integer> actionTimer;
	
	
	public boolean isReady(String combatant){ // throws NullPointerException
		return actionTimer.get(combatant) <= 0;
	}
	
	// public ??? getAction(String combatant) //TODO: ??
	
	public int getMonsterHealth(int monsterId){
		return 100;
	}
	
	/**
	 * Gets the state of the monster.
	 * Can be normal, poisoned, slow, haste, stop, stone, paralyzed, fury, weakness or doom
	 * @param monsterId
	 * @return
	 */
	public byte getMonsterState(int monsterId){
		return 0;
	}
	
	public static final int STATE_NORMAL = 0;
	public static final int STATE_POISONED = 1;
	public static final int STATE_SLOW = 2;
	public static final int STATE_HASTE = 3;
	public static final int STATE_STOP = 4;
	public static final int STATE_STONE = 5;
	public static final int STATE_PARALYZED = 6;
	public static final int STATE_FURY = 7;
	public static final int STATE_WEAKNESS = 8;
	public static final int STATE_DOOM = 9;
	
	/**
	 * Get the current state of the battle
	 * @return state of battle, 0 = ongoing, 1 = player won, 2 = GAME OVER..
	 */
	public byte getBattleState(){
		// check if either all monsters have been disabled or all players are dead.
		
		// If everyone is dead (including the players) treat it as game over..
		
		// first check if all players are dead
		// in which case it is GAME OVER, no matter if the monsters are dead or alive
		boolean hasLiveMember = false;
		int[] activePlayers = party.getActiveMembers();
		for(int i = 0; i < activePlayers.length; i++){
			if(party.getMemberHealth(activePlayers[i]) >0){
				hasLiveMember = true;
			}
		}
		
		if(!hasLiveMember){
			// GAME OVER!
			return 2;
		}
		
		// check monsters
		boolean hasLiveMonster = false;
		for (int i = 0; i < getNumEnemies(); i++) {
			if (this.getMonsterHealth(i) > 0) {
				switch (this.getMonsterState(i)) { // also check if monster was
													// immobilized
				case STATE_STOP:
				case STATE_STONE:
				case STATE_PARALYZED:
					break;
				default:
					hasLiveMonster = true;
				}
			}
		}
		
		return (byte)(hasLiveMonster? 0 : 1);
	}
	
	/**
	 * ticks all combat timers
	 * @param time
	 */
	public void tick(float time){
		
	}
	
	public Combat(MonsterSetup setup, Party party, int baseDelay){
		this.setup = setup;
		this.party = party;
		this.baseActionDelay = baseDelay;
	}
	
	public Monster getMonster(int monsterId){
		return setup.getMonsters().get(monsterId);
	}
	
	public int getNumEnemies(){
		return setup.getMonsters().size();
	}
	
	
	
	public int[] getMonsterPosition(int monsterId, int w, int h){
		int posx = 0;
		int posy = 0;
		if(setup.getFormation() == MonsterSetup.FORMATION_SIMPLE){
			/*
			 * 0 2
			 * 1 3
			 * TODO:  4?
			 */
			if(monsterId <= 1) // first column
				posx = (int)(w/2f+w/6f);
			else
				posx = (int)(w/2f+w/4f);
			
			if(monsterId == 0 || monsterId == 2) // first row
				posy = (int)(w/4f+w/8f);
			else
				posy = (int)(w/6f);
			
		}
		else {
			//TODO:
			System.err.println("NIY, not implemented yet! ;p");
		}
		return new int[]{posx, posy};
	}
}
