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

import java.util.LinkedList;


/**
 * A class to abstract away the combat targeting.
 * @author talas
 *
 */
public class Targeting {

	private final byte type;
	private final LinkedList<Combatant> validTargets;
	private final Combatant caster;
	private LinkedList<Combatant> selected;
	private final boolean offensive;
	
	public static final byte TARGET_ALL = 0;
	public static final byte TARGET_SINGLE = 1;
	public static final byte TARGET_RANDOM = 2;
	public static final byte TARGET_SELF = 3;
	public static final byte TARGET_SIDE = 4;
	public static final byte TARGET_ALLY_ALL = 5;
	public static final byte TARGET_ALLY_SINGLE = 6;
	public static final byte TARGET_ALLY_RANDOM = 7;
	public static final byte TARGET_ENEMY_ALL = 8;
	public static final byte TARGET_ENEMY_SINGLE = 9;
	public static final byte TARGET_ENEMY_RANDOM = 10;
	public static final byte TARGET_ALL_OTHERS = 11;
	
	public Targeting(byte type, LinkedList<Combatant> validTargets, Combatant caster, boolean offensive){
		this.type = type;
		this.validTargets = validTargets;
		this.caster = caster;
		this.selected = new LinkedList<Combatant>();
		this.offensive = offensive;
		this.cycle(false);
	}
	
	/**
	 * Cycles to the next valid target(s) if there is/are one/some.
	 */
	public void next()
	{
		this.cycle(false);
	}
	
	/**
	 * Cycles to the previous valid target(s) if there is/are one/some.
	 */
	public void previous(){
		this.cycle(true);
	}
	
	private void cycle(boolean reverse){
		switch(this.type){ // A switch case from the abyss! NOTE: Could be written as 1 beautiful line in common lisp. TODO: Rewrite in common lisp
			case TARGET_SINGLE:
			case TARGET_ALLY_SINGLE:
			case TARGET_ENEMY_SINGLE:
				if(this.selected.size() == 0){
					boolean searchForMonster = false;
					if((this.caster instanceof PartyMember && this.offensive) || this.caster instanceof Monster && !this.offensive)
						searchForMonster = true;
					for(Combatant c : validTargets){
						if((c instanceof Monster && searchForMonster) || c instanceof PartyMember && !searchForMonster){
							selected.add(c);
							break;
						}
					}
				}
				else {
					int currentIndex = validTargets.indexOf(this.selected.getFirst());
					this.selected.clear();
					if(reverse){
						if(currentIndex == 0)
							this.selected.add(validTargets.getLast());
						else
							this.selected.add(validTargets.get(currentIndex-1));
					}
					else {
						if(currentIndex == this.validTargets.size()-1)
							this.selected.add(validTargets.getFirst());
						else
							this.selected.add(validTargets.get(currentIndex+1));
					}
				}
				break;
				
			case TARGET_SIDE:
				// uniq and only valid for players.
				if(this.selected.size() == 0){
					if(this.caster instanceof PartyMember) {
						for(Combatant c : this.validTargets){
							if(c instanceof Monster && this.offensive)
								this.selected.add(c);
							else if(!this.offensive)
								this.selected.add(c);
						}
					}
					else {
						for(Combatant c : this.validTargets){
							if(c instanceof PartyMember)
								this.selected.add(c);
							else if(!this.offensive)
								this.selected.add(c);
						}
					}
				}
				else {
					// currently it either targets all the monsters or all the partymembers
					// so we just need to switch them
					if(this.selected.getFirst() instanceof Monster){
						// fill it with players
						this.selected.clear();
						for(Combatant c : this.validTargets)
							if(c instanceof PartyMember)
								this.selected.add(c);
					}
					else {
						// fill it with monsters
						this.selected.clear();
						for(Combatant c : this.validTargets)
							if(c instanceof Monster)
								this.selected.add(c);
					}
				}
				break;
				
			default:
				// all others
				// these ones cant really cycle. so just add everyone and done.
				if(this.selected.size() == 0){
					this.selected = this.validTargets;
				}
				break;
		}
	}
	
	/**
	 * 
	 * @return LinkedList<Combatant> the selected combatant(s)
	 */
	public LinkedList<Combatant> getCurrentTargets() {
		return this.selected;
	}
	
	/**
	 * 
	 * @return byte the type of selection used on this Targeting
	 */
	public byte getSelectionType(){
		return type;
	}
}
