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

public class MayorHouse extends Level {

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return "hometown/mayorhouse.png";
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
		{ // walls and near walls
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{292,292,192,192,288,288,511,511,544,544,607,607,575,575,607,607,540,540,578,578,560,
							544,544,515,461,461,479,479,464,449,449,376,376},
					new float[]{0  , 30, 30,255,256,541,541,516,516,539,539,512,512,487,487,191,191,231,231,277,351,
							275,235,224,224,182,182, 97, 97, 75, 32, 32,  2});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // table and chairs
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{409,409,418,504,517,542,542,534,519,505,421},
					new float[]{337,413,447,447,412,413,386,349,337,321,321});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // table and chairs
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{234,240,230,221,221,210,192},
					new float[]{ 30, 75, 93,105,125,133,133});

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
		// empty
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();
		{
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(0,770,170,300, "mayors house?", talk, 0);
			d.addTimeChange("mayors house!");
			
			MonsterSetup setup = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			Monster zombie1 = new Monster("Zombie1","malesoldierzombie.png",5,20,15,3,1.25f);
			Monster zombie2 = new Monster("Zombie2","malesoldierzombie.png",5,20,15,3,1.25f);
			Monster zombie3 = new Monster("Zombie3","malesoldierzombie.png",5,20,15,3,1.25f);
			Monster zombie4 = new Monster("Zombie4","malesoldierzombie.png",5,20,15,3,1.25f);
			zombie1.addCombatAction(ZombieLord.bite);
			zombie2.addCombatAction(ZombieLord.bite);
			zombie3.addCombatAction(ZombieLord.bite);
			zombie4.addCombatAction(ZombieLord.bite);
			setup.appendMonster(zombie1);
			setup.appendMonster(zombie2);
			setup.appendMonster(zombie3);
			setup.appendMonster(zombie4);
			setup.exp = 100;
			
			d.addFight(setup);
			
			dialogs.add(d);
		}
		
		{
			TalkScript talk = new TalkScript();
			talk.add("Leoric", "Theres so many!");
			talk.add("Tolinai", "We have to get out of  here!");
			
			Dialog d = new Dialog(0,900,0,900, "mayors house!", talk, 0);
			d.addTimeChange("leave mayors house");
			d.addLevelTransfer(new HomeTownNight(), 2093, 1521, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		/*{
			TalkScript talk = new TalkScript();
			

			
			Dialog d = new Dialog(292,376,2,31, "leave mayors house", talk, 0);


			d.addLevelTransfer(new HomeTownNight(), 2093, 1521, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}*/
		
		
		
		
		return dialogs;
	}

	@Override
	public Sprite foreground(Texture t) {
		// TODO Auto-generated method stub
		return null;
	}

}
