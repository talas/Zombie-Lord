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

public class Party {

	public static final int ID_MAIN = 0;
	public static final int ID_GF = 1;
	public static final int ID_SIDE = 2;
	
	public Party(){
		this.activeMembers = new int[]{0};
	}
	
	private PartyMember[] members = new PartyMember[5];
	
	private int[] activeMembers;
	
	public String getMemberName(int id){
		if(members[id] != null)
		{
			return members[id].getName();
		}
		else
			return "Zombie Lord";
	}
	
	public int getMemberHealth(int id){
		return 100;
	}
	
	public boolean hasCombatItem(){
		return false; // TODO: actually, we have no items at all..
	}
	
	public void setMemberHealth(int id, int health){
		//TODO: setMemberHealth
	}
	
	public void addMember(PartyMember member){
		members[member.id] = member;
	}
	
	public PartyMember[] getActiveMembers(){
		PartyMember[] active = new PartyMember[this.activeMembers.length];
		for(int i = 0; i < activeMembers.length; i++){
			active[i] = members[this.activeMembers[i]];
		}
		return active;
	}
	
	public int[] getInactiveMembers(){
		return new int[]{1};
	}
	
	public void setActiveMembers(int first, int second, int third){
		//TODO: setActiveMembers
	}
}
