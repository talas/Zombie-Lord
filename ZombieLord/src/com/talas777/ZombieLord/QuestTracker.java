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

import java.util.HashMap;

public class QuestTracker {

	private HashMap<String,Boolean> questsCompleted;
	
	public QuestTracker(){
		this.questsCompleted = new HashMap<String, Boolean>();
	}
	
	
	public void registerQuest(String uniqueId){
		
		if(this.questsCompleted.get(uniqueId) != null){
			// Already registered
			throw new java.lang.RuntimeException("The quest: '"+uniqueId+"' is already registered!");
		}
		questsCompleted.put(uniqueId, false);
	}
	
	public boolean isCompleted(String uniqueId){
		if(this.questsCompleted.get(uniqueId) == null){
			// Already registered
			throw new java.lang.RuntimeException("The quest: '"+uniqueId+"' was not registered!");
		}
		return questsCompleted.get(uniqueId);
	}
	
	public void setCompleted(String uniqueId){
		if(this.questsCompleted.get(uniqueId) == null){
			// Already registered
			throw new java.lang.RuntimeException("The quest: '"+uniqueId+"' was not registered!");
		}
		questsCompleted.put(uniqueId, true);
	}
	
	
}
