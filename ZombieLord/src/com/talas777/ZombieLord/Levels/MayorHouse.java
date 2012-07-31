package com.talas777.ZombieLord.Levels;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;
import com.talas777.ZombieLord.Dialog;
import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.TimeTracker;

public class MayorHouse extends Level {

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return "mayorhouse";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 640, 640);
	}

	@Override
	public String getForeground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{ // walls and near walls
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{292,292,192,192,288,288,511,511,544,544,607,607,575,575,607,607,540,540,578,578,560,
							544,544,515,461,461,479,479,464,449,449,376,376},
					new float[]{0  , 30, 30,255,256,541,541,516,516,539,539,512,512,487,487,191,191,231,231,277,351,
							275,235,224,224,182,182, 97, 97, 75, 32, 32,  2});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // table and chairs
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{409,409,418,504,517,542,542,534,519,505,421},
					new float[]{337,413,447,447,412,413,386,349,337,321,321});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // table and chairs
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{234,240,230,221,221,210,192},
					new float[]{ 30, 75, 93,105,125,133,133});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}

	}

	@Override
	public String getMusic() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLevelTransfer(int posx, int posy, TimeTracker timer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LinkedList<MonsterArea> getMonsterAreas(TimeTracker timer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<Dialog> getLevelDialogs() {
		// TODO Auto-generated method stub
		return null;
	}

}
