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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.talas777.ZombieLord.ZombieLord;


public class Defender extends Attacker {

	private int range;
	private int damage;
	private float actionDelay;
	private float availableTime;
	public int cost;
	private String appearance;
	private final int speed;
	private final int baseHealth;
	private byte direction;
	private float shootTimeLeft;
	
	private static final Texture arrowTex = new Texture(Gdx.files.internal("data/zd/arrow.png"));
	
	public Defender(String appearance, int health, int cost, int damage, int range, int speed, float actionDelay) {
		super(appearance, health, 0, speed);
		this.appearance = appearance;
		this.range = range;
		this.damage = damage;
		this.actionDelay = actionDelay;
		this.cost = cost;
		this.speed = speed;
		this.baseHealth = health;
		Texture tex = new Texture(Gdx.files.internal(appearance));
		this.s = new Sprite(tex, 0, 32*2, 32, 32);
	}
	
	@Override
	public void draw(SpriteBatch batch, float deltaTime){
		float screenPosX = posx*32;
		float screenPosY = posy*32;
		s.setPosition(screenPosX, screenPosY);
		int size = 32;
		boolean shooting = shootTimeLeft>0;
		switch(this.direction){
			case ZombieLord.DIR_EAST:
				s.setRegion((shooting?size:0), size*3, size, size);
				break;
			case ZombieLord.DIR_NORTH:
				s.setRegion((shooting?size:0), size*0, size, size);
				break;
			case ZombieLord.DIR_SOUTH:
				s.setRegion((shooting?size:0), size*2, size, size);
				break;
			case ZombieLord.DIR_WEST:
				s.setRegion((shooting?size:0), size*1, size, size);
				break;
		}
		shootTimeLeft -= deltaTime;
		s.draw(batch);
	}
	
	public String toString(){
		return "Defender: dmg"+this.damage;
	}
	

	
	public boolean equals(Defender d){
		
		if(d.damage == this.damage){
			if(d.range == this.range){
				if(d.baseHealth == this.baseHealth){
					if(d.actionDelay == this.actionDelay){
						if(d.speed == this.speed){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public Defender clone(){
		Defender clone = new Defender(appearance, baseHealth, cost, damage, range, speed, actionDelay);
		return clone;
	}
	
	public static float getRotationDegrees(int fromx, int fromy, int tox, int toy){
		
		float diffx = fromx - tox;
		float diffy = fromy - toy;
		
		if(diffx == 0)
			diffx = 0.001f;
		
		if(diffy == 0){
			diffy = 0.001f;
			if(diffx > 0)
				return 90;
			else
				return 270;
		}
		
		float angle = (float)(Math.atan(diffy/diffx)*(180/Math.PI));
		
		angle += 90;
		
		if(diffx < 0)
			angle += 180;
		
		System.out.println("dx: "+diffx+", dy: "+diffy+", angle: "+ angle);
		
		return angle;
	}
	
	public void act(LinkedList<Attacker> attackers, float deltaTime){
		availableTime += deltaTime;
		
		boolean noAction = true;
		
		if(availableTime < actionDelay){
			return; // still 'charging' to carry out another action
		}
		
		for(Attacker attacker : attackers){
			if(availableTime < actionDelay)
				return; // no more time left (have to 'charge' again..)
			
			if(isInRange(attacker)){
				// attack
				Sprite arrow = new Sprite(arrowTex, 32, 32);
				int fromx = this.getX()*32;
				int fromy = this.getY()*32;
				int tox = attacker.getX()*32;
				int toy = attacker.getY()*32;
				this.shootTimeLeft = 1f;
				
				float angle = getRotationDegrees(fromx, fromy, tox, toy);
				
				if(angle <= 45 || angle > 315){
					// north
					this.direction = ZombieLord.DIR_NORTH;
				}
				else if(angle > 45 && angle <= 135){
					// east
					this.direction = ZombieLord.DIR_WEST;
				}
				else if(angle > 135 && angle <= 225){
					// south
					this.direction = ZombieLord.DIR_SOUTH;
				}
				else // west
					this.direction = ZombieLord.DIR_EAST;
				
				if(angle == 90)
					arrow.rotate90(true);
				else if(angle == 270)
					arrow.rotate90(false);
				else
					arrow.rotate(angle);
				
				ZombieLord.sendAnimatedMissile(arrow, fromx, tox, fromy, toy, 5.7f);
				
				attacker.health -= damage;
				availableTime -= actionDelay;
				noAction = false;
			}
		}
		if(noAction && availableTime > actionDelay){
			availableTime = actionDelay; // dont charge for more than 1 shot.
		}
			
	}
	
	public boolean isInRange(Attacker other){
		int diffx = Math.abs(this.getX() - other.getX());
		if(diffx > range)
			return false;
		
		int diffy = Math.abs(this.getY() - other.getY());
		if(diffy > range)
			return false;
		
		return true;
	}


}
