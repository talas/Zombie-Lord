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

import com.badlogic.gdx.graphics.g2d.Sprite;

public class AnimatedMissile {
	public Sprite sprite;
	public float speed;
	public int fromX;
	public int fromY;
	
	public int toX;
	public int toY;
	
	public AnimatedMissile(Sprite s, float speed, int fromX, int fromY, int toX, int toY){
		this.sprite = s;
		this.speed = speed;
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
		s.setX(fromX);
		s.setY(fromY);
	}
	
	public boolean hasArrived(){
		float diffx = fromX - toX;
		float diffy = fromY - toY;
		
		
		boolean xReached = false;
		boolean yReached = false;
		
		if(diffx > 0){ // going to the left
			if(this.sprite.getX() <= toX)
				xReached = true;
		}
		else if(diffx < 0){
			if(this.sprite.getX() >= toX)
				xReached = true;
		}
		else
			xReached = true;
		
		if(diffy > 0){
			if(this.sprite.getY() <= toY)
				yReached = true;
		}
		else if(diffy < 0){
			if(this.sprite.getY() >= toY)
				yReached = true;
		}
		else
			yReached = true;
		
		return (xReached && yReached);
	}
}
