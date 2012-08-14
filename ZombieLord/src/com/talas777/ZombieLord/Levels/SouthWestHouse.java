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

public class SouthWestHouse extends Level {

	@Override
	public String getBackground() {
		return "hometown/southwesthouse.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 640, 512);
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
					new float[]{303,303,224,223,208,196,192,159,159, 58, 58, 41, 32, 32, 72, 72,118,118,159,159,193,193,223,223,
							288,288,323,323,335,349,349,374,373,384,395,400,448,447,447,480,480,544,544,576,576,607,607,507,507,
							484,484,461,461,609,609,480,480,426,418,334,334},
					new float[]{ 49, 63, 63,111,125,125,157,159,193,193,243,255,255,316,316,263,263,320,320,316,316,321,321,294,
							294,320,320,281,278,281,305,305,291,290,300,321,321,315,314,314,320,320,298,298,319,318,256,256,286,
							286,255,255,220,220,191,191, 92, 92, 64, 64, 48});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}

	}

	@Override
	public String getMusic() {
		// TODO Auto-generated method stub
		return "data/music/Mark_Subbotin_-_Phoenix.ogg";
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
		// 1 zombie to kill..
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();
		//position: x=283, y=207, time=east house-combat
		//position: x=481, y=300, time=east house-combat
		{
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(0,770,170,300, "south west house!", talk, 0);
			d.addTimeChange("leave sw house");
			
			MonsterSetup setup = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			Monster zombie1 = new Monster("Zombie1","monsters/malesoldierzombie.png",5,20,15,3,1.25f);

			zombie1.addCombatAction(ZombieLord.BITE);

			setup.appendMonster(zombie1);
			setup.exp = 10;

			
			d.addFight(setup);
			//d.addLevelTransfer(new HomeTownNight(), 2928, 1388, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		{
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai","Let's go to the house  north of this one.");
			
			Dialog d = new Dialog(303,334,48,62, "leave sw house", talk, 0);
			d.addTimeChange("west house?");

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
