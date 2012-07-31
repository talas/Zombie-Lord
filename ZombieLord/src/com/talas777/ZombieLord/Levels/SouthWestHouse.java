package com.talas777.ZombieLord.Levels;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;
import com.talas777.ZombieLord.Dialog;
import com.talas777.ZombieLord.Level;
import com.talas777.ZombieLord.Monster;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.MonsterSetup;
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class SouthWestHouse extends Level {

	@Override
	public String getBackground() {
		return "hometown/southwesthouse.png";
	}

	@Override
	public Sprite background(Texture t) {
		return new Sprite(t, 0, 0, 640, 512);
	}

	@Override
	public String getForeground() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void applyCollisionBoundaries(World world, float pixels_per_meter) {
		// TODO Auto-generated method stub

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
		// 1 zombie to kill..
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();
		//position: x=283, y=207, time=east house-combat
		//position: x=481, y=300, time=east house-combat
		{
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(0,470,165,300, "south west house!", talk, 0);
			d.addTimeChange("leave sw house");
			
			MonsterSetup setup = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			Monster zombie1 = new Monster("Zombie1","malesoldierzombie.png",5,20,15,3,1.25f);

			zombie1.addCombatAction(ZombieLord.bite);

			setup.appendMonster(zombie1);

			
			d.addFight(setup);
			//d.addLevelTransfer(new HomeTownNight(), 2928, 1388, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		{
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai","Let's go to the house north of this one.");
			
			Dialog d = new Dialog(223,256,103,123, "leave east house", talk, 0);
			d.addTimeChange("south east house?");

			d.addLevelTransfer(new HomeTownNight(), 3120, 1401, ZombieLord.DIR_SOUTH);
			
			dialogs.add(d);
		}
		
		
		
		
		return dialogs;
	}

}
