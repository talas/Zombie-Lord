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
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class SecondTownInn2 extends Level {

	@Override
	public String getBackground() {
		return "secondtown/st-inn-2.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 2048, 2048);
	}
	
	@Override
	public int getCamMaxY(){
		return 2046;
	}
	
	@Override
	public int getCamMinY(){
		return 1408;
	}
	
	@Override
	public int getCamMaxX(){
		return 642;
	}

	@Override
	public String getForeground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Sprite foreground(Texture t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.type = BodyDef.BodyType.StaticBody;
		Body groundBody = world.createBody(groundBodyDef);
		{ // borders
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{8,8,386,386,413,413,439,439,631,631,359,359},
					new float[]{1792,1952,1952,1936,1936,1953,1953,1737,1737,1696,1696,1791});

			environmentShape.createLoop(vertices);
			groundBody.createFixture(environmentShape, 0);
			environmentShape.dispose();
		}
		{ // stairs
			ChainShape environmentShape = new ChainShape();
			
			Vector2[] vertices = vectorize(
					new float[]{63,63,33,33},
					new float[]{1919,1857,1857,1919});

			environmentShape.createChain(vertices);
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
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();

		
		{ // go back downstairs
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(42,57,1880,1890, "start", "THE END", talk, 0);
			d.addLevelTransfer(new SecondTownInn1(), 48,1911, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		return dialogs;
	}

}
