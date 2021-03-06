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

public class TalkScript {

	
	private LinkedList<Utterance> talk;
	
	public TalkScript(){
		this.talk = new LinkedList<Utterance>();
	}
	
	public void add(String speaker, String sentence){
		talk.add(new Utterance(speaker, sentence));
	}
	
	public LinkedList<Utterance> getTalk(){
		return talk;
	}
}
