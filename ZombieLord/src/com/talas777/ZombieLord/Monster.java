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

import java.util.Collections;
import java.util.LinkedList;


public class Monster extends Combatant{

	
	/**
	 * filename of monsters avatar
	 */
	public String textureName;

	
	/**
	 * strength of monsters physical attack, for calculations
	 */
	public float attackStrength;

	
	/**
	 * The chance of carrying out each action in combat
	 */
	private LinkedList<Float> combatActionWeights;
	
	/**
	 * Simplified constructor for most monsters
	 * @param name
	 * @param textureName
	 * @param exp
	 * @param health_max
	 * @param mana_max
	 * @param level
	 * @param attack
	 */
	public Monster(String name, String textureName, int exp, int health_max, int mana_max, int level, float attack){
		super(name, health_max, health_max, mana_max, mana_max, exp, level);
		this.textureName = textureName;
		this.combatActionWeights = new LinkedList<Float>();
	}
	/**
	 * Constructor for monsters that doesnt start with full health and/or mana
	 * @param name
	 * @param textureName
	 * @param exp
	 * @param health
	 * @param mana
	 * @param health_max
	 * @param mana_max
	 * @param level
	 * @param attack
	 */
	public Monster(String name, String textureName, int exp, int health, int mana, int health_max, int mana_max, int level, float attack){
		super(name, health, health_max, mana, mana_max, exp, level);
		this.textureName = textureName;
		this.combatActionWeights = new LinkedList<Float>();
	}
	

	
	/**
	 * 
	 * @param action
	 * @param chance, Note that this is a weight for picking a random action.
	 */
	public void addCombatAction(CombatAction action, float chance){
		super.addCombatAction(action);
		this.combatActionWeights.add(chance);
	}
	
	/**
	 * Adds an action with a default weight of 1
	 * @param action
	 */
	@Override
	public void addCombatAction(CombatAction action){
		super.addCombatAction(action);
		this.combatActionWeights.add(1f);
	}
	
	public CurrentAction getMonsterAction(Party party, Combat combat){
		// Uses all the information available to make a decission for the monster.
		//TODO: use info, write function
		
		LinkedList<CombatAction> viableActions = (LinkedList<CombatAction>) this.getCombatActions().clone();
		
		CombatAction chosenAction = null;
		CombatAction mediocreChoice = null;
		Combatant mediocreTarget = null;
		Combatant chosenTarget = null;
		
		boolean wantHealing = false;
		boolean singleEnemy = false;
		
		if(this.health < this.health_max/2){
			// less than 50% health.. should heal if possible?
			wantHealing = true;
			System.out.println("heal me!");
		}
		
		if(combat.getLivePlayers().size() == 1){
			singleEnemy = true;
			// Zolom/ruby lululul
		}
		
		Collections.shuffle(viableActions);
		
		
		
		if(wantHealing){
			// seach for healing actions..
			
			for(int i = 0; i < viableActions.size(); i++){
				if(chosenAction != null) // dont keep looking if we already found a good one
					break;
				CombatAction current = viableActions.get(i);
				if(current.healthChange > 0 && current.mpCost <= this.mana){ // AKA heals instead of damages
					// Check if it can be used on me and my allies :>
					
					switch(current.targetType){ // switch case from hell
						case CombatAction.TARGET_ALLY_ALL:
						case CombatAction.TARGET_ALLY_SINGLE:
						case CombatAction.TARGET_SELF:
							// Found a "perfect one"
							chosenAction = current;
							chosenTarget = this;
							break;
						case CombatAction.TARGET_ALL:
						case CombatAction.TARGET_RANDOM:
							// Found a "mediocre one"
							// check if VERY desperate..
							if(combat.getLiveMonsters().size() == 1 && this.health < this.health_max/4f){// last enemy and with critical health = critical measures
								mediocreChoice = current;
								mediocreTarget = this;
							}
							break;
						default:
							//cant be used
					}
				}
			}
		}
			while(chosenAction == null){ // didnt want healing, so loop untill we find something else..
				for(int i = 0; i < viableActions.size(); i++){
					if(chosenAction != null) // dont keep looking if we already found a good one
						break;
					CombatAction current = viableActions.get(i);
					if(current.healthChange < 0  && current.mpCost <= this.mana){ // we want to deal damage
						// Check if it can be used on me and my allies :>
						
						switch(current.targetType){ // switch case from hell
							case CombatAction.TARGET_ENEMY_ALL:
								// Good, unless theres only a single enemy..?
								if(!singleEnemy || (Math.random()*100f) >= 70  ){ // 30% chance to use against single enemy
									chosenAction = current;
									chosenTarget = combat.getLivePlayers().getFirst();
								}
								break;
							case CombatAction.TARGET_ENEMY_RANDOM:
							case CombatAction.TARGET_ENEMY_SINGLE:
								// these 2 are the same for monsters..
								// and both of them are good
								int numPlayers = combat.getLivePlayers().size();
								int chosenPlayer = (int)Math.floor((Math.random()*numPlayers));
								chosenAction = current;
								chosenTarget = combat.getLivePlayers().get(chosenPlayer);
								break;
							case CombatAction.TARGET_ALL_OTHER:
							case CombatAction.TARGET_RANDOM:
								// Found a "mediocre one"
								// check if VERY desperate..
								if(combat.getLiveMonsters().size() == 1 || this.health < this.health_max/4f){// last enemy and with critical health = critical measures
									mediocreChoice = current;
									mediocreTarget = this;
								}
								break;
							case CombatAction.TARGET_ALL:
								// Found a "mediocre one"
								// check if VERY desperate..
								if(Math.random()*100 > 80 || (combat.getLiveMonsters().size() == 1 && this.health > this.health_max/3f)){
									mediocreChoice = current;
									mediocreTarget = this;
								}
								break;
							default:
								//Not good
						}
					}
				}
			}
		
		
		if(chosenAction == null){
			chosenAction = mediocreChoice;
			chosenTarget = mediocreTarget;
		}
		if(chosenAction == null){
			System.err.println("Failed to find an action for "+this.getName()+" lvl"+this.level);
			return new CurrentAction(null, this);
		}
		
		return new CurrentAction(chosenAction, this, chosenTarget);
	}
	
	public int getBaseDelay(){
		return 200/level;
	}

}
