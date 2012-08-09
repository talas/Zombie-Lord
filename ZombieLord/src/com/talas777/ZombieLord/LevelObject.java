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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;

/**
 * An object that is placed somewhere on the level.
 * It might be interactible, and it might disappear after some event.
 * @author talas
 *
 */
public abstract class LevelObject {

	private int posx;
	private int posy;
	
	private int sizex;
	private int sizey;
	
	private int nearRadius = 48;
	private int collisionRadius = 3;
	private Sprite sprite;
	
	private boolean collision;
	
	private boolean deleteMe = false;
	
	private Vector2[] getCollisionVectors(){
		// guess we make a square..
		Vector2[] vectors = new Vector2[4];
		float x = (posx-sizex/2) / ZombieLord.PIXELS_PER_METER;
		float y = (posy-sizey/2) / ZombieLord.PIXELS_PER_METER;
		float sx = sizex / ZombieLord.PIXELS_PER_METER;
		float sy = sizey / ZombieLord.PIXELS_PER_METER;
		vectors[0] = new Vector2(x, y);
		vectors[1] = new Vector2(x+sx, y);
		vectors[2] = new Vector2(x+sx, y+sy);
		vectors[3] = new Vector2(x, y+sy);
		
		return vectors;
	}
	
	/**
	 * 
	 * @param posx
	 * @param posy
	 * @param texture Texture for the object
	 * @param sizex int texture size x
	 * @param sizey int texture size y
	 */
	public LevelObject(int posx, int posy, Texture texture, int sizex, int sizey, boolean collision){
		this.posx = posx;
		this.posy = posy;
		this.calcSprite(texture,sizex,sizey);
		this.sizex = sizex;
		this.sizey = sizey;
		this.collision = collision;
	}
	
	/**
	 * Trigger that fires when the player interacts with this object.
	 * If nothing happens, then the object is static.
	 */
	public abstract void interact(Party p, QuestTracker quests);
	
	protected void delete(){
		this.deleteMe = true;
	}
	
	public boolean shouldBeDeleted(){
		return this.deleteMe;
	}
	
	private void calcSprite(Texture texture, int sizex, int sizey){
		sprite = new Sprite(texture, 0,0, sizex, sizey);
		sprite.setX(posx-sizex/2);
		sprite.setY(posy-sizey/2);
	}
	
	public ChainShape getCollisionBoundary(){
		if(! collision)
			return null;
		ChainShape shape = new ChainShape();

		shape.createLoop( this.getCollisionVectors() );
		return shape;
	}
	
	public void draw(SpriteBatch batch){
		sprite.draw(batch);
	}
	
	public boolean isNear(int posx, int posy){
		if(posx < this.posx+this.nearRadius && posx > this.posx-this.nearRadius){
			if(posy < this.posy+this.nearRadius && posy > this.posy-this.nearRadius){
				return true;
			}
		}
		return false;
	}
}
