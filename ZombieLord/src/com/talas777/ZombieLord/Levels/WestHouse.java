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

package com.talas777.ZombieLord.Levels;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.talas777.ZombieLord.Dialog;
import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.Monster;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.MonsterSetup;
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class WestHouse extends Level {

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return "prerenders/westhouse.png";
	}

	@Override
	public Sprite background(Texture t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getForeground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getMusic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevelTransfer(int posx, int posy, TimeTracker timer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LinkedList<MonsterArea> getMonsterAreas(TimeTracker timer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Dialog> getLevelDialogs() {
		// empty
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();
		{
			TalkScript talk = new TalkScript();
			talk.add("Leoric", "Nobody?");
			talk.add("Tolinai", "They must be hiding at the Mayors house west of here. Let's go there!");
			
			Dialog d = new Dialog(0,770,170,300, "west house!", talk, 0);
			d.addTimeChange("mayors house?");
			
			dialogs.add(d);
		}
		
		{
			TalkScript talk = new TalkScript();
			

			
			Dialog d = new Dialog(303,334,48,62, "mayors house?", talk, 0);


			d.addLevelTransfer(new HomeTownNight(), 2800, 1116, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		
		
		
		return dialogs;
	}

	@Override
	public Sprite foreground(Texture t) {
		// TODO Auto-generated method stub
		return null;
	}

}
