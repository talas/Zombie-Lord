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

package com.talas777.ZombieLord.Minigames;

import java.util.LinkedList;
import java.util.ListIterator;

import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.ZombieLord;
import com.talas777.ZombieLord.Minigames.TowerDefense.Attacker;
import com.talas777.ZombieLord.Minigames.TowerDefense.Cursor;
import com.talas777.ZombieLord.Minigames.TowerDefense.Defender;

public abstract class ZombieDefense {

	private final Level nextLevel;
	private final int nextPosX;
	private final int nextPosY;
	
	private final int numWaves;
	private int currentWaveNumber;
	
	public LinkedList<Defender> defenders;
	public LinkedList<Attacker> attackers;
	
	/**
	 * Money earned from killing zombies, can be spent to buy defenses.
	 */
	public int money;
	
	public int minx;
	public int miny;
	public int maxx;
	public int maxy;
	
	public Cursor cursor;
	
	private int getDistanceTo(int fromx, int fromy, int tox, int toy){
		// square distance, since nothing can move diagonally
		// Note, if something can move diagonally one day, this method might require an update
		
		int diffx = Math.abs(fromx - tox);
		int diffy = Math.abs(fromy - toy);
		
		return (diffx+diffy);
	}
	
	private boolean isTileEmpty(int x, int y){
		for(Attacker a : attackers){
			if(a.getX() == x && a.getY() == y)
				return false;
		}
		for(Defender d : defenders){
			if(d.getX() == x && d.getY() == y)
				return false;
		}
		return true;
	}
	
	public abstract LinkedList<Defender> getAvailableDefenses();
	
	private Defender defenderToPlace;
	
	public Defender cyclePlaceableDefenders(){
		
		LinkedList<Defender> defenders = getAvailableDefenses();
		
		
		
		int x = 0; //defenders.lastIndexOf(defenderToPlace);
		
		for(Defender d : defenders){
			if(d.equals(defenderToPlace))
				break;
			else
				x++;
		}
		
		x++;
		if(x >= defenders.size())
			x = 0;
		
		defenderToPlace = defenders.get(x);
		System.out.println("selected: "+defenderToPlace);
		this.cursor.cycleSelector();
		return defenderToPlace;
	}
	
	public boolean addDefense(){
		Defender d = defenderToPlace.clone();
		int x = cursor.x;
		int y = cursor.y;
		if(d.cost <= money){
			if(isTileEmpty(x,y)){
				d.setPos(x, y);
				defenders.add(d);
				money -= d.cost;
				return true;
			}
		}
		return false;
	}
	
	public abstract int getMoneyForWave(int waveNumber);
	
	/**
	 * 
	 * @param x position of attacker
	 * @param y position of attacker
	 * @return direction that the attacker should go next. -1 = dont move.
	 */
	public byte findPath(int x, int y, Attacker atk){
		// TODO: consider hindrances(defenders)
		// TODO: actual smart algorithm here.. has to be very fast or doing lookups instead of calculations
		// TODO: zombies learn which paths hold merit (homemade <ant colony optimization + evolutionary algorithms>)
		
		LinkedList<Byte> possible = new LinkedList<Byte>();

		boolean canGoEast = true;
		boolean canGoWest = true;
		boolean canGoNorth = true;
		boolean canGoSouth = true;
		
		
		if(x <= minx)
			canGoWest = false;
		if(x >= maxx)
			canGoEast = false;
		if(y <= miny)
			canGoSouth = false;
		if(y > maxy)
			canGoNorth = false;
		
		
		if(canGoEast){
			// check if tile is empty
			if(!isTileEmpty(x+1, y))
				canGoEast = false;
		}
		if(canGoWest){
			// check if tile is empty
			if(!isTileEmpty(x-1, y))
				canGoWest = false;
		}
		if(canGoNorth){
			// check if tile is empty
			if(!isTileEmpty(x, y+1))
				canGoNorth = false;
		}
		if(canGoSouth){
			// check if tile is empty
			if(!isTileEmpty(x, y-1))
				canGoSouth = false;
		}
		
		
		
		
		if(canGoEast)
			possible.add((byte)ZombieLord.DIR_EAST);
		if(canGoWest)
			possible.add((byte)ZombieLord.DIR_WEST);
		if(canGoNorth)
			possible.add((byte)ZombieLord.DIR_NORTH);
		if(canGoSouth)
			possible.add((byte)ZombieLord.DIR_SOUTH);
		
		if(possible.size() == 0)
			return -1;
		
		
		byte programDir = atk.nextDir();
		for(Byte b : possible){
			if(b.byteValue() == programDir){
				// valid, chose it?
				if(Math.random() < atk.chance()){
					// yep
					return b;
				}
			}
		}
		
		int sel = (int)(Math.random()*possible.size());
		System.out.println("sel:"+sel);
		
		Byte dir = possible.get((int)(Math.random()*possible.size()));
		
		if(dir == null)
			return -1;
		
		return dir;
	}
	
	public ZombieDefense(int numWaves, Level nextLevel, int nextPosX, int nextPosY){
		this.numWaves = numWaves;
		this.nextLevel = nextLevel;
		this.nextPosX = nextPosX;
		this.nextPosY = nextPosY;
		this.currentWaveNumber = 1;
		this.defenders = new LinkedList<Defender>();
		this.cursor = new Cursor();
		this.defenderToPlace = this.getAvailableDefenses().getFirst();
	}
	
	public abstract boolean isDefeatAllowed();
	public abstract int getHealthLeft();
	
	public final int getCurrentWaveNumber(){
		return this.currentWaveNumber;
	}
	
	public final void gotoNextWave(){
		if(this.currentWaveNumber >= numWaves)
			return;
		
		this.money += this.getMoneyForWave(this.currentWaveNumber);
		
		this.currentWaveNumber++;
		System.out.println("Wave: "+this.currentWaveNumber);
	}
	
	public final int getNumWaves(){
		return numWaves;
	}
	
	public final Level getNextLevel(){
		return nextLevel;
	}
	
	public final int getNextLevelX(){
		return nextPosX;
	}
	
	public final int getNextLevelY(){
		return nextPosY;
	}
	
	public abstract boolean hasReachedGate(Attacker attacker);
	
	public abstract void loseHealth();
	
	public LinkedList<Attacker> handleAttackers(LinkedList<Attacker> attackers){
		
		ListIterator<Attacker> it = attackers.listIterator();
		while(it.hasNext()){
			Attacker current = it.next();
			if(current.health <= 0){
				// kill attacker
				// earn money
				System.out.println("Killed an attacker.");
				this.money += current.getMoneyReward();
				it.remove();
				continue;
			}
			if(hasReachedGate(current)){
				// lose health & kill attacker
				System.out.println("An Attacker reached the gate!");
				this.loseHealth();
				it.remove();
				continue;
			}
		}
		
		return attackers;
	}
	
	/**
	 * Creates and returns the list of attackers for the next wave.
	 * Note that the function is undefined for wave numbers higher than the last wave.
	 * @param waveNumber which wave to calculate for, starts at 1.
	 * @return LinkedList<Attacker> containing all the attackers of this wave, in the order that they should appear.
	 */
	public abstract LinkedList<Attacker> getWave(int waveNumber);
	
	
}
