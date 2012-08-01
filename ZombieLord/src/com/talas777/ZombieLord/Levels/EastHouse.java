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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.talas777.ZombieLord.Dialog;
import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.Monster;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.MonsterSetup;
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class EastHouse extends Level {

	@Override
	public String getBackground() {
		return "hometown/easthouse.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 512, 512);
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
					new float[]{223,223,185,185,171,171,159,159,190,190,159,159,215,215,255,258,288,288,
							297,297,329,329,320,320,351,359,256,256},
					new float[]{106,127,129,148,148,158,158,196,196,223,223,257,257,311,311,321,321,315,
							311,224,224,211,211,165,165,126,126,103});

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
		//position: x=283, y=207, time=east house-combat
		//position: x=481, y=300, time=east house-combat
		{
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai", "Zombies!");
			talk.add("Leoric", "Ready? Here we go!");
			
			Dialog d = new Dialog(0,470,165,300, "east house-combat", talk, 0);
			d.addTimeChange("leave east house");
			
			MonsterSetup setup = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			Monster zombie1 = new Monster("Zombie1","malesoldierzombie.png",5,20,15,3,1.25f);
			Monster zombie2 = new Monster("Zombie2","malesoldierzombie.png",5,20,15,3,1.25f);
			zombie1.addCombatAction(ZombieLord.bite);
			zombie2.addCombatAction(ZombieLord.bite);
			setup.appendMonster(zombie1);
			setup.appendMonster(zombie2);
			setup.exp = 25;
			
			d.addFight(setup);
			//d.addLevelTransfer(new HomeTownNight(), 2928, 1388, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		{
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai","Let's go to the house  south of here next.");
			
			Dialog d = new Dialog(223,256,103,123, "leave east house", talk, 0);
			d.addTimeChange("south east house?");

			d.addLevelTransfer(new HomeTownNight(), 3120, 1401, ZombieLord.DIR_SOUTH);
			
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
