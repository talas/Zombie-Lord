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
* along with this program.  If not, see <http://www.gnu.org/licenses/>.*/

package com.talas777.ZombieLord;

import java.util.LinkedList;
import java.util.ListIterator;

import com.talas777.ZombieLord.Minigames.ZombieDefense;

/**
 * A very powerful dialog handler..
 * 
 * Could also be named an action script.
 * @author talas
 *
 */
public class Dialog {
	
	private final int minx;
	private final int maxx;
	private final int miny;
	private final int maxy;
	
	private final String maxTime;
	private final String minTime;
	
	public final TalkScript talkScript;
	
	public final int button;
	
	private final ListIterator<Utterance> iter;
	
	private final LinkedList<PartyMember> joins;
	private final LinkedList<PartyMember> quits;
	
	public LinkedList<PartyMember> getMemberJoins(){
		return joins;
	}
	
	public LinkedList<PartyMember> getMemberQuits(){
		return quits;
	}
	
	private String timeChange;
	
	public String getTimeChange(){
		return timeChange;
	}
	
	private MonsterSetup setup;
	
	public MonsterSetup getFight(){
		return setup;
	}
	
	private ZombieDefense zombieDefense;
	
	public ZombieDefense getZombieDefense(){
		return zombieDefense;
	}
	
	
	/**
	 * For level transfer
	 */
	public Level nextLevel;
	public int posx;
	public int posy;
	public int direction;
	
	public Utterance getNextUtterance(){
		return iter.next();
	}
	
	public boolean hasNextUtterance(){
		return iter.hasNext();
	}
	
	public boolean isInside(int posx, int posy, TimeTracker time){
		if (posx >= minx && posx <= maxx && posy >= miny && posy <= maxy){

			if(time.hasOccured(minTime) || time.getTime().equals(minTime)){
				if(time.hasOccured(maxTime))
				{
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param minx
	 * @param maxx
	 * @param miny
	 * @param maxy
	 * @param minTime
	 * @param maxTime
	 * @param talk
	 * @param button
	 */
	public Dialog(int minx, int maxx, int miny, int maxy, String minTime, String maxTime, TalkScript talk, int button){
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;

		
		this.minTime = minTime;
		this.maxTime = maxTime;
		
		this.talkScript = talk;
		
		this.button = button;
		this.iter = talk.getTalk().listIterator();
		this.joins = new LinkedList<PartyMember>();
		this.quits = new LinkedList<PartyMember>();
	}
	
	/**
	 * 
	 * @param minx
	 * @param maxx
	 * @param miny
	 * @param maxy
	 * @param time
	 * @param talk
	 * @param button
	 */
	public Dialog(int minx, int maxx, int miny, int maxy, String time, TalkScript talk, int button){
		this(minx, maxx, miny, maxy, time, time, talk, button);
	}
	
	public void addFight(MonsterSetup setup){
		this.setup = setup;
	}
	
	public void addZombieDefense(ZombieDefense zd){
		this.zombieDefense = zd;
	}
	
	public void addTimeChange(String time){
		this.timeChange = time;
	}
	
	public void addLevelTransfer(Level level, int posx, int posy, int direction){
		this.nextLevel = level;
		this.posx = posx;
		this.posy = posy;
		this.direction = direction;
	}
	
	public void addItemReward(Item item, int count){
		
	}
	
	public void addItemLoss(Item item, int count){
		
	}
	
	public void addMemberGain(PartyMember member){
		this.joins.add(member);
	}
	
	public void addMemberLoss(PartyMember member){
		this.quits.add(member);
	}
}
