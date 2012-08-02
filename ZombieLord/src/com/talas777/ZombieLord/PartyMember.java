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

public class PartyMember extends Combatant{

	public final int id;
	
	public static long getExperienceForLevel(int level){
		return level*level;
	}
	
	public PartyMember(int id, String name, int healthMax, int healthNow, int manaMax, int manaNow, int exp){
		super(name, healthNow, healthMax, manaNow, manaMax, exp, getLevel(exp));
		this.id = id;
	}
	
	public static int getLevel(int experience){
		return (int)Math.floor(Math.sqrt(experience))+1;
	}
	
	public boolean addExperience(int exp){
		this.exp += exp;
		boolean hasLevel = false;
		while(this.exp > getExperienceForLevel(level+1)){
			// level up!
			level++;
			hasLevel = true;
		}
		return hasLevel;
	}
	
	public int getMana(){
		return mana;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getBaseDelay(){
		return (int)(Math.max(50, Math.min(100, 187/level))*0.5f);
	}
}
