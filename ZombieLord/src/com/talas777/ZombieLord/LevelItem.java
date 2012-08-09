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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

public class LevelItem extends LevelObject {

	private Item itemType;
	private byte itemCount;
	private String uniqueId;

	
	
	public LevelItem(int posx, int posy, Item type, byte count, String uniqueId){
		super(posx, posy, new Texture("data/item.png"),16,20,false);
		this.uniqueId = uniqueId;
		this.itemType = type;
		this.itemCount = count;
	}
	
	/**
	 * The type of item the player should get.
	 * Note that you must use getItemCount() also, to find out how many he/she should get.
	 * @return Item type given
	 */
	public Item getItemType(){
		return itemType;
	}
	
	/**
	 * Ammount of items that should be given.
	 * NOTE: I wonder if the player should lose items if this number is negative..
	 * @return int ammount of items given
	 */
	public int getItemCount(){
		return itemCount;
	}

	@Override
	public void interact(Party p, QuestTracker quests) {
		if(p.giveItem(itemType, itemCount)){
			quests.setCompleted(uniqueId);
			this.delete();
		}
		
	}
}
