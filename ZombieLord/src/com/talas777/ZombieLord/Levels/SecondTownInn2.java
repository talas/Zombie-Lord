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
import com.talas777.ZombieLord.Monster;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.MonsterSetup;
import com.talas777.ZombieLord.Monsters;
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
		return "data/music/Nebur_Maik_-_Pretty_Baby__only_bells_.ogg";
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

		
		/*{ // go back downstairs
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(42,57,1880,1890, "start", "THE END", talk, 0);
			d.addLevelTransfer(new SecondTownInn1(), 48,1911, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}*/
		{ // TODO: remove gameOver
			TalkScript talk = new TalkScript();
			talk.add("Talas", "GAME OVER!");
			talk.add("Leoric", "What?");
			talk.add("Tolinai", "No Way! We haven't even started.");
			talk.add("Talas' GF", "zzzzz");
			talk.add("Talas", "Well, it's sad but true. I just couldn't finish anymore. I'm sleepy aswell. Just look at how strange this dialog is getting.");
			talk.add("Talas", "Anyways, theres ALOT of things I couldnt implement. As a thanks for playing, I'll let you fight 2 trolls and a manticore.");
			talk.add("Tolinai", "Eh.. You call that a thanks?");
			talk.add("Talas", "I'll try again then. Thanks for playing, I promise to finish the game soon. Good luck with the monsters!");
			Dialog d = new Dialog(42,57,1880,1890, "start", "entered inn", talk, 0);
			
			Monster troll1 = new Monster(Monsters.Troll,5,100,19,3,1.25f);
			Monster troll2 = new Monster(Monsters.Troll,5,100,19,3,1.25f);
			
			troll1.addCombatAction(ZombieLord.BITE);
			troll1.addCombatAction(ZombieLord.PUNCH);
			troll1.addCombatAction(ZombieLord.TWINFIST);
			troll1.addCombatAction(ZombieLord.REGROWTH);
			troll2.addCombatAction(ZombieLord.BITE);
			troll2.addCombatAction(ZombieLord.PUNCH);
			troll2.addCombatAction(ZombieLord.TWINFIST);
			troll2.addCombatAction(ZombieLord.REGROWTH);
			
			MonsterSetup setup1 = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			setup1.appendMonster(troll1);
			setup1.appendMonster(troll2);
			
			setup1.exp = 1000;

			d.addFight(setup1);
			d.addTimeChange("THE END");
			
			dialogs.add(d);
		}
		{ // TODO: remove gameOver
			TalkScript talk = new TalkScript();

			Dialog d = new Dialog(42,57,1880,1890, "THE END", "THE END", talk, 0);
			
			Monster manticore = new Monster(Monsters.Manticore,500,700,60,9,1.25f);
			
			manticore.addCombatAction(ZombieLord.BITE);
			manticore.addCombatAction(ZombieLord.ROULETTE_STING);
			manticore.addCombatAction(ZombieLord.GRAND_CLAW);
			manticore.addCombatAction(ZombieLord.MAGIC_ARROW);
			
			MonsterSetup setup1 = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			setup1.appendMonster(manticore);
			
			setup1.exp = 10000000;

			d.addFight(setup1);
			d.addTimeChange("zero");
			
			dialogs.add(d);
		}
		return dialogs;
	}

}
