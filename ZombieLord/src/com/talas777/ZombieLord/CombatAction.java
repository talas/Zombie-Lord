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

import com.badlogic.gdx.graphics.g2d.Sprite;

public class CombatAction {

	/*
	 * Because there will be few actions but many animations, all animations
	 * will be hard-coded somewhere else (?) 
	 */
	
	public static final int TARGET_ENEMY_SINGLE = 0;
	public static final int TARGET_ENEMY_ALL = 1;
	public static final int TARGET_SELF = 2;
	public static final int TARGET_ALLY_SINGLE = 3;
	public static final int TARGET_ALLY_ALL = 4;
	public static final int TARGET_ENEMY_RANDOM = 5;
	//public static final int TARGET_LAST_ATTACKER = 6;
	public static final int TARGET_RANDOM = 7;
	public static final int TARGET_ALL = 8;
	public static final int TARGET_ALL_OTHER = 9;
	
	public boolean isDistanceAttack = false;
	
	public String name;
	public String description;
	
	public int minLevel;
	public int maxLevel;
	
	public int hpCost;
	public int mpCost;
	
	/**
	 * A sprite that will be overlayed those that are affected by the action
	 */
	public Sprite overlaySprite;
	
	/**
	 * Can be negative to deal damage, or positive to heal.
	 */
	public float healthChange;
	
	public int targetType;
	
	/**
	 * Full constructor
	 * @param name Name of the action
	 * @param description Friendly description
	 * @param minLevel minimum xp level to use the action
	 * @param maxLevel maximum xp level to use the action
	 * @param hpCost health consumed by using the action
	 * @param mpCost mana consumed by using the action
	 * @param healthChange how this action affects the health of the target
	 * @param targetType what this action targets
	 */
	public CombatAction(String name, String description, int minLevel, int maxLevel, int hpCost, int mpCost, float healthChange, int targetType){
		this.name = name;
		this.description = description;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.hpCost = hpCost;
		this.mpCost = mpCost;
		this.healthChange = healthChange;
		this.targetType = targetType;
	}
	
	/**
	 * Constructor for most monster actions
	 * @param name
	 * @param mpCost
	 * @param healthChange
	 * @param targetType
	 */
	public CombatAction(String name, int mpCost, float healthChange, int targetType){
		this(name, "",0,0,0,mpCost,healthChange,targetType);
	}
	
	public CombatAction(String name, String description, int mpCost, float healthChange, int targetType){
		this(name, description,0,0,0,mpCost,healthChange,targetType);
	}
}
