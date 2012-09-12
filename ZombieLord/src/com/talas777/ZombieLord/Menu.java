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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.talas777.ZombieLord.Items.ConsumeableItem;

/**
 * Class to take care of everything related to using the ingmae 'main menu'.
 * 
 * Main menu options: Item, Character, Magic, Quest, Save, Option, Quit
 * @author talas
 */
public class Menu {

	private final NinePatch ninePatch;
	private final BitmapFont font;
	private final int lineSpace;
	
	private int currentWindow;
	
	/** The main menu, lets you see some basic info about the active characters and is a hub to the other menus. */
	private final int WINDOW_MAIN = 0;
	/** Sort and/or use items (mix items? shake items? stir items?) */
	private final int WINDOW_ITEM = 1;
	/** Look at detailed character info, change equipment */
	private final int WINDOW_CHARACTER = 2;
	/** Choose who should carry which soul stones */
	private final int WINDOW_MAGIC = 3;
	/** Tells you what to do */
	private final int WINDOW_QUEST = 4;
	/** Save the game to some easily corruptable file */
	private final int WINDOW_SAVE = 5;
	/** Turn off sounds, music and maybe something else annoying */
	private final int WINDOW_OPTION = 6;
	/** Are you sure you want to quit? Are you really sure? Are you sure that you are really sure? */
	private final int WINDOW_QUIT = 7;
	

	/** when you have selected an item, it will ask you who to use it on */
	private final int OPTION_USE_ITEM = 8;
	/** when you have selected a players 'change weapon' option, it wants to know which weapon to change to */
	private final int OPTION_SELECT_WEAPON = 9;
	
	private int numOptions;
	private int selectedOption = 0;
	private int selectedItemNumber = -99;
	private Item selectedItem = null;
	
	private final Sprite cursor;
	
	private final Party party;
	
	public Menu(NinePatch ninePatch, BitmapFont font, int lineSpace, String cursorImage, Party p) {
		this.ninePatch = ninePatch;
		this.font = font;
		this.lineSpace = lineSpace;
		Texture t = new Texture(cursorImage);
		cursor = new Sprite(t);
		party = p;
		this.gotoMainMenu(0);
	}
	
	private void gotoMainMenu(int prevMenu){
		this.selectedOption = prevMenu;
		this.numOptions = 7;
		this.currentWindow = WINDOW_MAIN;
	}
	
	private void gotoItemMenu(Inventory i){
		this.selectedOption = 0;
		this.numOptions = i.getNumItems();
		this.currentWindow = WINDOW_ITEM;
	}
	
	private void gotoCharacterMenu(){
		// view characters
		// TODO: set equipment
		this.selectedOption = 0;
		this.numOptions = 1;
		this.currentWindow = WINDOW_CHARACTER;
	}
	
	private String questHint;
	
	private void gotoQuestMenu(ZombieLord zl){
		// view quests
		// TODO: optional quests
		this.selectedOption = 0;
		this.numOptions = 1;
		this.currentWindow = WINDOW_QUEST;
		this.questHint = zl.questHint;
	}
	
	public void draw(SpriteBatch batch){
		int posx = 355;
		int posy = 313;
		switch(currentWindow){
			
			case WINDOW_MAIN:
				drawParty(batch, 1, false);
				ninePatch.draw(batch, posx-55, posy-7*lineSpace, 180, 7*lineSpace+5);
				font.setColor(Color.WHITE);
				font.draw(batch, "Item", posx, posy-0*lineSpace);
				font.draw(batch, "Character", posx, posy-1*lineSpace);
				font.setColor(Color.GRAY);
				font.draw(batch, "Magic", posx, posy-2*lineSpace);
				font.setColor(Color.WHITE);
				font.draw(batch, "Quest", posx, posy-3*lineSpace);
				font.setColor(Color.GRAY);
				font.draw(batch, "Save", posx, posy-4*lineSpace);
				font.draw(batch, "Option", posx, posy-5*lineSpace);
				font.setColor(Color.WHITE);
				font.draw(batch, "Quit", posx, posy-6*lineSpace);
				cursor.setX(posx-40);
				cursor.setY(posy-2-lineSpace-selectedOption*lineSpace);
				cursor.draw(batch);
				
				break;
			case OPTION_USE_ITEM:
			case WINDOW_ITEM:
				drawParty(batch, 0, true);
				int numItems = Math.min(party.getInventory().getNumItems(), 16);
				posx = 265;
				ninePatch.draw(batch, posx-85, posy-numItems*lineSpace, 280, numItems*lineSpace+5);
				int j = 0;
				for(Item i : party.getInventory().getItems()){
					font.setColor(Color.GRAY);
					if(i instanceof ConsumeableItem){
						ConsumeableItem it = (ConsumeableItem)i;
						if(it.nonCombatAllowed())
							font.setColor(Color.WHITE);
					}
					font.draw(batch, party.getInventory().getItemCount(i)+"x "+i.name, posx-50, posy-lineSpace*j);
					j++;
				}
				if(this.selectedItem == null){
					cursor.setX(posx-40-45);
					cursor.setY(posy-2-lineSpace-selectedOption*lineSpace);
					cursor.draw(batch);
				}
				else {
					cursor.setX(posx-40-45);
					cursor.setY(posy-2-lineSpace-this.selectedItemNumber*lineSpace);
					cursor.draw(batch);
					
					cursor.setX(40);
					cursor.setY(posy-20-lineSpace-this.selectedOption*100);
					cursor.draw(batch);
				}
				break;
			case WINDOW_CHARACTER:
				drawParty(batch, 2, true);
				break;
			case WINDOW_QUEST:
				posx = 135;
				posy = 200;
				font.draw(batch, "Current Objective: ", posx, posy+lineSpace);
				font.draw(batch, this.questHint, posx, posy);
		}
		
	}
	
	private void drawParty(SpriteBatch batch, int full, boolean pushBack){
		int ms = 106;
		int m = 213;
		int p = 50;

		if(full == 0)
			p -= 20;
		
		if(pushBack)
			p -= 25;
		
		PartyMember[] members = party.getActiveMembers();
		for(int i = 0; i < members.length && i < 3; i++){
			// if for some reason we have 4 members, we still only care about 3..
			
			if(full==1){
				ninePatch.draw(batch, p-20, m-ms*i, 260, 105);
			}
			else if (full == 2){
				ninePatch.draw(batch, p-20, m-ms*i, 475, 105);
			}
			else {
				ninePatch.draw(batch, p-5, m-ms*i, 170, 105);
			}
			// draw the members ugly mug shot
			if(full != 0) {
				Texture t = members[i].getFace();
				Sprite s = new Sprite(t);
				s.scale(-0.29f);
				s.setPosition(p-30, m-ms*i-11);
				s.draw(batch);
			}
			font.setColor(Color.WHITE);
			int cur = members[i].getHealth();
			int max = members[i].getHealthMax();
			if(cur < max/2f){
				font.setColor(Color.YELLOW);
				if(members[i].getHealth() == 0)
					font.setColor(Color.GRAY);
			}
			font.draw(batch, members[i].getName(), p, m-ms*i+20);
			
			
			int base = 80;
			
			if(full == 0)
				base = 5;
			
			font.draw(batch, "LVL: "+members[i].getLevel(), base+p, m-ms*i+100);
			
			String xpString = "error";
			Color old = font.getColor();
			if(members[i].getLevel() == ZombieLord.MAX_PLAYER_LEVEL){
				xpString = " --- ";
				font.setColor(Color.BLACK);
			}
			else
				xpString = members[i].getExperience()+"/"+PartyMember.getExperienceForLevel(members[i].getLevel()+1);
			font.draw(batch, "XP: "+xpString, base+p, m-ms*i+85);
			font.setColor(old);
			
			font.draw(batch, "HP: "+cur+"/"+max, base+p, m-ms*i+70);
			font.draw(batch, "MP: "+members[i].getMana()+"/"+members[i].getManaMax(), base+p, m-ms*i+55);
			if(full == 2){
				// draw all data
				font.draw(batch, "STR: "+members[i].getStrength(), 240+p, m-ms*i+100);
				font.draw(batch, "VIT: "+members[i].getVitality(), 350+p, m-ms*i+100);
				//font.draw(batch, "VIT: "+members[i].getVitality(), 240+p, m-ms*i+85);
				
				//font.draw(batch, "AGI: "+members[i].getAgility(), 240+p, m-ms*i+70);
				font.draw(batch, "INT: "+members[i].getIntelligence(), 240+p, m-ms*i+85);
				//font.draw(batch, "INT: "+members[i].getIntelligence(), 240+p, m-ms*i+55);
				font.draw(batch, "WIS: "+members[i].getWisdom(), 350+p, m-ms*i+85);
				font.draw(batch, "SPR: "+members[i].getSpirit(), 240+p, m-ms*i+70);
				font.draw(batch, "AGI: "+members[i].getAgility(), 350+p, m-ms*i+70);
				font.draw(batch, "LUCK: "+members[i].getLuck(), 240+p, m-ms*i+55);
			}
		}
	}
	
	public void selectNext(){
		if(this.selectedOption < this.numOptions-1)
			this.selectedOption++;
		else
			this.selectedOption = 0;
	}
	
	public void selectPrevious() {
		if(this.selectedOption > 0)
			this.selectedOption--;
		else
			this.selectedOption = this.numOptions-1;
	}
	
	public void confirmChoice(ZombieLord zl){
		switch(currentWindow){
			case WINDOW_MAIN:
				confirmMainWindow(zl);
				break;
			case WINDOW_ITEM:
				confirmItemWindow(zl);
				break;
			case OPTION_USE_ITEM:
				useItem(zl);
				break;
		}
		
	}
	
	private void useItem(ZombieLord zl){
		// use the item if possible, then go back if no more of the type, otherwise stay here
		if(this.selectedItem instanceof ConsumeableItem){
			
			// TODO: allowed to use it on this player?
			
			
			
			Combatant target = party.getActiveMembers()[this.selectedOption];
			
			ConsumeableItem it = (ConsumeableItem) this.selectedItem;
			
			it.getEffect().combatEffect.applyEffect(null, target);
			party.getInventory().removeItem(this.selectedItem, (byte)1);
			
			// TODO: play a sound?
			
			if(party.getInventory().getItemCount(this.selectedItem) == 0){
				// back to item selection
				this.gotoItemMenu(party.getInventory());
				this.selectedItemNumber = -99;
				this.selectedItem = null;
			}
			else {
				// TODO: nothing special?
			}
		}
		else {
			// error, non-consumeable item can be used???
			System.err.println("[ERR] Non-consumeable item cannot be used(Do you believe otherwise?).");
		}
	}
	
	private void confirmItemWindow(ZombieLord zl){
		// find the selected item..
		
		Item i = zl.party.getInventory().getItems().get(this.selectedOption);
		
		// check if its valid..
		if(i instanceof ConsumeableItem){
			ConsumeableItem it = (ConsumeableItem) i;
			if(it.nonCombatAllowed()){
				// valid
				this.selectedItem = i;
				this.currentWindow = OPTION_USE_ITEM;
				this.selectedItemNumber = this.selectedOption;
				this.selectedOption = 0;
				this.numOptions = party.getActiveMembers().length;
				return;
			}
		}
		// not valid
		zl.playSound("data/sound/bounce.wav", 0.5f);
	}
	
	private void confirmMainWindow(ZombieLord zl){
		switch(this.selectedOption){
		case 0:
			// item
			this.gotoItemMenu(zl.party.getInventory());
			break;
		case 1:
			// character
			this.gotoCharacterMenu();
			break;
		case 2:
			// magic
			ZombieLord.announce("Sorry! Nothing here yet.");
			break;
		case 3:
			// quest
			this.gotoQuestMenu(zl);
			break;
		case 4:
			// save
			ZombieLord.announce("Sorry! Saving is not implemented yet.");
			break;
		case 5:
			// option
			ZombieLord.announce("Sorry! There are no configurable options yet.");
			break;
		case 6:
			// quit
			// TODO: are you sure that you are really sure that you know you want to quit now?
			System.exit(0);
			break;
		default:
			// error
			System.err.println("Invalid option '"+selectedOption+"' for mainmenu, go tell talas!");
			break;
		}
	}
	
	public boolean isDead(){
		return this.dead;
	}
	
	public void dispose(){
		// kill any sprite or texture remaining so this menu can be destroyed safely
		this.cursor.getTexture().dispose();
	}
	
	private boolean dead = false;
	
	public void exit(ZombieLord zl){
		// back out of the current menu
		switch(currentWindow){
		case WINDOW_MAIN:
			// kill the whole menu
			this.dead = true;
			break;
		case WINDOW_ITEM:
			// back to main window
			this.gotoMainMenu(0);
			break;
		case OPTION_USE_ITEM:
			// select another item
			this.gotoItemMenu(party.getInventory());
			this.selectedOption = this.selectedItemNumber;
			this.selectedItemNumber = -99;
			this.selectedItem = null;
			break;
		case WINDOW_CHARACTER:
			// back to main menu
			this.gotoMainMenu(1);
			break;
		case WINDOW_QUEST:
			// back to main menu
			this.gotoMainMenu(3);
			break;
		}
	}
	
	
}
