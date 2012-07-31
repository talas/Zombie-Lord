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

public class MonsterSetup {
	//TODO: MonsterSetup, loot, gold, dialog, ++
	
	private int formation;
	
	/**
	 * 2 monsters front, 3 monsters back
	 */
	public static final int FORMATION_SIMPLE = 0;
	
	/**
	 * force all monsters on a single row (might get ugly)
	 */
	public static final int FORMATION_ALL_FRONT = 1;
	
	/**
	 * 1 monster front, 4 back
	 */
	public static final int FORMATION_SINGLE_FRONT = 2;
	
	/**
	 * 3 monsters front, 1 back
	 */
	public static final int FORMATION_SINGLE_BACK = 3;
	
	private LinkedList<Monster> monsters;
	
	public MonsterSetup(int formation){
		this.formation = formation;
		this.monsters = new LinkedList<Monster>();
	}
	
	/**
	 * Adds a monster to the next empty spot in the formation
	 * @param monster
	 */
	public void appendMonster(Monster monster){
		monsters.add(monster);
	}
	
	public LinkedList<Monster> getMonsters()
	{
		return monsters;
	}
	
	public int getFormation(){
		return formation;
	}
}
