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

import com.talas777.ZombieLord.Items.*;

public abstract class Item {
	
	private static final CombatEffect heal50effect = new CombatEffect(CombatEffect.TYPE_ITEM,50,false);
	private static final CombatEffect heal250effect = new CombatEffect(CombatEffect.TYPE_ITEM,250,false);
	
	private static final CombatAction heal50 = new CombatAction("Potion",ZombieLord.ITEM_ACTION,0,heal50effect,Targeting.TARGET_SINGLE);
	private static final CombatAction heal250 = new CombatAction("Hi-Potion",ZombieLord.ITEM_ACTION,0,heal250effect,Targeting.TARGET_SINGLE);
	public static final Item Potion = new ConsumeableItem("Potion",true,heal50,false,false);
	public static final Item Hi_Potion = new ConsumeableItem("Hi-Potion",true,heal250,false,false);
	
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Item){
			Item p = (Item)o;
			return this.equals(p);
		}
		return false;
	}
	

	public boolean equals(Item i){
		if(i.combat == this.combat){
			if(i.maxStack == this.maxStack){
				if(i.name.equals(this.name)){
					return true;
				}
			}
		}
		return false;
	}
	
	
	public final String name;
	
	private final boolean combat;
	
	public final byte maxStack;
	
	
	public Item(String name, boolean isCombatItem){
		this.name = name;
		this.combat = isCombatItem;
		this.maxStack = 99;
	}
	
	public Item(String name, boolean isCombatItem, byte maxStack){
		this.name = name;
		this.combat = isCombatItem;
		this.maxStack = maxStack;
	}
	
	/**
	 * Whether or not this item may be used in combat.
	 * Grenades, potions and such would be combat items.
	 * @return
	 */
	public boolean isCombatItem(){
		return combat;
	}
}
