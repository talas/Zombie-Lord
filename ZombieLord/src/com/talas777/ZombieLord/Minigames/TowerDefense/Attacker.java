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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.talas777.ZombieLord.Minigames.ZombieDefense;
import com.talas777.ZombieLord.ZombieLord;

import java.util.ListIterator;

public class Attacker {
	public int health;
	public final int healthMax;
	public int mana;
	private float tileDelay;
	//private String appearance;
	
	protected int posx;
	protected int posy;
	
	protected Sprite s;
	private byte lastDir;
	private float lastPathFind;
	private float moveTime;
        private float attackChance = 0.3f;
        private float attackStrength = 1;
        private int chargeLevel = 0;
	
	public int getX(){
		return posx;
	}
	
	public int getY(){
		return posy;
	}
	
	public void setPos(int x, int y){
		this.posx = x;
		this.posy = y;
	}
	
	/*
	 * Limits the rate at which the pathfinding algorithm is called to find a nice path.
	 * No matter how nice the pathfinding algorithm gets, it will probably eat cpu as number of attackers increase.
	 * Higher limit = less cpu usage, but maybe dumber mobs.
	 * Note: this limit needs tweaking, maybe even platform dependent..
	 */
	//private static final float pathFindRate = 1f;
	
	public void draw(SpriteBatch batch, float deltaTime){
		float screenPosX = posx*32;
		float screenPosY = posy*32;
		switch(lastDir){
			case ZombieLord.DIR_EAST:
				s.setRegion(0, 32*3, 32, 32);
				break;
			case ZombieLord.DIR_NORTH:
				s.setRegion(0, 32*0, 32, 32);
				break;
			case ZombieLord.DIR_WEST:
				s.setRegion(0, 32*1, 32, 32);
				break;
			default:
				s.setRegion(0, 32*2, 32, 32);
				break;
		}
		s.setPosition(screenPosX, screenPosY);
		// 'charged' zombies have a malevolent glow
		s.setColor(1f,1f-this.chargeLevel*0.1f,1f-this.chargeLevel*0.1f,1f);
		
		s.draw(batch);
		if(this.chargeLevel >= 10){
		        // draw nice halo effect, to signify great evil
		        Texture t = new Texture(Gdx.files.internal("data/zd/zhalo.png"));
			Sprite halo1 = new Sprite(t);
			halo1.setColor(1f,1f,1f,0.35f);
			halo1.setPosition(screenPosX, screenPosY);
			halo1.draw(batch);
		        Texture f = new Texture(Gdx.files.internal("data/flash64.png"));
			Sprite halo2 = new Sprite(f);
			halo2.setColor(1f,0.5f,0.5f,0.15f);
			halo2.setPosition(screenPosX-32+16, screenPosY-32+16);
			halo2.draw(batch);
		}

		
	}
	
	public int getMoneyReward(){
		return (int)(Math.random()*2);
	}
	
	public byte nextDir(){
		return ZombieLord.DIR_NORTH;
	}
	
	public float chance(){
		return 0.90f;
	}
	
	public void move(float deltaTime, ZombieDefense zd){
	        
		byte direction = 0;
		
		moveTime += deltaTime;		

		if(moveTime >= tileDelay && Math.random() < attackChance){
		    // try to attack instead of moving
		    boolean hasAttacked = false;
		    ListIterator<Defender> dlit = zd.defenders.listIterator();
		    while(dlit.hasNext()){
			Defender d = dlit.next();
			if(zd.getDistanceTo(this.posx, this.posy, d.getX(), d.getY()) <= 1){
			    d.health -= this.attackStrength;
			    if(d.health <= 0){
				// baleet eet
				dlit.remove();
				// TODO: some sort of effect..
			    }
				
			    hasAttacked = true;
			    // TODO: draw the zbite sprite..
			    
			}
		    }

		    if(hasAttacked){
			// every time the z has attacked, increase attack by 50%
			// yep, scary but true
			// TODO: 'charged' zombies glow..
			if(this.chargeLevel < 10){
			    this.attackStrength *= 1.5;
			    this.chargeLevel ++;
			}
			moveTime = -tileDelay;
			return;
		    }
		}
		

		
		if(moveTime > tileDelay){
			moveTime -= tileDelay;
			
			/*if(lastPathFind < pathFindRate){
				direction = lastDir;
				lastPathFind += deltaTime;
			}
			else {*/
				direction = zd.findPath(this.posx, this.posy, this);
				//lastPathFind = deltaTime;
				if(direction == -1){
					// dont move!
					return;
				}
			//}
			
		}
		else
			return;
		
		this.lastDir = direction;
		
		//System.out.println("atk("+posx+","+posy+") moving: "+direction);
		
		switch(direction){
			case ZombieLord.DIR_EAST:
				posx ++;
				break;
			case ZombieLord.DIR_NORTH:
				posy ++;
				break;
			case ZombieLord.DIR_WEST:
				posx --;
				break;
			default: // DIR_SOUTH
				posy --;
				break;
		}
	}
	
	public Attacker(String appearance, int health, int mana, float movementDelay){
		Texture tex = new Texture(Gdx.files.internal(appearance));
		this.health = health;
		this.mana = mana;
		this.tileDelay = movementDelay;
		s = new Sprite(tex, 32, 32);
		this.healthMax = health;
	}
}
