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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.LinkedList;

public class CombatEffect {

	
	public static final byte TYPE_PHYSICAL = 0;
	public static final byte TYPE_MAGICAL = 1;
	public static final byte TYPE_ITEM = 2;
	
    public static final int STAT_STR = 0;
    public static final int STAT_VIT = 1;
    public static final int STAT_AGI = 2;
    public static final int STAT_INT = 3;
    public static final int STAT_WIS = 4;
    public static final int STAT_SPR = 5;
    public static final int STAT_LUCK = 6;
    
    
	private byte effectType;
	private double baseHealthChange;
	private double baseManaChange;
	private LinkedList<StatusChange> statusChanges;
	private LinkedList<AttributeChange> attributeChanges;
	private boolean healthPercent;
	private boolean manaPercent;
	
	private final Element element;
	
	/**
	 * 
	 * @return true if this combateffect deals damage to the target
	 */
	public boolean isDamaging(){
		return this.baseHealthChange < 0;
	}
	
	/**
	 * Full constructor, See other constructors for more hints.
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 * @param baseHealthChange the base health change (affected by atk and def). For percent based this number is the percent change (-0.15 = 15% loss of health)
	 * @param baseManaChange note that this is not the casting cost! this is mana drain on the target.
	 * @param healthPercent if this attack changes health based on a percentage
	 * @param manaPercent if this attack changes mana based on a percentage
	 * @param element which element to use for this attack
	 */
	public CombatEffect(byte effectType, double baseHealthChange, double baseManaChange, boolean healthPercent, boolean manaPercent, Element element){
		this.effectType = effectType;
		this.baseHealthChange = baseHealthChange;
		this.baseManaChange = baseManaChange;
		this.healthPercent = healthPercent;
		this.manaPercent = manaPercent;
		
		this.statusChanges = new LinkedList<StatusChange>();
		this.attributeChanges = new LinkedList<AttributeChange>();
		this.element = element;
	}
	
	/**
	 * Constructor for attacks that only changes health (and maybe status)
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 * @param baseHealthChange the base health change (affected by atk and def). For percent based this number is the percent change (-0.15 = 15% loss of health)
	 * @param healthPercent if this attack changes health based on a percentage
	 * @param element which element to use for this attack
	 */
	public CombatEffect(byte effectType, double baseHealthChange, boolean healthPercent, Element element){
		this(effectType, baseHealthChange, 0, healthPercent, false, element);
	}

	/**
	 * Constructor for attacks that only changes mana (and maybe status)
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 * @param baseManaChange the base mana change (affected by atk and def). For percent based this number is the percent change (-0.72 = 72% loss of mana)
	 * @param manaPercent if this attack changes mana based on a percentage
	 * @param nothing not used
	 * @param element which element to use for this attack
	 */
	public CombatEffect(byte effectType, double baseManaChange, boolean manaPercent, boolean nothing, Element element){
		this(effectType, 0, baseManaChange, false, manaPercent, element);
	}
	
	/**
	 * Add a status change to this combat effect.
	 * @param status which status type to add/remove
	 * @param chance the chance of applying this status change
	 * @param state whether to add or remove the status (true = add)
	 * @param strength for poison this is the strength, for others this is the time.
	 */
	public void addStatusChange(int status, float chance, boolean state, int strength){
		this.statusChanges.add(new StatusChange(status, chance, state, strength));
	}
	
	/**
	 * Attribute change alters the primary attribute (str, vit, etc.) of a Combatant.
	 * It can be either temporary (for the current combat) or permanent.
	 * @param attribute which attribute to affect (strength=0,vit,agi,int,wis,spr,luck=6)
	 * @param change how much to increase the attribute, negative = decrease
	 * @param duration how long the change lasts, 0 = permanent
	 * @param chance the chance of afflicting the attribute change on each attack
	 */
	public void addAttributeChange(int attribute, int change, float duration, float chance){
		this.attributeChanges.add(new AttributeChange(attribute, change, duration, chance));
	}
	
	/**
	 * Constructor for attacks that doesnt change health or mana (but could change status)
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 */
	public CombatEffect(byte effectType){
		this(effectType, 0, 0, false, false, ZombieLord.ELEM_NULL);
	}
	
	public void applyEffect(Combatant caster, Combatant target){
		float multiplier = 1f;
		float divisor = 1f;
		boolean critical = false;
		
		if(this.effectType == TYPE_MAGICAL){
			multiplier = caster.getMATK()/10f;
			// NOTE: cant dodge or luck magical stuffs
			
			if(this.element == ZombieLord.ELEM_PHYSICAL){
				// spell that deal physical damage use DEF instead of MDEF.
				// but still no dodge or luck
				divisor = target.getDEF()/10f;
			}
			else {
				divisor = target.getMDEF()/10f;
			}
		}
		else if(this.effectType == TYPE_PHYSICAL){
			// luck has a chance to cause 'critical hit', which is double damage (only physical attacks)
			
			if(caster.getCriticalHitChance() > Math.random()){
				// critical hit!
				critical = true;
			}
			
			// agi can help dodge the attacker. agi also helps counter the enemys dodge attempt
			// dodging a critical hit -> normal hit
			
			float dodgeChance = target.getDodgeChance();
			float hitChance = caster.getHitChance();
			
			if(Math.random()*dodgeChance > Math.random()*hitChance){
				// dodge!
				if(critical){
					// hit normal
					ZombieLord.announce("Deflect!");
				}
				else {
					// completely dodged the attack, no damage or status changes.
					ZombieLord.announce("Dodge!");
					return;
				}
			}
			else if(critical){
				// double damage
				multiplier *= 2;
				ZombieLord.announce("Critical hit!");
			}
			
			multiplier = caster.getATK()/10f;
			divisor = target.getDEF()/10f;
		}
		
		// else, TYPE_ITEM is not affected by atk and def
		
		
		
		if(divisor < 1)
			divisor = 1f;
		
		
		
		float elemMult = 1;
		
		if(this.effectType != TYPE_ITEM)
			caster.getElementStrength(this.element);
		
		float elemDefense = target.getElementDefense(this.element);
		
		
		double healthChange = baseHealthChange*multiplier/divisor*elemMult;
		double manaChange = baseManaChange*multiplier/divisor*elemMult;
		
		if(healthChange < 0.0 && this.effectType == TYPE_PHYSICAL) // minimum 1 damage physical attack (unless element immune).. so just inc by 1
		    healthChange -= 1;

		// def 1 -> *= 0 -> 0 effect (no effect)
		// def 0.5 -> *= 0.5 -> 50% effect (halve)
		// def 0 -> *= 1 -> 100% effect (normal)
		// def -1 -> *= 2 -> 200% effect (double effect)
		// def 1.5 -> *= -1.5 -> -50% effect (absorb)
		healthChange *= 1-elemDefense;
		manaChange *= 1-elemDefense;
		
		/*if(elemDefense == 1){
			// no effect
			healthChange = 0;
			manaChange = 0;
		}
		else if(elemDefense > 1){
			// absorb (reverse)
			healthChange = -healthChange;
			manaChange = -manaChange;
		}
		else if (elemDefense > 0){
			// remove some amount of damage
			healthChange *= 1-elemDefense;
			manaChange *= 1-elemDefense;
		}*/
		
		System.out.println("mul:"+multiplier+", div:"+divisor+", dmg: "+healthChange);
		Sprite s = target.getSprite();
		int posOffset = 32;
		if(s != null)
		    ZombieLord.addFloatingNumbers((int)(Math.abs(healthChange)), s.getX()+posOffset ,s.getY()+posOffset, (healthChange <= 0? Color.WHITE : Color.GREEN));
		if(healthPercent){ // percent changes are not affected by atk and def
			healthChange *= baseHealthChange;
		}
		if(manaPercent){ // percent changes are not affected by atk and def
			manaChange *= baseManaChange;
		}
		
		target.health += healthChange;
		target.mana += manaChange;
		
		if(target.health < 0)
			target.health = 0;
		if(target.health > target.getHealthMax())
		    target.health = target.getHealthMax();
		
		if(target.mana < 0)
			target.mana = 0;
		if(target.mana > target.getManaMax())
		    target.mana = target.getManaMax();

		for(StatusChange statusChange : statusChanges){
			if(Math.random() < (critical?statusChange.chance*2:statusChange.chance)){
				// apply status change..
			    
				if(critical && statusChange.status == Combat.STATE_POISONED){
					// critical hit + poison -> double strength poison
					target.addStatusChange(statusChange.status, statusChange.state, statusChange.strength*2);
				}
				else
					target.addStatusChange(statusChange.status, statusChange.state, statusChange.strength);
			}
		}
		
		for(AttributeChange change : this.attributeChanges){
			if(Math.random() < (critical?change.chance*2:change.chance)){
				// apply attribute change
				
				if(change.duration == 0){
					// permanent
					switch(change.attribute){
						case STAT_STR:
							// str
							target.addStrength(change.change);
							break;
						case STAT_VIT:
							// vit
							target.addVitality(change.change);
							break;
						case STAT_AGI:
							// agi
							target.addAgility(change.change);
							break;
						case STAT_INT:
							// int
							target.addIntelligence(change.change);
							break;
						case STAT_WIS:
							// wis
							target.addWisdom(change.change);
							break;
						case STAT_SPR:
							// spr
							target.addSpirit(change.change);
							break;
						case STAT_LUCK:
							// luck
							target.addLuck(change.change);
							break;
						default:
							// error
							System.err.println("ERROR: status change "+change.attribute+" does not exist (yet)[1]!");
							break;
					}
				}
				else {
					target.addTemporaryAttribute(change.attribute, change.change, change.duration);
				}
			}
		}
	}
	
	private class AttributeChange {
		public final float duration;
		public final int attribute;
		public final int change;
		public final float chance;
		/**
		 * Attribute change alters the primary attribute (str, vit, etc.) of a Combatant.
		 * It can be either temporary (for the current combat) or permanent.
		 * @param attribute which attribute to affect (strength=0,vit,agi,int,wis,spr,luck=6)
		 * @param change how much to increase the attribute, negative = decrease
		 * @param duration how long the change lasts, 0 = permanent
		 * @param chance the chance of afflicting the attribute change on each attack
		 */
		public AttributeChange(int attribute, int change, float duration, float chance){
			this.attribute = attribute;
			this.change = change;
			this.duration = duration;
			this.chance = chance;
		}
	}
	
	private class StatusChange {
		public final int status;
		public final float chance;
		public final boolean state;
		public final int strength;
		
		public StatusChange(int status, float chance, boolean state, int strength){
			this.status = status;
			this.chance = chance;
			this.state = state;
			this.strength = strength;
		}
	}
	
	/*
	 * Effect types (all can be conditional):
	 * raw health change
	 * percent health change
	 * raw mana change
	 * percent mana change
	 * status change
	 * temporary attribute change
	 * permanent attribute change
	 */
}
