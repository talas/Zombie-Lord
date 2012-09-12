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

import com.badlogic.gdx.graphics.Texture;

public abstract class NPC extends LevelObject {

	public NPC(int posx, int posy, Texture texture, int sizex, int sizey, boolean collision) {
		super(posx, posy, texture, sizex, sizey, collision);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interact(Party p, QuestTracker quests) {
		// TODO Auto-generated method stub

	}

}
