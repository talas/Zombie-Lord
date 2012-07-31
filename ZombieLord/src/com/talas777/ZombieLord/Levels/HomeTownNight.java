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

import com.talas777.ZombieLord.Dialog;
import com.talas777.ZombieLord.Monster;
import com.talas777.ZombieLord.MonsterArea;
import com.talas777.ZombieLord.MonsterSetup;
import com.talas777.ZombieLord.TalkScript;
import com.talas777.ZombieLord.TimeTracker;
import com.talas777.ZombieLord.ZombieLord;

public class HomeTownNight extends HomeTown {

	@Override
	public String getBackground() {
		// TODO Auto-generated method stub
		return "hometown-night.png";
	}

	@Override
	public String getForeground() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getLevelTransfer(int posx, int posy, TimeTracker timer) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getMusic() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public LinkedList<MonsterArea> getMonsterAreas(TimeTracker timer) {
		
		if(timer.getTime().equals("start")){
			// no monsters in the beginning
			return null;
		}
		else {
			// Zombies and stuff
			MonsterArea area = new MonsterArea(0,3200,0,3200,0.003f);
			
			Monster zombie1 = new Monster("Zombie1","malesoldierzombie.png",5,20,15,3,1.25f);
			Monster zombie2 = new Monster("Zombie1","malesoldierzombie.png",5,20,15,3,1.25f);
			
			zombie1.addCombatAction(ZombieLord.bite);
			zombie2.addCombatAction(ZombieLord.bite);
			
			MonsterSetup setup1 = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			setup1.appendMonster(zombie1);
			
			MonsterSetup setup2 = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			setup2.appendMonster(zombie1);
			setup2.appendMonster(zombie2);
			
			area.addMonsterSetup(setup1, 0.7f);
			area.addMonsterSetup(setup2, 0.3f);
			
			LinkedList<MonsterArea> areas = new LinkedList<MonsterArea>();
			areas.add(area);
			return areas;
		}
	}
	
	@Override
	public LinkedList<Dialog> getLevelDialogs() {
		LinkedList<Dialog> dialogs = new LinkedList<Dialog>();

		// TODO: dialogs needed here?
		
		//TODO: find real positions..
		
		{ // entering myhouse the first time
			TalkScript talk = new TalkScript();
			
			//TODO: pos shouldve been outside myhouse
			Dialog d = new Dialog(2922,2930,1416,1430, "start", talk, 0);
			d.addTimeChange("talk with gf");
			d.addLevelTransfer(new MyHouse(), 448, 220, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		
		{ // talk when exiting myhouse the first time
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai", "Lets go to the eastmost house first.");
			talk.add("Leoric", "Watch out for zombies!");
			
			//TODO: pos shouldve been outside myhouse
			Dialog d = new Dialog(0,0,50,50, "east house?", talk, 0);
			d.addTimeChange("east house!");
			
			dialogs.add(d);
		}
		{ // talk when entering east house
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "Ok, are you ready? I can hear zombies inside.");
			talk.add("Leoric", "Lets go!");
			
			//TODO: pos shouldve been outside east house, level is also missing
			Dialog d = new Dialog(0,0,50,50, "east house!", talk, 0);
			d.addTimeChange("east house-combat");
			d.addLevelTransfer(null, 0, 0, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		{ // talk when entering south-west house
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "It's barricaded..");
			talk.add("Tolinai", "I can hear zombies inside, I guess we can't help them.");
			talk.add("Tolinai", "We'll go to the house on the left next.");
			
			//TODO: pos shouldve been outside south east house
			Dialog d = new Dialog(0,0,50,50, "south east house?", talk, 0);
			d.addTimeChange("south west house?");
			
			dialogs.add(d);
		}
		
		
		return dialogs;
	}

}