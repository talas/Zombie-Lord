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
		return "hometown/hometown-night.png";
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
		return "data/music/Mark_Subbotin_-_Phoenix.ogg";
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
			setup1.exp = 1;
			
			MonsterSetup setup2 = new MonsterSetup(MonsterSetup.FORMATION_SIMPLE);
			setup2.appendMonster(zombie1);
			setup2.appendMonster(zombie2);
			setup2.exp = 5;
			
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

		
		{ // entering myhouse the first time
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(2913,2942,1416,1430, "start", talk, 0);
			d.addTimeChange("talk with gf");
			d.addLevelTransfer(new MyHouse(), 448, 220, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		
		{ // talk when exiting myhouse the first time
			TalkScript talk = new TalkScript();
			
			talk.add("Tolinai", "Lets go to the eastmost house first.");
			talk.add("Leoric", "Watch out for zombies!");
			
			Dialog d = new Dialog(0,3200,0,3200, "east house?", talk, 0);
			d.addTimeChange("east house!");
			
			dialogs.add(d);
		}
		{ // talk when entering east house
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "Ok, are you ready? I can hear zombies inside.");
			talk.add("Leoric", "Lets go!");
			
			Dialog d = new Dialog(3106,3130,1413,1426, "east house!", talk, 0);
			d.addTimeChange("east house-combat");
			d.addLevelTransfer(new EastHouse(), 239, 149, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		{ // talk when entering south-west house
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "It's barricaded..");
			talk.add("Tolinai", "I can hear zombies     inside, I guess we can't help   them.");
			talk.add("Tolinai", "We'll go to the house  on the left next.");
			
			Dialog d = new Dialog(2960,3130,1310,1329, "south east house?", talk, 0);
			d.addTimeChange("south west house?");
			
			dialogs.add(d);
		}
		{ // talk when entering south-west house
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "It's locked..");
			talk.add("Tolinai", "Break it open!");
			

			Dialog d = new Dialog(2790,2808,1125,1145, "south west house?", talk, 0);
			d.addTimeChange("south west house!");
			d.addLevelTransfer(new SouthWestHouse(), 318, 87, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		{ // talk when entering west house
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(2660,2680,1440,1459, "west house?", talk, 0);
			d.addTimeChange("west house!");
			d.addLevelTransfer(new WestHouse(), 205, 126, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		{ // talk when entering mayors house
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(2070,2097,1530,1559, "mayors house?", talk, 0);
			d.addLevelTransfer(new MayorHouse(), 337, 52, ZombieLord.DIR_NORTH);
			
			dialogs.add(d);
		}
		{ // talk when exiting mayors house
			TalkScript talk = new TalkScript();
			
			talk.add("Leoric", "What do we do now?");
			talk.add("Tolinai", "There are still hordes of zombies coming, we have to   flee the town!");
			
			Dialog d = new Dialog(0,3200,0,3200, "leave mayors house", talk, 0);
			d.addTimeChange("leave hometown");
			
			dialogs.add(d);
		}
		{ // exit the town
			TalkScript talk = new TalkScript();
			
			Dialog d = new Dialog(2190,2500,0,17, "leave hometown", talk, 0);
			d.addTimeChange("left hometown");
			d.addLevelTransfer(new SecondTown(), 117,1949, ZombieLord.DIR_SOUTH);
			
			
			dialogs.add(d);
		}
		
		
		return dialogs;
	}

}
