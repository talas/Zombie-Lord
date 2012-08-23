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

package com.talas777.ZombieLord.Minigames.TowerDefense;

import java.util.LinkedList;

import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.Levels.MyHouse;
import com.talas777.ZombieLord.Minigames.ZombieDefense;

public class HomeTownDefense extends ZombieDefense {

	private int healthLeft;
	
	public HomeTownDefense() {
		super(50, new MyHouse(), 460, 410);
		
		this.healthLeft = 20;
		
		this.minx = 0;
		this.miny = 0;
		
		this.maxx = 15-1;
		this.maxy = 10-1;
		this.money = 45;
	}

	@Override
	public LinkedList<Attacker> getWave(int waveNumber) {
		LinkedList<Attacker> attackers = new LinkedList<Attacker>();
		for(int i = 0; i < waveNumber; i++){
			Attacker attacker = new Attacker("data/zd/zombie.png", 10, 0, 1f);
			
			if(i > this.maxx*2)
				attacker.setPos(i-this.maxx*2, -2);
			else if(i > this.maxx)
				attacker.setPos(i-this.maxx, -1);
			else
				attacker.setPos(i, 0);
			
			attackers.add(attacker);
		}
		return attackers;
	}

	@Override
	public int getHealthLeft() {
		return healthLeft;
	}
	
	

	@Override
	public boolean isDefeatAllowed() {
		return true;
	}

	@Override
	public boolean hasReachedGate(Attacker attacker) {
		if(attacker.getY() >= 10)
			return true;
		return false;
	}

	@Override
	public void loseHealth() {
		healthLeft = (healthLeft == 0 ? 0 : healthLeft-1);
	}

	@Override
	public LinkedList<Defender> getAvailableDefenses() {
		// first level defenses = wall and archer
		// TODO: traps?
		Defender archer = new Defender("data/zd/archer.png",5,20,3,4,2,2.2f);
		Defender wall = new Defender("data/zd/wall.png",20,5,0,0,0,99);
		
		
		LinkedList<Defender> defenders = new LinkedList<Defender>();
		defenders.add(archer);
		defenders.add(wall);
		
		
		return defenders;
	}

	@Override
	public int getMoneyForWave(int waveNumber) {
		return Math.min(waveNumber+5,10);
	}

}
