/* Zombie Lord - A story driven roleplaying game
* Copyright (C) 2012  Talas (talas777@gmail.com)
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package com.talas777.ZombieLord;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * A class to hold all the data of an ongoing battle
 * @author talas
 *
 */
public class Combat {

	MonsterSetup setup;
	Party party;
	
	public boolean waitingForPlayerCommand = false;
	
	/**
	 * Array of creatures in this Combat
	 * TODO: use this
	 */
	Combatant[] combatants;
	
	/**
	 * how long the players have to wait to take the initial action.
	 */
	public final int baseActionDelay;
	
	// each player character(actually monsters too) has to wait a given time between each action.
	// time is only subtracted when no action is occuring (kinda common i think in rpgs?)
	
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
		//boolean hasLiveMember = false;
		if(this.getLivePlayers().size() == 0)
			return 2;
		/*
		PartyMember[] activePlayers = party.getActiveMembers();
		for(int i = 0; i < activePlayers.length; i++){
			if(activePlayers[i].getHealth() >0){
				hasLiveMember = true;
			}
		}
		
		if(!hasLiveMember){
			// GAME OVER!
			return 2;
		}*/
		
		// check monsters
		//boolean hasLiveMonster = false;
		if(this.getLiveMonsters().size() == 0){
			return 1;
		}
		return 0;
		/*for (int i = 0; i < getNumEnemies(); i++) {
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
		}*/
		
		//return (byte)(hasLiveMonster? 0 : 1);
	}
	
	/**
	 * ticks all combat timers
	 * @param time
	 */
	public void tick(float time){
		for(int i = 0; i < combatants.length; i++){
			if(combatants[i].actionTimer <= 0)
				combatants[i].actionTimer = 0;
			else
				combatants[i].actionTimer -= time;
		}
	}
	
	public Monster getFirstReadiedMonster(){
		for(int i = 0; i < combatants.length; i++){
			if(combatants[i] instanceof Monster){
				if(combatants[i].actionTimer <= 0 && combatants[i].health > 0){
					switch(combatants[i].getTerminalState()){
					case STATE_STOP:
					case STATE_STONE:
					case STATE_PARALYZED:
						break;
						default:
							return (Monster) combatants[i]; // A monster that is ready to take action
					}
				}
			}
		}
		return null;
	}
	
	public PartyMember getFirstReadiedPlayer(){
		for(int i = 0; i < combatants.length; i++){
			if(combatants[i] instanceof PartyMember){
				if(combatants[i].actionTimer <= 0 && combatants[i].health > 0){
					switch(combatants[i].getTerminalState()){
					case STATE_STOP:
					case STATE_STONE:
					case STATE_PARALYZED:
						break;
						default:
							return (PartyMember) combatants[i]; // A monster that is ready to take action
					}
				}
			}
		}
		return null;
	}
	
	public boolean canUse(CombatOption option, PartyMember player){
		
		if(option.item != null && party.getInventory().getItemCount(option.item) > 0)
			return true;
		
		if(option.hardCoded && option == ZombieLord.item){
			if(party.hasCombatItem())
				return true;
			return false;
		}
		
		if(option.hardCoded || option.subGroup)
			return true;
		
		// else, mana check..
		//NOTE: there is no HP check.. for spells that cost hp, we are happy to kill the player (even if he didnt have enough hp).
		CombatAction action = option.associatedActions.getFirst();
		if(action.mpCost > player.mana)
			return false;
		else
			return true;
	}
	
	public LinkedList<Combatant> getValidTargets(CombatAction action, Combatant caster){
		LinkedList<Combatant> targets = new LinkedList<Combatant>();
		
		LinkedList<Combatant> liveCombatants = getLiveCombatants();
		LinkedList<Combatant> enemies = new LinkedList<Combatant>();
		LinkedList<Combatant> allies = new LinkedList<Combatant>();
		
		if(caster instanceof Monster){
			for(int i = 0; i < liveCombatants.size(); i++){
				if(liveCombatants.get(i) instanceof Monster)
					allies.add(liveCombatants.get(i));
				else
					enemies.add(liveCombatants.get(i));
			}
		}
		else {
			for(int i = 0; i < liveCombatants.size(); i++){
				if(liveCombatants.get(i) instanceof PartyMember)
					allies.add(liveCombatants.get(i));
				else
					enemies.add(liveCombatants.get(i));
			}
		}

		
		
		switch(action.targetType){
		case Targeting.TARGET_SELF:
			targets.add(caster);
			return targets;
		case Targeting.TARGET_ALL:
		case Targeting.TARGET_RANDOM:
			return liveCombatants;
		case Targeting.TARGET_ALL_OTHERS:
			for(Combatant c : liveCombatants)
				if(c != caster)
					targets.add(c);
			return targets;
		case Targeting.TARGET_ALLY_ALL:
		case Targeting.TARGET_ALLY_SINGLE:
			return allies;
		case Targeting.TARGET_ENEMY_ALL:
		case Targeting.TARGET_ENEMY_SINGLE:
		case Targeting.TARGET_ENEMY_RANDOM:
			return enemies;
		}
		return null;
	}
	
	public boolean isEscapeAllowed(){
		return false; // TODO: no! never!
	}
	
	public boolean isItemAllowed(){
		return true; // TODO: keep your zalbatrunk!
	}
	
	public float getEscapeChance(){
		return 0.75f;
	}
	
	public void resetActionTimer(Combatant c){
		c.actionTimer = (c.getBaseDelay()*(1.5f*Math.random()+0.5f));
	}
	
	public LinkedList<Combatant> applyAction(CurrentAction action){
		if(action.action == null){
			// no action == defend/wait
			return null;
		}
		else {
			
			
			LinkedList<Combatant> affected = action.targeting.getCurrentTargets();
			
			
			/*
			double abs = Math.abs(action.action.healthChange);
			
			float damage = (float)Math.floor(abs + Math.sqrt(action.caster.level*abs));
			
			if(action.action.healthChange < 0){
				damage *= -1;
			}*/
			
			System.out.println(action.caster.getName()+"("+action.caster.health+","+action.caster.mana+")"+" uses "+action.action.name+" on "+(affected.size() > 1? "all":affected.getFirst())+"!");
			for(Combatant c : affected){
				action.action.combatEffect.applyEffect(action.caster, c);
				/*c.health += damage;
				if(c.health < 0)
					c.health = 0;
				else if(c.health > c.health_max)
					c.health = c.health_max;*/
			}
			
			
			/*
			LinkedList<Combatant> liveCombatants = getLiveCombatants();
			
			LinkedList<Combatant> enemies = new LinkedList<Combatant>();
			
			LinkedList<Combatant> allies = new LinkedList<Combatant>();
			
			LinkedList<Combatant> affected = new LinkedList<Combatant>();
			
			
			if(action.caster instanceof Monster){
				for(int i = 0; i < liveCombatants.size(); i++){
					if(liveCombatants.get(i) instanceof Monster)
						allies.add(liveCombatants.get(i));
					else
						enemies.add(liveCombatants.get(i));
				}
			}
			else {
				for(int i = 0; i < liveCombatants.size(); i++){
					if(liveCombatants.get(i) instanceof PartyMember)
						allies.add(liveCombatants.get(i));
					else
						enemies.add(liveCombatants.get(i));
				}
			}
			
			double abs = Math.abs(action.action.healthChange);
			
			float damage = (float)Math.floor(abs + Math.sqrt(action.caster.level*abs));
			
			if(action.action.healthChange < 0){
				damage *= -1;
			}
			
			System.out.println(action.caster.getName()+"("+action.caster.health+","+action.caster.mana+")"+" uses "+action.action.name+"(" + Math.abs(damage) + ") on "+(action.primaryTarget == null? "all":action.primaryTarget.getName()+"("+action.primaryTarget.health+")")+"!");
			
			switch(action.action.targetType){  // apply damage/healing
			case Targeting.TARGET_ALLY_SINGLE:
			case Targeting.TARGET_ENEMY_SINGLE:
				action.primaryTarget.health += damage;
				affected.add(action.primaryTarget);
				if(action.primaryTarget.health < 0)
					action.primaryTarget.health = 0;
				else if(action.primaryTarget.health > action.primaryTarget.health_max)
					action.primaryTarget.health = action.primaryTarget.health_max;
				break;
			case Targeting.TARGET_ALL:
				for(int i = 0; i < liveCombatants.size(); i++){
					liveCombatants.get(i).health += damage;
					affected.add(liveCombatants.get(i));
					if(liveCombatants.get(i).health < 0)
						liveCombatants.get(i).health = 0;
					else if(liveCombatants.get(i).health > liveCombatants.get(i).health_max)
						liveCombatants.get(i).health = liveCombatants.get(i).health_max;
				}
				break;
			case Targeting.TARGET_SELF:
				action.caster.health += damage;
				affected.add(action.caster);
				if(action.caster.health < 0)
					action.caster.health = 0;
				else if(action.caster.health > action.caster.health_max)
					action.caster.health = action.caster.health_max;
				break;
			case Targeting.TARGET_RANDOM:
				int numLive = liveCombatants.size();
				int selected = (int)Math.floor(Math.random()*numLive);
				liveCombatants.get(selected).health += damage;
				affected.add(liveCombatants.get(selected));
				if(liveCombatants.get(selected).health < 0)
					liveCombatants.get(selected).health = 0;
				else if(liveCombatants.get(selected).health > liveCombatants.get(selected).health_max)
					liveCombatants.get(selected).health = liveCombatants.get(selected).health_max;
				break;
			case Targeting.TARGET_ALL_OTHERS:
				for(int i = 0; i < liveCombatants.size(); i++){
					if(liveCombatants.get(i) == action.caster)
						continue;
					liveCombatants.get(i).health += damage;
					affected.add(liveCombatants.get(i));
					if(liveCombatants.get(i).health < 0)
						liveCombatants.get(i).health = 0;
					else if(liveCombatants.get(i).health > liveCombatants.get(i).health_max)
						liveCombatants.get(i).health = liveCombatants.get(i).health_max;
				}
				break;
			case Targeting.TARGET_ALLY_ALL:
				for(int i = 0; i < allies.size(); i++){
					allies.get(i).health += damage;
					affected.add(allies.get(i));
					if(allies.get(i).health < 0)
						allies.get(i).health = 0;
					else if(allies.get(i).health > allies.get(i).health_max)
						allies.get(i).health = allies.get(i).health_max;
				}
				break;
			case Targeting.TARGET_ENEMY_ALL:
				for(int i = 0; i < enemies.size(); i++){
					enemies.get(i).health += damage;
					affected.add(enemies.get(i));
					if(enemies.get(i).health < 0)
						enemies.get(i).health = 0;
					else if(enemies.get(i).health > enemies.get(i).health_max)
						enemies.get(i).health = enemies.get(i).health_max;
				}
				break;
			case Targeting.TARGET_ENEMY_RANDOM:
				int sel = (int)Math.floor(Math.random()*enemies.size());
				enemies.get(sel).health += damage;
				affected.add(enemies.get(sel));
				if(enemies.get(sel).health < 0)
					enemies.get(sel).health = 0;
				else if(enemies.get(sel).health > enemies.get(sel).health_max)
					enemies.get(sel).health = enemies.get(sel).health_max;
				break;
			}*/
			
			
			action.caster.mana -= action.action.mpCost;// apply mpCost to caster (if any)
			return affected;
		}
	}
	
	/**
	 * Resets all monsters.
	 */
	public void cleanUp(){
		for(Combatant c : combatants){
			if(c instanceof Monster){
			        c.health = c.getHealthMax();
			        c.mana = c.getManaMax();
			}
		}
	}
	
	public Combatant[] getCombatants(){
		return combatants;
	}
	
	public LinkedList<Combatant> getLiveCombatants(){
		LinkedList<Combatant> liveCombatants = new LinkedList<Combatant>();
		for(int i = 0; i < combatants.length; i++){
				if(combatants[i].health > 0){
					switch(combatants[i].getTerminalState()){
					case STATE_STOP:
					case STATE_STONE:
					case STATE_PARALYZED:
						break;
						default:
							liveCombatants.add(combatants[i]);
					}
				}
		}
		return liveCombatants;
	}
	
	public LinkedList<Monster> getLiveMonsters(){
		LinkedList<Monster> liveMonsters = new LinkedList<Monster>();
		for(int i = 0; i < combatants.length; i++){
			if(combatants[i] instanceof Monster){
				if(combatants[i].health > 0){
					switch(combatants[i].getTerminalState()){
					case STATE_STOP:
					case STATE_STONE:
					case STATE_PARALYZED:
						break;
						default:
							liveMonsters.add((Monster)combatants[i]);
					}
				}
			}
		}
		return liveMonsters;
	}
	
	
	/*private LinkedList<Combatant> _getLivePlayers(){
		LinkedList<Combatant> livePlayers = new LinkedList<Combatant>();
		for(int i = 0; i < combatants.length; i++){
			if(combatants[i] instanceof PartyMember){
				if(combatants[i].health > 0){
					switch(combatants[i].getState()){
					case STATE_STOP:
					case STATE_STONE:
					case STATE_PARALYZED:
						break;
						default:
							livePlayers.add(combatants[i]);
					}
				}
			}
		}
		return livePlayers;
	}*/
	
	public LinkedList<PartyMember> getLivePlayers(){
		LinkedList<PartyMember> livePlayers = new LinkedList<PartyMember>();
		for(int i = 0; i < combatants.length; i++){
			if(combatants[i] instanceof PartyMember){
				if(combatants[i].health > 0){
					switch(combatants[i].getTerminalState()){
					case STATE_STOP:
					case STATE_STONE:
					case STATE_PARALYZED:
						break;
						default:
							livePlayers.add((PartyMember)combatants[i]);
					}
				}
			}
		}
		return livePlayers;
	}
	
	public Combat(MonsterSetup setup, Party party, int baseDelay){
		this.setup = setup;
		this.party = party;
		this.baseActionDelay = baseDelay;
		
		this.combatants = new Combatant[setup.getMonsters().size()+party.getActiveMembers().length];
		PartyMember[] mem = party.getActiveMembers();
		LinkedList<Monster> monsters = setup.getMonsters();
		int i = 0;
			for(int j = 0; j < mem.length; j++){
				combatants[i] = mem[j];
				combatants[i].actionTimer = this.baseActionDelay + combatants[i].getBaseDelay()*Math.random();
				i++;
			}
			for(int j = 0; j < monsters.size(); j++){
				combatants[i] = monsters.get(j);
				combatants[i].actionTimer = -this.baseActionDelay + combatants[i].getBaseDelay()*(2f*Math.random());
				i++;
			}
		
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
				posy = (int)(h/2f+h/8f);
			else
				posy = (int)(h/3f);
			
		}
		else {
			//TODO:
			System.err.println("NIY, not implemented yet! ;p");
		}
		return new int[]{posx, posy};
	}
}
