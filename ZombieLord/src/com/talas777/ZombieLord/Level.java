package com.talas777.ZombieLord;

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
	public static final Vector2[] vectorize(float[] xValues, float[] yValues, float pixels_per_meter){
		Vector2[] vec = new Vector2[xValues.length];
		for(int i = 0; i < xValues.length; i++){
			vec[i] = new Vector2(xValues[i]/pixels_per_meter, yValues[i]/pixels_per_meter);
		}
		return vec;
	}
	
	/**
	 * 
	 * @param posx current x position
	 * @param posy current y position
	 * @return Which level to transfer to (or -1 for no transfer)
	 */
	public abstract int getLevelTransfer(int posx, int posy);

}
