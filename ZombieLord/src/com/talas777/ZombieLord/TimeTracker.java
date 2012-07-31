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
import java.util.ListIterator;

/**
 * Keeps track of which events have passed in game.
 * Used for the "MAIN" quest. sidequests will need something else..
 * @author talas
 *
 */
public class TimeTracker {

	
	private LinkedList<String> events;
	private String current;
	
	
	/**
	 * Checks if a specific event has already occured.
	 * Note that the current event has "not already occured"
	 * @param event to check for
	 * @return boolean, true if event has occured, false if event is current, next or later
	 */
	public boolean hasOccured(String event){
		if(current.equals(event)){
			return false; // its happening right now, hasnt already happened
		}
		
		ListIterator<String> l = this.events.listIterator();
		
		while(l.hasNext()){
			String s = l.next();
			if(s.equals(this.current)){
				return false;
			}
			if(s.equals(event)){
				return true;
			}
		}
		return false;
	}
	
	public String getTime(){
		return current;
	}
	
	public void setTime(String time){
		this.current = time;
	}
	
	/**
	 * Adds the event to the back of the list
	 * @param event
	 */
	public void addEvent(String event){
		events.add(event);
	}
	
	/**
	 * Adds the event after the specified event in the list
	 * @param previous
	 * @param event
	 */
	public void insertEvent(String previous, String event){
		ListIterator<String> l = this.events.listIterator();
		while(l.hasNext()){
			String s = l.next();
			if(s.equals(this.current)){
				l.add(event);
				return;
			}
		}
	}
	
	public void incrementTime(){
		this.current = getNextEvent();
	}
	
	public TimeTracker(){
		this.current = null;
		this.events = new LinkedList<String>();
	}
	
	public String getPreviousEvent(){
		String prev = null;
		for(String s : this.events){
			if(s.equals(this.current))
				return prev;
			else
				prev = s;
		}
		return prev;
	}
	
	public String getNextEvent(){
		if(current == null)
			return this.events.getFirst();
		boolean found = false;
		for(String s : this.events){
			if(found)
				return s;
			else if(s.equals(this.current))
				found = true;
		}
		return null;
	}
}
