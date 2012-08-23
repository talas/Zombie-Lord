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

package com.talas777.ZombieLord.Minigames.TowerDefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Cursor {
	public int x;
	public int y;
	
	private Sprite sprite;
	
	private Sprite selector;
	
	private int selected;
	private final int maxSel = 1;
	
	public void draw(SpriteBatch batch){
		sprite.setX(x*32);
		sprite.setY(y*32);
		sprite.draw(batch);
		
		selector.setX(x*32);
		selector.setY(y*32);
		selector.draw(batch);
	}
	
	public Cursor(){
		Texture tex = new Texture(Gdx.files.internal("data/zd/cursor.png"));
		sprite = new Sprite(tex, 32, 32);
		
		Texture seltex = new Texture(Gdx.files.internal("data/zd/selector.png"));
		selector = new Sprite(seltex, 32, 32);
		selected = 0;
		x = 1;
		y = 8;
	}
	
	public void cycleSelector(){
		selected++;
		
		if(selected > maxSel)
			selected = 0;
		
		selector.setRegion(32*selected,0,32,32);
	}
}
