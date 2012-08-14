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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicInstance {

	private final Music music;
	private final String id;
	
	public MusicInstance(String id){
		this.music = Gdx.audio.newMusic(Gdx.files.internal(id));
		this.id = id;
	}
	
	public boolean isSame(String id){
		return (this.id.equals(id));
	}
	
	public Music getMusic() {
		return music;
	}
	
	public void play(){
		music.play();
	}
	
	public void stop(){
		music.stop();
	}
	
	public void pause(){
		music.pause();
	}
	
	public void dispose(){
		music.dispose();
	}
	
	public void setLooping(boolean state){
		music.setLooping(state);
	}
	
	public void setVolume(float volume){
		music.setVolume(volume);
	}
}
