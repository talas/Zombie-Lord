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
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class Church extends Level {

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return "hometown/church.png";
	}

	@Override
	public Sprite background(Texture t) {
		// TODO Auto-generated method stub
		return new Sprite(t, 0, 0, 1024,1024);
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
		{ // church
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{439,439,477,477,566,566,563,470,470,442,442,425,425,440,440,603,603,538,538,505,505},
					new float[]{389,449,449,497,497,540,527,527,478,478,485,485,518,518,614,614,388,388,380,380,389});

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
		
		//TODO: find real positions..
		
		{ // The first dialog.. Hello World!
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "Huh, what happened?");
			talk.add("Leoric", "Why am I ... here?");
			talk.add("Leoric", "It's late, I should go home and talk to ... my .. Wife");
			
			Dialog d = new Dialog(0,600,0,600, "zero", talk, 0);
			d.addTimeChange("start");
			d.setQuestHint("Find out where you live");
			dialogs.add(d);
		}
		{
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(500,550,0,395, "start", talk, 0);
			d.addLevelTransfer(new HomeTownNight(), 1775, 305, ZombieLord.DIR_SOUTH);
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
