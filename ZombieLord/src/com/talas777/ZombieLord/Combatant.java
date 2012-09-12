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
import java.util.ListIterator;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Combatant {
	
	
	
	
	
	// Stats / atributes. These are used in combat calculations
	private int strength; // strength increase atk
	private int vitality; // vitality increase health and defense
	private int agility; // agility increase chance to hit(accuracy) and dodge (evasion)
	private int intelligence; // intelligence gives magic atk power
	private int wisdom; // wisdom gives mana
	private int spirit; // spirit increase magic defense
	private int luck; // luck increase chance of critical hit
	
	public int getStrength(){
		int var = strength;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 0)
				var += t.change;
		}
		return var;
	}
	
	public int getVitality(){
		int var = vitality;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 1)
				var += t.change;
		}
		return var;
	}
	
	public int getAgility(){
		int var = agility;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 2)
				var += t.change;
		}
		return var;
	}
	
	public int getIntelligence(){
		int var = intelligence;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 3)
				var += t.change;
		}
		return var;
	}
	
	public int getWisdom(){
		int var = wisdom;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 4)
				var += t.change;
		}
		return var;
	}
	
	public int getSpirit(){
		int var = spirit;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 5)
				var += t.change;
		}
		return var;
	}
	
	public int getLuck(){
		int var = luck;
		for(TemporaryAttribute t : this.temporaryAttributes){
			if(t.attribute == 6)
				var += t.change;
		}
		return var;
	}
	
	private class ElementEffect {
		public final Element element;
		public float effect;
		
		public ElementEffect(Element element, float effect){
			this.element = element;
			this.effect = effect;
		}
	}
	
	private final LinkedList<ElementEffect> elementStrength;
	private final LinkedList<ElementEffect> elementDefense;
	
	private final LinkedList<TemporaryAttribute> temporaryAttributes;
	
	/**
	 * 1 = 100% effect
	 * 0.5 = 50% effect
	 * 0 = 0% effect
	 * 2 = 200% effect
	 * @param element
	 * @return
	 */
	public void setElementStrength(Element element, float strength){
		for(ElementEffect e : this.elementStrength){
			if(e.element == element){
				e.effect = strength;
				return;
			}
		}
		elementStrength.add(new ElementEffect(element, strength));
	}
	
	public float getCriticalHitChance(){
		return this.getLuck()*0.1f;
	}
	
	/**
	 * 
	 *  def 1 -> *= 0 -> 0 effect (no effect)
	 *	def 0.5 -> *= 0.5 -> 50% effect (halve)
	 * 	def 0 -> *= 1 -> 100% effect (normal)
	 *	def -1 -> *= 2 -> 200% effect (double effect)
	 *	def 1.5 -> *= -1.5 -> -50% effect (absorb)
	 * @param element
	 * @return
	 */
	public void setElementDefense(Element element, float defense){
		for(ElementEffect e : this.elementDefense){
			if(e.element == element){
				e.effect = defense;
				return;
			}
		}
		elementDefense.add(new ElementEffect(element, defense));
	}
	
	/**
	 * 1 = 100% effect
	 * 0.5 = 50% effect
	 * 0 = 0% effect
	 * 2 = 200% effect
	 * @param element
	 * @return
	 */
	public float getElementStrength(Element element){
		for(ElementEffect e : this.elementStrength){
			if(e.element == element)
				return e.effect;
		}
		return 1f;
	}
	
	
	/**
	 * 
	 *  def 1 -> *= 0 -> 0 effect (no effect)
	 *	def 0.5 -> *= 0.5 -> 50% effect (halve)
	 * 	def 0 -> *= 1 -> 100% effect (normal)
	 *	def -1 -> *= 2 -> 200% effect (double effect)
	 *	def 1.5 -> *= -1.5 -> -50% effect (absorb)
	 * @param element
	 * @return
	 */
	public float getElementDefense(Element element){
		for(ElementEffect e : this.elementDefense){
			if(e.element == element)
				return e.effect;
		}
		return 0f;
	}
	
	
	
	
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
		this.temporaryAttributes = new LinkedList<TemporaryAttribute>();
		this.elementStrength = new LinkedList<ElementEffect>();
		this.elementDefense = new LinkedList<ElementEffect>();
		
		
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
	
	/**
	 * Ticks status changes and temporary attribute changes.
	 * removing changes that have dissolved and applying damage and effects
	 * @param time
	 */
	public void tickStatusChanges(float time){
		ListIterator<TemporaryAttribute> lia = this.temporaryAttributes.listIterator();
		while(lia.hasNext()){
			TemporaryAttribute a = lia.next();
			if(a.timeLeft != 0){
				a.timeLeft -= time;
				if(a.timeLeft <= 0)
					lia.remove();
			}
		}
		
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
	private int health_max;
	
	/**
	 * mana at the start of battle
	 */
	public int mana;
	/**
	 * max mana
	 */
	private int mana_max;
	
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

        public int getHealthMax() {
	    return this.health_max;
	}

        public int getManaMax() {
	    return this.mana_max;
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
		return this.getIntelligence();
	}
	
	public int getMDEF(){
		return this.getSpirit();
	}
	
	public int getATK(){
		return this.getStrength();
	}
	
	public int getDEF(){
		return this.getVitality();
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

	/**
	 * 
	 * @return (3+agility)
	 */
	public float getDodgeChance() {
		return 3+this.getAgility();
	}

	/**
	 * 
	 * @return (90+agility)
	 */
	public float getHitChance() {
		return 90+this.getAgility();
	}

	public void addStrength(int change) {
		this.strength += change;
	}

	public void addVitality(int change) {
		this.vitality += change;
	}

	public void addAgility(int change) {
		this.agility += change;
	}

	public void addIntelligence(int change) {
		this.intelligence += change;
	}

	public void addWisdom(int change) {
		this.wisdom += change;
	}

	public void addSpirit(int change) {
		this.spirit += change;
	}

	public void addLuck(int change) {
		this.luck += change;
	}
	
	private class TemporaryAttribute {
		public final int attribute;
		public final int change;
		public float timeLeft;
		public TemporaryAttribute(int attribute, int change, float duration){
			if(duration <= 0){
				System.err.println("[WARN] Temporary attribute "+attribute+" duration <= 0!");
			}
			this.attribute = attribute;
			this.change = change;
			this.timeLeft = duration;
		}
	}
	


	public void addTemporaryAttribute(int attribute, int change, float duration) {
		temporaryAttributes.add(new TemporaryAttribute(attribute, change, duration));
	}
}
