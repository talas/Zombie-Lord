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

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.talas777.ZombieLord.Dialog;
import com.talas777.ZombieLord.Item;
import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.LevelItem;
import com.talas777.ZombieLord.LevelObject;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.QuestTracker;
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class MyHouse extends Level {

	@Override
	public String getBackground() {
		return "hometown/myhouse.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 640, 640);
	}

	@Override
	public String getForeground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{ // everything
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{420,420,288,288, 32, 32,223,223,254,254,223,223,351,351,224,224,255,259,276,290,319,319,349,370,393,414,420,454,
							454,579,579,607,607,580,580,607,607,580,580,477,477,416},
					new float[]{161,190,190,288,288,321,321,385,385,414,414,439,439,479,479,543,543,517,517,535,535,524,524,519,513,513,544,544,
							448,448,398,398,318,318,287,287,253,244,191,191,163,163});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		
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
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();
		
		{
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai", "Oh, Leoric you're back. Thank goodness you are safe!");
			talk.add("Leoric", "Honey, what's going on? I can't remember anything!");
			talk.add("Tolinai", "But, you remember me   right?");
			talk.add("Leoric", "Yes...  ..");
			talk.add("Tolinai", "That's good, you almost had me worried there.");
			talk.add("Leoric", "Why is there nobody     outside? It seemed so empty out there.");
			talk.add("Tolinai", "Rumor has it that      Zombies are coming to destroy   the village. Everyone is hiding.");
			talk.add("Leoric", "We must do something!");
			talk.add("Tolinai", "Are you sure you are   ready to fight?");
			talk.add("Leoric", "Yes, but will you help  me with your magic?");
			talk.add("Tolinai", "Of course! It will be  just like old times.");
			talk.add("Tolinai", "Here, take your sword. I have my staff ready. Lets go  to each house and help fend off the zombies!");
			
			Dialog d = new Dialog(469,567,297,398, "talk with gf", talk, 0);
			d.addTimeChange("leave home");
			d.addMemberGain(ZombieLord.Tolinai);
			
			dialogs.add(d);
		}
		{
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(420,470,160,185, "leave home", talk, 0);
			d.addTimeChange("east house?");
			d.addLevelTransfer(new HomeTownNight(), 2928, 1388, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		
		
		
		return dialogs;
	}

	@Override
	public Sprite foreground(Texture t) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public LinkedList<LevelObject> getLevelObjects(QuestTracker tracker){
		LinkedList<LevelObject> objects = new LinkedList<LevelObject>();
		
		if(!tracker.isCompleted("First potion.")){
			LevelItem topas = new LevelItem(400, 450, Item.Potion, (byte)1, "First potion.");
			objects.add(topas);
		}
		
		return objects;
	}

}
