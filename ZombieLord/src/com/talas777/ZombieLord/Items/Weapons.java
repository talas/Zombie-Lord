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

import com.talas777.ZombieLord.ZombieLord;

public class Weapons {
	public static final Sword shortSword = new Sword("Short Sword","Leorics old sword", 10, 1,0,0);
	public static final Staff woodenStick = new Staff("Wooden Stick","Tolinai's worn out staff", 5, 1, 10, 4);
	
	private static boolean initialized = false;
	public static void initialize(){
		if(initialized){
			System.err.println("Weapons already initialized!");
			System.exit(1);
		}
		initialized = true;
		
		woodenStick.addElementOffense(ZombieLord.ELEM_EARTH, 5);
	}
}
