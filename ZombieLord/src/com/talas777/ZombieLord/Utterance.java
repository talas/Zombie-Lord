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

public class Utterance {

	public final String speaker;
    private final String[] sentence;
    public final int length;
	
	public Utterance(String speaker, String sentence){
		this.speaker = speaker;
		LinkedList<String> s = new LinkedList<String>();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < sentence.length(); i++) {
		    
		    char c = sentence.charAt(i);
		    if(c == ' '){
		    	s.add(sb.toString());
		    	sb = new StringBuilder();
		    }
		    else {
		    	sb.append(c);
		    }
		}
		if(sb.length() > 0)
			s.add(sb.toString());
		this.length = sentence.length();
		this.sentence = s.toArray(new String[0]);
	}
	
	public String[] getSentence() {
		return sentence;
	}
}
