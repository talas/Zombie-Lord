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

public class SecondTown extends Level {

	@Override
	public String getBackground() {
		return "secondtown/secondtown.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 2048, 2048);
	}

	@Override
	public String getForeground() {
		return "secondtown/secondtown-foreground.png";
	}
	
	@Override
	public int getCamMaxY(){
		return 2046;
	}
	
	@Override
	public int getCamMinY(){
		return 928;
	}
	
	@Override
	public int getCamMaxX(){
		return 1216;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{ // borders
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{  1,   1,1215,1215},
					new float[]{930,2016,2016, 930});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // inn
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{897,801,799,830,1149,1183,1183,1025,1021,928,928,897},
					new float[]{1797,1799,1972,1983,1983,1972,1604,1602,1795,1797,1811,1811});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // left house
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{415,385,382,415,510,542,539,447,446,414},
					new float[]{1253,1253,1428,1440,1439,1426,1255,1253,1268,1269});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // right house
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{798,769,767,799,927,958,955,830,830,798},
					new float[]{1348,1347,1492,1504,1504,1492,1348,1348,1362,1362});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // strange tent
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{965,965,1023,1081,1081},
					new float[]{1126,1227,1275,1227,1126});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // apple tree
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{649,649,660,660},
					new float[]{1286,1312,1312,1286});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // middle tree 1
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{457,457,469,469},
					new float[]{1542,1568,1568,1542});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // middle tree 2
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{553,553,564,564},
					new float[]{1607,1633,1633,1607});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // fir 1 (upper)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{327,327,342,342},
					new float[]{1893,1939,1939,1893});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // fir 2 (right)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{40,40,54,54},
					new float[]{1317,1366,1366,1317});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // fir 3 (south)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{840,840,854,854},
					new float[]{967,1013,1013,967});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestones (2 south)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{197,197,216,216},
					new float[]{1088,1149,1149,1087});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestone (leftmost)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{132,132,154,154},
					new float[]{1154,1181,1181,1157});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestone (middle)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{198,198,213,213},
					new float[]{1190,1210,1210,1190});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestone (rightmost)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{263,263,278,278},
					new float[]{1190,1211,1211,1190});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		
	}

	@Override
	public String getMusic() {
		// TODO Auto-generated method stub
		return "data/music/Nebur_Maik_-_Pretty_Baby__only_bells_.ogg";
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

		
		{ // entering inn
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(895,930,1794,1805, "start", "THE END", talk, 0);
			d.addLevelTransfer(new SecondTownInn1(), 177,1814, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		return dialogs;
	}

	@Override
	public Sprite foreground(Texture t) {
		return new Sprite(t, 0, 0, 2048, 2048);
	}

}
