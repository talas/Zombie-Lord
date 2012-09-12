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
	
	public MonsterType monsterType;
	
	public static final float ALPHA_START = 1f;
	
	
	public float fadeSpeed = 0.5f;
	
	public float alpha = ALPHA_START;

	
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
	public Monster(MonsterType monsterType, int level){
		super(monsterType.getName(), monsterType.getHealth(level), monsterType.getHealth(level),
				monsterType.getMana(level), monsterType.getMana(level), monsterType.getExperience(level), level,
				monsterType.getStrength(level), monsterType.getVitality(level), monsterType.getAgility(level),
				monsterType.getIntelligence(level), monsterType.getWisdom(level), monsterType.getSpirit(level),
				monsterType.getLuck(level));
		this.textureName = monsterType.getImageFileName();
		this.monsterType = monsterType;
		this.combatActionWeights = new LinkedList<Float>();
		for(CombatAction action : monsterType.getCombatActions(level)){
			super.addCombatAction(action);
		}
	}
	
	/*
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
	/*public Monster(MonsterType monsterType, int exp, int health, int mana, int health_max, int mana_max, int level, float attack){
		super(monsterType.getName(), health, health_max, mana, mana_max, exp, level,
				monsterType.getStrength(level), monsterType.getVitality(level), monsterType.getAgility(level),
				monsterType.getIntelligence(level), monsterType.getWisdom(level), monsterType.getSpirit(level),
				monsterType.getLuck(level));
		this.textureName = monsterType.getImageFileName();
		this.monsterType = monsterType;
		this.combatActionWeights = new LinkedList<Float>();
	}*/
	
	
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
		
		if(this.health < this.getHealthMax()/2){
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
				if((!current.combatEffect.isDamaging()) && current.mpCost <= this.mana){ // AKA heals instead of damages
					// Check if it can be used on me and my allies :>
					
					switch(current.targetType){ // switch case from hell
						case Targeting.TARGET_ALLY_ALL:
						case Targeting.TARGET_ALLY_SINGLE:
						case Targeting.TARGET_SELF:
							// Found a "perfect one"
							chosenAction = current;
							chosenTarget = this;
							break;
						case Targeting.TARGET_ALL:
						case Targeting.TARGET_RANDOM:
							// Found a "mediocre one"
							// check if VERY desperate..
						    if(combat.getLiveMonsters().size() == 1 && this.health < this.getHealthMax()/4f){// last enemy and with critical health = critical measures
								// TODO: decide if monsters should be suicidal.
								// if they are, it makes the game harder.. as monster could suicide to trigger a game over..
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
					if(current.combatEffect.isDamaging() && current.mpCost <= this.mana){ // we want to deal damage
						// Check if it can be used on me and my allies :>
						
						switch(current.targetType){ // switch case from hell
							case Targeting.TARGET_ENEMY_ALL:
								// Good, unless theres only a single enemy..?
								if(!singleEnemy || (Math.random()*100f) >= 70  ){ // 30% chance to use against single enemy
									chosenAction = current;
									chosenTarget = combat.getLivePlayers().getFirst();
								}
								break;
							case Targeting.TARGET_ENEMY_RANDOM:
							case Targeting.TARGET_ENEMY_SINGLE:
								// these 2 are the same for monsters..
								// and both of them are good
								int numPlayers = combat.getLivePlayers().size();
								int chosenPlayer = (int)Math.floor((Math.random()*numPlayers));
								chosenAction = current;
								chosenTarget = combat.getLivePlayers().get(chosenPlayer);
								break;
							case Targeting.TARGET_ALL_OTHERS:
							case Targeting.TARGET_RANDOM:
								// Found a "mediocre one"
								// check if VERY desperate..
							    if(combat.getLiveMonsters().size() == 1 || this.health < getHealthMax()/4f){// last enemy and with critical health = critical measures
									mediocreChoice = current;
									mediocreTarget = this;
								}
								break;
							case Targeting.TARGET_ALL:
								// Found a "mediocre one"
								// check if VERY desperate..
							    if(Math.random()*100 > 80 || (combat.getLiveMonsters().size() == 1 && this.health > getHealthMax()/3f)){
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
			return new CurrentAction(null, this, null);
		}
		
		LinkedList<Combatant> target = new LinkedList<Combatant>();
		target.add(chosenTarget);
		
		Targeting t = new Targeting(chosenAction.targetType, target, this, true);
		
		return new CurrentAction(chosenAction, this, t);
	}
	
	public int getBaseDelay(){
		return 200/level;
	}

}
