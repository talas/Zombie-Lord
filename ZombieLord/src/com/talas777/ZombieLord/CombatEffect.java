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

public class CombatEffect {

	
	public static final byte TYPE_PHYSICAL = 0;
	public static final byte TYPE_MAGICAL = 1;
	public static final byte TYPE_ITEM = 2;
	
	private byte effectType;
	private double baseHealthChange;
	private double baseManaChange;
	private LinkedList<StatusChange> statusChanges;
	private boolean healthPercent;
	private boolean manaPercent;
	
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
	 */
	public CombatEffect(byte effectType, double baseHealthChange, double baseManaChange, boolean healthPercent, boolean manaPercent){
		this.effectType = effectType;
		this.baseHealthChange = baseHealthChange;
		this.baseManaChange = baseManaChange;
		this.healthPercent = healthPercent;
		this.manaPercent = manaPercent;
		
		this.statusChanges = new LinkedList<StatusChange>();
	}
	
	/**
	 * Constructor for attacks that only changes health (and maybe status)
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 * @param baseHealthChange the base health change (affected by atk and def). For percent based this number is the percent change (-0.15 = 15% loss of health)
	 * @param healthPercent if this attack changes health based on a percentage
	 */
	public CombatEffect(byte effectType, double baseHealthChange, boolean healthPercent){
		this(effectType, baseHealthChange, 0, healthPercent, false);
	}

	/**
	 * Constructor for attacks that only changes mana (and maybe status)
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 * @param baseManaChange the base mana change (affected by atk and def). For percent based this number is the percent change (-0.72 = 72% loss of mana)
	 * @param manaPercent if this attack changes mana based on a percentage
	 * @param nothing not used
	 */
	public CombatEffect(byte effectType, double baseManaChange, boolean manaPercent, boolean nothing){
		this(effectType, 0, baseManaChange, false, manaPercent);
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
	 * Constructor for attacks that doesnt change health or mana (but could change status)
	 * @param effectType what type of effect this is, can be physical, magical or item.
	 */
	public CombatEffect(byte effectType){
		this(effectType, 0, 0, false, false);
	}
	
	public void applyEffect(Combatant caster, Combatant target){
		float multiplier = 1f;
		float divisor = 1f;
		if(this.effectType == TYPE_MAGICAL){
			multiplier = caster.getMATK()/10f;
			divisor = target.getMDEF()/10f;
		}
		else if(this.effectType == TYPE_PHYSICAL){
			// TODO: account for luck..
			// luck has a chance to cause 'critical hit', which is double damage (only physical attacks)
			// TODO: account for agi..
			// agi can help dodge the attacker. agi also helps counter the enemys dodge attempt
			multiplier = caster.getATK()/10f;
			divisor = target.getDEF()/10f;
		}
		// else, TYPE_ITEM is not affected by atk and def
		
		if(divisor < 1)
			divisor = 1f;
		
		// TODO: element skill multiplier
		
		
		
		double healthChange = baseHealthChange*multiplier/divisor;
		double manaChange = baseManaChange*multiplier/divisor;
		
		System.out.println("mul:"+multiplier+", div:"+divisor+", dmg: "+healthChange);
		
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
		if(target.health > target.health_max)
			target.health = target.health_max;
		
		if(target.mana < 0)
			target.mana = 0;
		if(target.mana > target.mana_max)
			target.mana = target.mana_max;
		
		for(StatusChange statusChange : statusChanges){
			if(Math.random() < statusChange.chance){
				// apply status change..
				target.addStatusChange(statusChange.status, statusChange.state, statusChange.strength);
			}
		}
		
		// TODO: Temporary attributes / changes
		
		// TODO: Permanent attribute changes.
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
