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

public class Monsters {

	
	public static final MonsterType Zombie = new MonsterType("Zombie", 5, 2, 1, 1, 1, 1, 1);
	
	public static final MonsterType Troll = new MonsterType("Troll", 10, 13, 2, 3, 4, 9, 4);
	
	public static final MonsterType Manticore = new MonsterType("Manticore", 20, 12, 9, 6, 5, 14, 8);
	
	
	
	public static void initiate(){
		Zombie.setImage("monsters/malesoldierzombie.png", 128, 128);
		Troll.setImage("monsters/troll.png", 128, 128);
		Manticore.setImage("monsters/manticore.png", 128, 128);
	}
}
