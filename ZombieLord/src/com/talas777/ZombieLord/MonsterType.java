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

/**
 * 
 * @author talas
 *
 */
public class MonsterType
{
	private int strength; // strength increase atk
	private int vitality; // vitality increase health and defense
	private int agility; // agility increase chance to dodge (evasion)
	private int intelligence; // intelligence gives magic atk power
	private int wisdom; // wisdom gives mana
	private int spirit; // spirit increase magic defense
	private int luck; // luck increase combat speed
	
	private final String name;
	
	
	public String getName(){
		return name;
	}
	/**
	 * Creates a monster with these base attributes.
	 * Note that for each level the monster will increase its attributes depending on that attributes growth chance.
	 * @param strength
	 * @param vitality
	 * @param agility
	 * @param intelligence
	 * @param wisdom
	 * @param spirit
	 * @param luck
	 */
	public MonsterType(String name, int strength, int vitality, int agility, int intelligence, int wisdom, int spirit, int luck){
		this.name = name;
		this.strength = strength;
		this.vitality = vitality;
		this.agility = agility;
		this.intelligence = intelligence;
		this.wisdom = wisdom;
		this.spirit = spirit;
		this.luck = luck;
		this.setAttributeGrowth(0.5f, 0.5f, 0.1f, 0.2f, 0.2f, 0.3f, 0.05f);
		this.setImage("data/monsters/missing.png",128,8);
	}
	
	private String imageFileName;
	private int imageSizeX;
	private int imageSizeY;
	
	public void setImage(String image, int sizeX, int sizeY){
		this.imageFileName =image;
		this.imageSizeX = sizeX;
		this.imageSizeY = sizeY;
	}
	
	// Attribute growth for this monster
	private float strGrowth;
	private float vitGrowth;
	private float agiGrowth;
	private float intGrowth;
	private float wisGrowth;
	private float sprGrowth;
	private float luckGrowth;
	
	
	
	/**
	 * Each level, the monsters attributes will increase by a given chance (1.0 = 100% chance, 0 = 0%).
	 * Use this method to set a nice attribute growth for your monster.
	 * @param strength, default 0.5
	 * @param vitality, default 0.5
	 * @param agility, default 0.1
	 * @param intelligence, default 0.2
	 * @param wisdom, default 0.2
	 * @param spirit, default 0.3
	 * @param luck, default 0.05
	 */
	public void setAttributeGrowth(float strength, float vitality, float agility, float intelligence, float wisdom, float spirit, float luck){
		this.strGrowth = Math.max(0, strength);
		this.vitGrowth = Math.max(0, vitality);
		this.agiGrowth = Math.max(0, agility);
		this.intGrowth = Math.max(0, intelligence);
		this.wisGrowth = Math.max(0, wisdom);
		this.sprGrowth = Math.max(0, spirit);
		this.luckGrowth = Math.max(0, luck);
	}
	
	public int getStrength(int level){
		return (int)(this.strength + (strGrowth*level));
	}
	
	public int getVitality(int level){
		return (int)(this.vitality + (vitGrowth*level));
	}
	
	public int getAgility(int level){
		return (int)(this.agility + (agiGrowth*level));
	}
	
	public int getIntelligence(int level){
		return (int)(this.intelligence + (intGrowth*level));
	}
	
	public int getWisdom(int level){
		return (int)(this.wisdom + (wisGrowth*level));
	}
	
	public int getSpirit(int level){
		return (int)(this.spirit + (sprGrowth*level));
	}
	
	public int getLuck(int level){
		return (int)(this.luck + (luckGrowth*level));
	}



	public String getImageFileName() {
		return imageFileName;
	}

	public int getImageSizeX() {
		return imageSizeX;
	}

	public int getImageSizeY() {
		return imageSizeY;
	}

}
