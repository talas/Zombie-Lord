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

import java.util.LinkedList;

/**
 * 
 * @author talas
 * Note, this class is pixel perrfect. all positions are counted as pixels, not box2d stuffs.
 */
public class MonsterArea {

	public final int minx, maxx;
	public final int miny, maxy;
	
	private LinkedList<MonsterSetup> monsterSetups;
	private LinkedList<Float> setupWeights;
	
	public final float encounterChance;
	
	public MonsterArea(int minx, int maxx, int miny, int maxy, float encounterChance){
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
		this.monsterSetups = new LinkedList<MonsterSetup>();
		this.setupWeights = new LinkedList<Float>();
		this.encounterChance = encounterChance;
	}
	
	public void addMonsterSetup(MonsterSetup setup, float setupWeight){
		monsterSetups.add(setup);
		setupWeights.add(setupWeight);
	}
	
	public boolean isInside(int posx, int posy){
		return (posx >= minx && posx <= maxx && posy >= miny && posy <= maxy);
	}
	
	public MonsterSetup getRandomSetup(){
		// TODO: roulette wheel here..
		
		
		return monsterSetups.get((int) Math.random()*monsterSetups.size());
	}
}
