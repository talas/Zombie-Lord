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

	
	public static final MonsterType Zombie = new MonsterType("Zombie", 5, 1, 1, 1, 1, 1, 1, 6.66667f, 1.66667f);
	
	public static final MonsterType Troll = new MonsterType("Troll", 10, 13, 2, 3, 4, 9, 4, 11.11111f, 5.5555f);
	
	public static final MonsterType Manticore = new MonsterType("Manticore", 20, 12, 9, 6, 5, 14, 8, 33.333333f, 23.8095f);
	
	
	
	protected final static void initiate(){
		Zombie.setImage("monsters/malesoldierzombie.png", 64, 64);
		Zombie.addCombatAction(ZombieLord.BITE,1);
		
		Troll.setImage("monsters/troll.png", 64, 64);
		Troll.addCombatAction(ZombieLord.BITE,1);
		Troll.addCombatAction(ZombieLord.PUNCH,1);
		Troll.addCombatAction(ZombieLord.TWINFIST,7);
		Troll.addCombatAction(ZombieLord.REGROWTH,9);
		
		Manticore.setImage("monsters/manticore.png", 128, 128);
		Manticore.addCombatAction(ZombieLord.BITE,1);
		Manticore.addCombatAction(ZombieLord.ROULETTE_STING,13);
		Manticore.addCombatAction(ZombieLord.GRAND_CLAW,5);
		Manticore.addCombatAction(ZombieLord.MAGIC_ARROW,9);
	}
}
