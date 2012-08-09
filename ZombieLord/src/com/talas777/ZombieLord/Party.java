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

public class Party {

	public static final int ID_MAIN = 0;
	public static final int ID_GF = 1;
	public static final int ID_SIDE = 2;
	
	public Party(){
		this.activeMembers = new LinkedList<PartyMember>();
		this.members = new LinkedList<PartyMember>();
		this.inventory = new Inventory(99);
	}
	
	private LinkedList<PartyMember> members;
	private LinkedList<PartyMember> activeMembers;
	private Inventory inventory;
	
	public boolean giveItem(Item i, byte count){
		
		return (inventory.addItem(i, count));
	}
	
	/*public boolean hasCombatItem(){
		return false; // TODO: actually, we have no items at all..
	}*/
	
	public Inventory getInventory(){
		return inventory;
	}
	
	public void addExperience(int ammount){
		if(ammount == 0)
			return; // Dont bother me!
		for(PartyMember active : activeMembers){
			if(active.getHealth() > 0){ //only people who survived get xp...
				active.addExperience(ammount);
			}
		}
	}

	
	public void addMember(PartyMember member){
		members.add(member);
		if(this.activeMembers.size() < 3){
			// also set as active..
		}
		activeMembers.add(member);
	}
	
	public boolean isActive(PartyMember member){
		for(PartyMember m : this.activeMembers){
			if(m.getName().equals(member.getName()))
				return true;
		}
		return false;
	}
	
	
	
	public PartyMember[] getActiveMembers(){
		PartyMember[] active = new PartyMember[this.activeMembers.size()];
		return activeMembers.toArray(active);
		/*for(PartyMember member : activeMembers){
			active[i] = member;
		}
		return active;*/
	}

	public boolean hasCombatItem() {
		return (inventory.getCombatItems().size() > 0);
	}

}
