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

import com.talas777.ZombieLord.CombatAction;
import com.talas777.ZombieLord.Item;

public class ConsumeableItem extends Item {

	private final CombatAction effect;
	
	/**
	 * If this item is offensive and we think the player should use it on the enemy.
	 */
	public final boolean offensive;
	
	/**
	 * If dead creatures must be targeted for this item, or alive
	 */
	public final boolean targetDead;

	private boolean nonCombatAllowed;
	
	/**
	 * 
	 * @param name
	 * @param isCombatItem
	 * @param effect
	 * @param offensive
	 * @param targetDead
	 * @param nonCombatAllowed
	 */
	public ConsumeableItem(String name, boolean isCombatItem, String description, CombatAction effect, boolean offensive, boolean targetDead, boolean nonCombatAllowed) {
		super(name, isCombatItem, description);
		this.effect = effect;
		this.offensive = offensive;
		this.targetDead = targetDead;
		this.nonCombatAllowed = nonCombatAllowed;
	}
	
	public CombatAction getEffect(){
		return effect;
	}

	public boolean nonCombatAllowed() {
		return nonCombatAllowed;
	}

}
