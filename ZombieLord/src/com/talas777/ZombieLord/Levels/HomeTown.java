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
import com.talas777.ZombieLord.TimeTracker;

public class HomeTown extends Level {

	@Override
	public String getBackground() {
		return "hometown/hometown.png";
	}
	
	public Sprite background(Texture t){
		return new Sprite(t, 0, 0, 3200, 3200);
	}

	@Override
	public String getForeground() {
		// TODO foreground
		return null;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		// TODO Auto-generated method stub
		
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{ // church
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{1473,1473,1507,1700,1775,1861,1950,1985,1985,1795,1795,1758,1758},
					new float[]{322,450,480,480,565,480,480,450,322,322,342,342,322});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestone 1
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{1898,1898,1941,1941},
					new float[]{225,265,265,225});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestone 2
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{1953,1953,1985,1985},
					new float[]{288,322,322,288});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestones 3
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{1989,1989,2106,2106},
					new float[]{226,254,254,226});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestones 4
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{2026,2026,2043,2043,2047,2047,2107,2107,2069,2069},
					new float[]{289 , 329, 340, 350, 350, 382, 382, 352, 352, 289});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestones 5
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{1985,1985,2008,2000},
					new float[]{353 , 382, 382, 353});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // gravestone 6
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{2110,2110,2129,2138,2144},
					new float[]{288 , 352, 352, 345, 290});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // farm fence (only outer)
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{2445,2445,2476,2476,2975,2975,2931,2931,3040,3040,3008,3008,3085,3085, 3200},
					new float[]{1   , 734, 734, 797, 797, 774, 774, 734, 734, 776, 776, 797, 797, 828, 828});

			environmentShape.createChain(vertices); // not a loop!
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		
		{ // pond (near square)
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{1906,1906,1961,1971,2026,2026,2002,2002,2022,2047,2150,2162,2162,2217,2224,2274,2289,2315,2324,2380,2380,2305,2293,2280
							,2200,2190,2159,2159,2135,2122,2002,2002,1970,1970},
					new float[]{1048,1135,1135,1108,1108,1127,1151,1194,1194,1232,1232,1254,1293,1293,1172,1171,1143,1136,1108,1101,1010,1010,1000,979,979
							,1001,1001,952,939,883,883,935,953,1033});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		
		{ // top houses and fence
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{3206,3138,3138,3099,3099,2946,2946,2908,2908,2880,2880,2754,2754,2717,2717,2689,2689,
							2654,2654,2563,2563,2348,2348,2304,2304,2207,2207,2112,2112,2077,2077,2020,2016,1922,1922,1953,
							2269,2303,2303,2403,2403,2369,2388,2479},
					new float[]{1409,1409,1446,1446,1408,1408,1439,1439,1409,1409,1440,1440,1505,1505,1442,1442,1477,
							1477,1440,1440,1512,1512,1799,1799,1666,1666,1536,1536,1569,1569,1536,1536,1730,1730,1974,1983,
							1983,1974,1822,1822,1859,1874,1925,1948});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // bottom right house
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{2945,2945,2974,3103,3136,3136,3042,3042},
					new float[]{1122,1301,1310,1310,1301,1090,1090,1121});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // bottom left house
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{2658,2658,2627,2626,2657,2848,2880,2876,2845,2845,2816,2816,2784,2784},
					new float[]{1121,1154,1154,1332,1342,1342,1334,1152,1152,1120,1120,1152,1152,1120});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // big tree (at square)
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{1771,1750,1702,1698,1709,1750,1766,1792,1824,1824,1816,1797},
					new float[]{1417,1417,1439,1480,1539,1583,1592,1580,1515,1496,1469,1453});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // border, fences, water
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
			new float[]{3197,1008,1008,950,942,916,910,910,910,980,980,950,950,1195,1360,1335,1423,1423,1477,1508,
					1544,1545,1545,1480,1489,1469,1457,1457,1430,1325,1325,1354,1354,1298,1291,1291,1266,1262,1231,
					1226,1226,1194,1113,1076,1075,1075,1041,1041, 915, 914, 897, 533, 533,1328,1328,1427,1454,1454,
					1504,1539,1677,1759,1759,1654,1653,2357,2357,2700,2700,2796,2796,2755,2716,2715,2631,2587,2539,
					2533,2533,2484,2467,2386,2377,2383,2382,3198},
			new float[]{   1,   2,  82, 82,115,119,147,198,199,257,518,540,683, 687, 865, 911, 911, 978, 979,1012,
					1012,1012,1041,1105,1153,1178,1209,1389,1406,1402,1342,1320,1241,1199,1147,1146,1133, 984, 969,
					 921, 920, 888, 888, 922, 922,1006,1007, 980, 980, 980,1008,1019,1558,1576,1442,1442,1466,1535,
					1584,1584,1714,1714,1781,1782,2888,2888,2922,2922,2728,2728,2377,2327,2327,2327,2232,2232,2172,
					2137,2136,2128,2040,2035,1958,1938,1938,1467});
			
			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // water
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{1793,1793,1793,1835,1853,1910,1910,1876,1862,1845,1831},
					new float[]{1712,1776,1777,1777,1809,1809,1778,1770,1743,1737,1716});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // stall1
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{2945,2945,3007,3007},
					new float[]{885,1009,1009,885});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // stall2
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{3039,3039,3103,3104},
					new float[]{887,1009,1009,888});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // stall3
			ChainShape environmentShape = new ChainShape();
			

			Vector2[] vertices = vectorize(
					new float[]{3136,3136,3166,3167},
					new float[]{888,976,976,888});

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
		return null;
	}

	@Override
	public LinkedList<Dialog> getLevelDialogs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sprite foreground(Texture t) {
		// TODO Auto-generated method stub
		return null;
	}

}
