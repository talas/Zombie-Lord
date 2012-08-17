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

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Combatant {
	
	
	
	
	
	// Stats / atributes. These are used in combat calculations
	private int strength; // strength increase atk
	private int vitality; // vitality increase health and defense
	private int agility; // agility increase chance to dodge (evasion)
	private int intelligence; // intelligence gives magic atk power
	private int wisdom; // wisdom gives mana
	private int spirit; // spirit increase magic defense
	private int luck; // luck increase combat speed
	
	
	
	
	
	
	
	private Sprite drawSprite;
	
	/*
	 * X position of sprite
	 */
	//private float posx = 0;
	
	/**
	 * if the combatant is attacking and should therefore be drawn a little towards its target.
	 */
	private boolean movedAhead = false;
	
	public Combatant(String name, int health, int health_max, int mana, int mana_max, int exp, int level,
			int strength,int vitality,int agility,int intelligence, int wisdom, int spirit, int luck){
		
		this.strength = strength;
		this.vitality = vitality;
		this.agility = agility;
		this.intelligence = intelligence;
		this.wisdom = wisdom;
		this.spirit = spirit;
		this.luck = luck;
		
		
		
		
		this.name = name;
		this.health = health;
		this.health_max = health_max;
		this.mana = mana;
		this.mana_max = mana_max;
		this.exp = exp;
		this.level = level;
		this.combatActions = new LinkedList<CombatAction>();
		this.drawSprite = null;
		
		this.statusChanges = new LinkedList<Status>();
		
		
		// set all states to normal.
		//TODO:!!
	}
	
	/**
	 * Used when in combat to determine how long the creature has to wait to
	 * make another action
	 */
	public double actionTimer = 0;
	
	/**
	 * name of the creature, MUST be uniqe
	 */
	private final String name;
	
	/**
	 * What state the creature is in
	 * Can be normal, poisoned, slow, haste, stop, stone, paralyzed, fury, weakness or doom
	 */
	private LinkedList<Status> statusChanges;
	
	private class Status {
		/*
		 * About the different statuses:
		 * Poison lasts until cured and comes in different strength
		 * Slow, haste and fury lasts for a set of time and wear off when the time is up.
		 * Doom has no effect until the time is up, which then causes the combatant to instantly die.
		 * Stop, Stone and paralyzed lasts untill cured and makes the combatant effectively dead.
		 * Weakness lasts until the time is up and always halves most of the combatants abilities.
		 */
		
		/**
		 * ID, comparable to Combat.STATE_???
		 */
		public int id;
		/**
		 * If the status is active or not.
		 */
		public boolean state;
		/**
		 * For poison its the strength, for others its the time left.
		 * Not all status types use it (like  Stop, Stone and paralyze).
		 */
		public int data;
		
		/**
		 * Used to count more accurately
		 */
		public float delta;
	}
	
	public void tickStatusChanges(float time){
		for(Status s : this.statusChanges){
			if(s.state){
				switch(s.id){
				case STATE_WEAKNESS:
				case STATE_FURY:
				case STATE_HASTE:
				case STATE_SLOW:
					s.data -= time;
					if(s.data <= 0){
						s.data = 0;
						s.state = false;
					}
					break;
				case STATE_DOOM:
					s.delta += time;
					while(s.delta >= 1){
						s.data -= 1;
						s.delta -=1f;
					}
					
					if(s.data <= 0){
						s.data = 0;
						s.state = false;
						this.health = 0;
					}
					break;
				case STATE_POISONED:
					s.delta += time;
					while(s.delta >= 1){
						s.delta -= 1f;
						this.health = Math.max(0, this.health - s.data);
					}
				}
			}
		}
	}
	
	/**
	 * afflicts or cures this combatants given status
	 * @param statusId
	 * @param newState
	 * @param strength
	 */
	public void addStatusChange(int statusId, boolean newState, int strength){
		for(Status s : this.statusChanges){
			if(s.id == statusId){
				if(s.state){
					// already on.
					if(newState){
						// trying to turn on, when already on..
						// effect depends on status type.
						switch(s.id){
						case STATE_POISONED:
							// increase poison strength
							if(s.data >= strength)
								s.data = (int)(s.data + strength/1.5f);
							else
								s.data = (int)(s.data/1.5f + strength);
							break;
						case STATE_WEAKNESS:
						case STATE_FURY:
						case STATE_HASTE:
						case STATE_SLOW:
							// increase timer
							s.data += strength;
							break;
						case STATE_DOOM:
							// decrease timer a little
							s.data = (int)Math.ceil(s.data / 1.3f);
							break;
						}
					}
					else {
						// trying to turn off
						s.state = false;
						s.data = 0;
					}
				}
				else {
					if(newState){
						// Trying to turn on, when off
						switch(s.id){
						case STATE_POISONED:
						case STATE_WEAKNESS:
						case STATE_FURY:
						case STATE_HASTE:
						case STATE_SLOW:
							s.data = strength;
							s.state = true;
							break;
						case STATE_DOOM:
							s.data = 30;
							s.state = true;
							break;
						default:
							s.state = true;
						}
					}
					// else, trying to turn off, when off => no effect
				}
			}
		}
		// TODO: this is what you're working on.
		// TODO: stop with your new bright idea and finish this first.
	}
	
	public LinkedList<Integer> getActiveStatusChanges(){
		LinkedList<Integer> statuses = new LinkedList<Integer>();
		
		for(Status s : this.statusChanges){
			if(s.state)
				statuses.add(s.id);
		}
		
		return statuses;
	}
	
	public int getPoisonStrength(){
		for(Status s : this.statusChanges){
			if(s.id == STATE_POISONED){
				if(s.state){
					return s.data;
				}
			}
		}
		return 0;
	}
	
	public int getSlowTimeLeft(){
		for(Status s : this.statusChanges){
			if(s.id == STATE_SLOW){
				if(s.state){
					return s.data;
				}
			}
		}
		return 0;
	}
	
	public int getHasteTimeLeft(){
		for(Status s : this.statusChanges){
			if(s.id == STATE_HASTE){
				if(s.state){
					return s.data;
				}
			}
		}
		return 0;
	}
	
	public int getFuryTimeLeft(){
		for(Status s : this.statusChanges){
			if(s.id == STATE_FURY){
				if(s.state){
					return s.data;
				}
			}
		}
		return 0;
	}
	
	public int getDoomTimeLeft(){
		for(Status s : this.statusChanges){
			if(s.id == STATE_DOOM){
				if(s.state){
					return s.data;
				}
			}
		}
		return 0;
	}
	
	public int getWeaknessTimeLeft(){
		for(Status s : this.statusChanges){
			if(s.id == STATE_WEAKNESS){
				if(s.state){
					return s.data;
				}
			}
		}
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
	 * health at the start of battle
	 */
	public int health;
	/**
	 * max health
	 */
	public int health_max;
	
	/**
	 * mana at the start of battle
	 */
	public int mana;
	/**
	 * max mana
	 */
	public int mana_max;
	
	/**
	 * experience given when defeated
	 */
	public int exp;
	
	/**
	 * level of monster, for calculations
	 */
	public int level;
	
	/**
	 * List of attacks this monster has
	 */
	private LinkedList<CombatAction> combatActions;
	
	/**
	 * dont use this one for monsters!
	 * @param action
	 */
	public void addCombatAction(CombatAction action){
		combatActions.add(action);
	}
	
	public String getName(){
		return this.name;
	}
	
	public LinkedList<CombatAction> getCombatActions(){
		return combatActions;
	}
	
	public int getTerminalState(){
		for(Status s : this.statusChanges){
			if(s.state){
				switch(s.id){
				case Combat.STATE_STONE:
				case Combat.STATE_PARALYZED:
				case Combat.STATE_STOP:
					return (int)s.id;
				}
			}
		}
		// NOTE: not in a terminal state
		return Combat.STATE_NORMAL;
	}
	
	public void setSprite(Sprite s){
		this.drawSprite = s;
		//this.posx = s.getX();
	}
	
	public Sprite getSprite(){
		return this.drawSprite;
	}
	
	public void setState(int stateId, boolean state){
		
		for(Status status : this.statusChanges){
			if(status.id == stateId)
				status.state = state;
		}
	}
	
	public int getMATK(){
		return this.intelligence;
	}
	
	public int getMDEF(){
		return this.spirit;
	}
	
	public int getATK(){
		return this.strength;
	}
	
	public int getDEF(){
		return this.vitality;
	}
	
	public abstract int getBaseDelay();
	
	public void setMoveAhead(boolean state){
		if(state == this.movedAhead)
			return; // already moved, do nothing
		else {
			//int movePixels = 200;
			int movePixels = 90;
			if((state && this instanceof Monster) || (!state && this instanceof PartyMember)){ // move left
				// I'm a little ashamed of this strange method
				movePixels *= -1;
			}
			
			//else -> move right
			//this.posx += movePixels;
			this.drawSprite.setX(this.drawSprite.getX() + movePixels);
			this.movedAhead = state;
		}
	}
	
	/*public float getX(){
		return this.posx;
	}*/
}
