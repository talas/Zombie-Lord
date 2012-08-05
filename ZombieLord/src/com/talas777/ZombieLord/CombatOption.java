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

public class CombatOption {

	// NOTE: combat options like item, defend and escape are hardcoded.. see ZombieLord.java
	public final LinkedList<CombatAction> associatedActions;
	public final String name;
	public final boolean subGroup;
	public final boolean hardCoded;
	
	/**
	 * For use with combat option that has a HARDCODED action.
	 * @param name
	 */
	public CombatOption(String name){
		this.name = name;
		this.subGroup = false;
		this.hardCoded = true;
		this.associatedActions = null;
	}
	
	/**
	 * For use with combat option that have several actions available, and lets the player chose which action to use.
	 * @param name
	 * @param actions
	 */
	public CombatOption(String name, LinkedList<CombatAction> actions){
		this.name = name;
		this.subGroup = true;
		this.hardCoded = false;
		this.associatedActions = actions;
	}
	
	/**
	 * For use with combat options that only has one action which is automatically selected.
	 * @param name
	 * @param action
	 */
	public CombatOption(String name, CombatAction action){
		this.name = name;
		this.subGroup = false;
		this.hardCoded = false;
		this.associatedActions = new LinkedList<CombatAction>();
		this.associatedActions.add(action);
	}
}
