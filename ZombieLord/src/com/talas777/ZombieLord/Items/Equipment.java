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

package com.talas777.ZombieLord.Items;

import java.util.LinkedList;

import com.talas777.ZombieLord.Element;
import com.talas777.ZombieLord.Item;
import com.talas777.ZombieLord.PartyMember;

public abstract class Equipment extends Item {

	private final int attack;
	private final int defense;
	private final int mAttack;
	private final int mDefense;
	
	/**
	 * Element attack multipliers
	 * Duplicate elements allowed
	 */
	private final LinkedList<ElementAffection> elementOffense;
	
	/**
	 * Element defense multipliers
	 * Duplicate elements allowed
	 */
	private final LinkedList<ElementAffection> elementDefense;
	
	public Equipment(String name, String description, int attack, int defense, int mAttack, int mDefense) {
		super(name, false,description,(byte)1);
		this.attack = attack;
		this.defense = defense;
		this.mAttack = mAttack;
		this.mDefense = mDefense;
		this.elementDefense = new LinkedList<ElementAffection>();
		this.elementOffense = new LinkedList<ElementAffection>();
	}
	
	private class ElementAffection {
		public final Element element;
		public final float strength;
		public ElementAffection(Element e, float s){
			this.element = e;
			this.strength = s;
		}
	}
	
	/**
	 * @see Combatant.getElementDefense
	 */
	protected void addElementDefense(Element element, float strength){
		this.elementDefense.add(new ElementAffection(element,strength));
	}
	
	/**
	 * @see Combatant.getElementStrength
	 */
	protected void addElementOffense(Element element, float strength){
		this.elementOffense.add(new ElementAffection(element,strength));
	}
	
	/**
	 * @see Combatant.getElementDefense
	 */
	public float getElementDefense(Element e){
		float defense = 0f;
		for(ElementAffection a : this.elementDefense){
			if(a.element == e)
				defense += a.strength;
		}
		return defense;
	}
	
	/**
	 * @see Combatant.getElementStrength
	 */
	public float getElementOffense(Element e){
		float defense = 0f;
		for(ElementAffection a : this.elementOffense){
			if(a.element == e)
				defense += a.strength;
		}
		return defense;
	}
	
	/**
	 * Details of how this equipment affects the player
	 * Note that equipment are allowed to have hidden properties
	 * @return
	 */
	public abstract String[] getDetails();
	
	
	/**
	 * Apply the effects of equipping this piece of equipment to given member.
	 * @param member
	 */
	public void equip(PartyMember member) {
		member.atk += attack;
		member.def += defense;
		member.matk += mAttack;
		member.mdef += mDefense;
		for(ElementAffection a : this.elementDefense){
			float oldValue = member.getElementDefense(a.element);
			member.setElementDefense(a.element, oldValue + a.strength);
		}
		for(ElementAffection a : this.elementOffense){
			float oldValue = member.getElementStrength(a.element);
			member.setElementStrength(a.element, oldValue + a.strength);
		}
	}
	
	/**
	 * Remove the effects from having equipped this equipment on the member
	 * @param member
	 */
	public void unEquip(PartyMember member) {
		member.atk -= attack;
		member.def -= defense;
		member.matk -= mAttack;
		member.mdef -= mDefense;
		for(ElementAffection a : this.elementDefense){
			float oldValue = member.getElementDefense(a.element);
			member.setElementDefense(a.element, oldValue - a.strength);
		}
		for(ElementAffection a : this.elementOffense){
			float oldValue = member.getElementStrength(a.element);
			member.setElementStrength(a.element, oldValue - a.strength);
		}
	}

}
