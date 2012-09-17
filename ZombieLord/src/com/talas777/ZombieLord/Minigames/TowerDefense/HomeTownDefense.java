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
		// every wave, 1 more zombie and slightly +speed
		// every 5 wave hp += 2
		int addhp = (int)(Math.floor(waveNumber/5)*2);
		float speed = (float)(1f - (0.01f*waveNumber));

		for(int i = 0; i < waveNumber; i++){
		    Attacker attacker = new Attacker("data/zd/zombie.png", 10+addhp, 0, speed);
			
		    int row = (int)(Math.floor(i/this.maxx));
			/*for(int j = 0; i < this.maxx*j; j++)
			  row = j;*/
			attacker.setPos(i-this.maxx*row, -row);
			
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
		Defender archer = new Defender("data/zd/archer.png",5,20,6,4,2,2.2f);
		Defender wall = new Defender("data/zd/wall.png",70,5,0,0,0,99);
		
		
		LinkedList<Defender> defenders = new LinkedList<Defender>();
		defenders.add(archer);
		defenders.add(wall);
		
		
		return defenders;
	}

	@Override
	public int getMoneyForWave(int waveNumber) {
		return Math.min(waveNumber+5,10);
	}

	@Override
	public String getMusic() {
		return "data/music/A.K.1974_-_Flight_Over_The_Green_Fields.ogg";
	}

}
