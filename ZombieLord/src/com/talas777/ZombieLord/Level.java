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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Level {
	
	
	/**
	 * 
	 * @return The background picture of the current level
	 */
	public abstract String getBackground();
	
	public abstract Sprite background(Texture t);
	
	/**
	 * 
	 * @return The foreground picture of the current level
	 */
	public abstract String getForeground();
	
	/**
	 * Adds all the collision boundaries to the given box2d World
	 * @param world
	 * @param pixels_per_meter
	 */
	public abstract void applyCollisionBoundaries(World world, float pixels_per_meter);
	
	public abstract String getMusic();
	
	/**
	 * create a vector array from the given positions
	 * @param xValues x values
	 * @param yValues y values
	 * @return array of vector2 points
	 */
	public static final Vector2[] vectorize(float[] xValues, float[] yValues){
		Vector2[] vec = new Vector2[xValues.length];
		for(int i = 0; i < xValues.length; i++){
			vec[i] = new Vector2(xValues[i]/ZombieLord.PIXELS_PER_METER, yValues[i]/ZombieLord.PIXELS_PER_METER);
		}
		return vec;
	}
	
	/**
	 * TODO: this function is not so smart.. maybe use a dialog instead?
	 * @param posx current x position
	 * @param posy current y position
	 * @return Which level to transfer to (or -1 for no transfer)
	 */
	public abstract int getLevelTransfer(int posx, int posy, TimeTracker timer);
	
	/**
	 * List of which monster areas are active in the given level and time.
	 * Note that this is only updated when the level is loaded/reloaded.
	 * @param timer
	 * @return
	 */
	public abstract LinkedList<MonsterArea> getMonsterAreas(TimeTracker timer);
	
	public String[] getBattleBackgrounds(){
		return new String[]{"battle1.png"};
	}
	
	public abstract LinkedList<Dialog> getLevelDialogs();

}
