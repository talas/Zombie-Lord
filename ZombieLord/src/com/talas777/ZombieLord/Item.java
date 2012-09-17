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
	
	private static final CombatEffect heal50effect = new CombatEffect(CombatEffect.TYPE_ITEM,50,false, ZombieLord.ELEM_HOLY);
	private static final CombatEffect heal250effect = new CombatEffect(CombatEffect.TYPE_ITEM,250,false, ZombieLord.ELEM_HOLY);
	
	private static final CombatEffect mana20effect = new CombatEffect(CombatEffect.TYPE_ITEM,0,20,false,false, ZombieLord.ELEM_NULL);
	private static final CombatEffect mana100effect = new CombatEffect(CombatEffect.TYPE_ITEM,0,100,false,false, ZombieLord.ELEM_NULL);
	private static final CombatEffect mana9999effect = new CombatEffect(CombatEffect.TYPE_ITEM,0,9999,false,false, ZombieLord.ELEM_NULL);
	
	private static final CombatEffect revive10effect = new CombatEffect(CombatEffect.TYPE_ITEM,10,true, ZombieLord.ELEM_HOLY);
	
	private static final CombatEffect antidoteEffect = new CombatEffect(CombatEffect.TYPE_ITEM);
	private static final CombatEffect grenadeEffect = new CombatEffect(CombatEffect.TYPE_ITEM, -70, false, ZombieLord.ELEM_FIRE);
        private static final CombatEffect add1strEffect = new CombatEffect(CombatEffect.TYPE_ITEM);
	
	
	private static final CombatAction heal50 = new CombatAction("Potion",ZombieLord.ITEM_ACTION,0,heal50effect,Targeting.TARGET_SINGLE);
	private static final CombatAction heal250 = new CombatAction("Hi-Potion",ZombieLord.ITEM_ACTION,0,heal250effect,Targeting.TARGET_SINGLE);
	
	private static final CombatAction mana20 = new CombatAction("Ether", ZombieLord.ITEM_ACTION,0,mana20effect,Targeting.TARGET_SINGLE);
	private static final CombatAction mana100 = new CombatAction("Hi-Ether", ZombieLord.ITEM_ACTION,0,mana100effect,Targeting.TARGET_SINGLE);
	private static final CombatAction mana9999 = new CombatAction("Super-Ether", ZombieLord.ITEM_ACTION,0,mana9999effect, Targeting.TARGET_SINGLE);
	
	private static final CombatAction revive10 = new CombatAction("Revive", ZombieLord.ITEM_ACTION,0,revive10effect,Targeting.TARGET_SINGLE);
	
	private static final CombatAction antidote = new CombatAction("Antidote", ZombieLord.ITEM_ACTION,0,antidoteEffect, Targeting.TARGET_SINGLE);
	private static final CombatAction grenade = new CombatAction("Grenade", ZombieLord.ITEM_ACTION, 0, grenadeEffect, Targeting.TARGET_SINGLE);
        private static final CombatAction add1str = new CombatAction("UNDEF", ZombieLord.ITEM_ACTION,0, add1strEffect, Targeting.TARGET_SINGLE);

	public static final Item Potion = new ConsumeableItem("Potion",true,"Heals one character by 50 hp",heal50,false,false,true);
	public static final Item Hi_Potion = new ConsumeableItem("Hi-Potion",true,"Heals one character by 250 hp",heal250,false,false,true);
	
	public static final Item Ether = new ConsumeableItem("Ether",true,"Gives one character 20 mp",mana20,false,false,true);
	public static final Item Hi_Ether = new ConsumeableItem("Hi-Ether",true,"Gives one character 100 mp",mana100,false,false,true);
	public static final Item Super_Ether = new ConsumeableItem("Super-Ether",true,"Gives one character 9999 mp",mana9999,false,false,true);
	
	public static final Item Phoenix_Feather = new ConsumeableItem("Phoenix Feather", true,"Revives a dead character", revive10, false, true, true);
	
	
	
	public static final Item Antidote = new ConsumeableItem("Antidote",true, "Cures poison status", antidote, false, false,true);
	
	
	public static final Item Grenade = new ConsumeableItem("Grenade",true,"Deals fire damage to one opponent",grenade,true,false,false);
	
	public static final Item Tissue = new UselessItem("Tissue", "This is a tough one..");
	public static final Item Panties = new UselessItem("Panties", "You found, you keep");
	public static final Item Buster_Sword = new UselessItem("Buster Sword", "Too big to fight with, maybe for LARPing?");
	public static final Item Cloud = new UselessItem("Cloud","Keep it in the bottle");
	public static final Item Ash = new UselessItem("Ash", "Maybe its from somones dead relatives?");
	public static final Item Banana = new UselessItem("Banana", "It's not edible");
	public static final Item Hot_Soup = new UselessItem("Hot Soup", "Everhot soup, unfortunately to hot to eat");
	
        public static final Item StrUp = new ConsumeableItem("Str Up", false, "Permanently increases Strength stat of a character by 1", add1str, false, false, true);

	protected final static void initiate(){
		antidoteEffect.addStatusChange(Combat.STATE_POISONED, 1.0f, false, 0);
		add1strEffect.addAttributeChange(CombatEffect.STAT_STR, 1, 0, 100);
	}
	
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
	
	
	public Item(String name, boolean isCombatItem, String description){
		this.name = name;
		this.combat = isCombatItem;
		this.maxStack = 99;
	}
	
	public Item(String name, boolean isCombatItem, String description, byte maxStack){
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
