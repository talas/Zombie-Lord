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

/**
 * First floor of the inn in the second town
 * @author talas
 *
 */
public class SecondTownInn1 extends Level {

	@Override
	public String getBackground() {
		return "secondtown/st-inn-1.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 2048, 2048);
	}

	@Override
	public String getForeground() {
		return "secondtown/st-inn-1-foreground.png";
	}

	@Override
	public Sprite foreground(Texture t) {
		return new Sprite(t, 0, 0, 2048, 2048);
	}

	@Override
	public int getCamMaxY(){
		return 2046;
	}
	
	@Override
	public int getCamMinY(){
		return 1408;
	}
	
	@Override
	public int getCamMaxX(){
		return 642;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{ // everything
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{136,136,30,30,8,8,32,32,34,34,62,62,64,64,98,98,142,142,320,320,384,384,457,457,423,423,457,457,423,423,457,457,423,423,457,457,423,423,461,461,632,632,360,360,217,217},
					new float[]{1782,1790,1790,1828,1828,1954,1954,1893,1893,1950,1950,1893,1893,1954,1954,1935,1935,1878,1878,1924,1924,1957,1957,1918,1918,1890,1890,1854,1854,1795,1795,1726,1726,1635,1635,1598,1598,1541,1541,1501,1501,1439,1439,1790,1790,1782});

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
		// TODO Auto-generated method stub
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();

		
		{ // leaving inn
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(136,217,1770,1793, "start", "THE END", talk, 0);
			d.addLevelTransfer(new SecondTown(), 913, 1766, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		{ // second floor
			TalkScript talk = new TalkScript();
			
			// return address (if undeliverable: 48, 1911)
			Dialog d = new Dialog(44,50,1936,1950, "start", "THE END", talk, 0);
			d.addLevelTransfer(new SecondTownInn2(), 48,1931, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		return dialogs;
	}

}
