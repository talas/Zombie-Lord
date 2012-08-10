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

public class Chest extends LevelObject {

	private boolean squareAppearance;
	private boolean open;
	private Item itemType;
	private byte itemCount;
	private String uniqueId;
	private String dialogString;
	
	public Chest(int posx, int posy, boolean squareType, Item type, byte count, String uniqueId, boolean open) {
		super(posx, posy, new Texture("data/chests.png"), 32, 32, true);
		this.squareAppearance = squareType;
		this.open = open;
		this.itemType = type;
		this.itemCount = count;
		this.uniqueId = uniqueId;
		updateSprite();
	}

	private void updateSprite(){
		sprite.setRegion((squareAppearance?0:32), (open?32:0), 32, 32);
	}
	
	public String getDialogString(){
		if(dialogString == null)
			return null;
		String tmp = dialogString;
		dialogString = null;
		
		return tmp;
	}
	
	@Override
	public void interact(Party p, QuestTracker quests) {
		if(quests.isCompleted(uniqueId)){
			dialogString = "It's empty..";
			return; // empty. TODO: tell the party its empty?
		}
		
		if(p.giveItem(itemType, itemCount)){
			quests.setCompleted(uniqueId);
			this.open = true;
			this.updateSprite();
			dialogString = "You aquired "+itemCount+"x "+itemType.name+"!";
		}
		else {
			dialogString = "Couldn't take the "+itemCount+"x "+itemType.name+", inventory is full.";
		}
	}

}
