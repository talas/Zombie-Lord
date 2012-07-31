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

public class CombatOption {

	// NOTE: combat options like item, defend and escape are hardcoded.. see ZombieLord.java
	public final CombatAction associatedAction;
	public final String name;
	public final boolean isCombatAction;
	
	public CombatOption(String name){
		this.name = name;
		this.isCombatAction = false;
		this.associatedAction = null;
	}
	
	public CombatOption(String name, CombatAction action){
		this.name = name;
		this.isCombatAction = true;
		this.associatedAction = action;
	}
}
