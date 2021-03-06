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

public class ActionCategory {
	
	
	private final byte id;

	public ActionCategory(byte id){
		this.id = id;
	}
	
	public String getCategoryName(){
		switch(id){
		case 0:
			return "Offensive Magic";
		case 1:
			return "Defensive Magic";
		case 2:
			return "Special";
		case 3:
			return "Summon";
		case 4:
			return "Attack";
		default:
			return "Monster Ability";
		}
		
	}
}
