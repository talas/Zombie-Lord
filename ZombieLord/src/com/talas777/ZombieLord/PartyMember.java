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
import com.badlogic.gdx.graphics.Texture;
import com.talas777.ZombieLord.Items.Weapon;

public class PartyMember extends Combatant{

	public final int id;
	
	public int atk;
	public int def;
	public int matk;
	public int mdef;
	
	private Weapon weapon;
	
	public static long getExperienceForLevel(int level){
		return level*level;
	}
	
	public Texture getFace(){
		
		return new Texture(Gdx.files.internal("data/ui/faces/"+this.getName().toLowerCase()+".png"));
	}
	
	private int[] baseStats;
	
	public PartyMember(int id, String name, int healthMax, int manaMax, int exp,
			int baseSTR, int baseVIT, int baseAGI, int baseINT, int baseWIS,
			int baseSPR, int baseLUCK){
		super(name, 0, 0, 0, 0, exp, 1,
				baseSTR, baseVIT, baseAGI, baseINT, baseWIS, baseSPR,baseLUCK);
		this.id = id;
		this.atk = 1;
		this.def = 1;
		this.matk = 1;
		this.mdef = 1;
		this.baseStats = new int[7+2];
		this.baseStats[0] = baseSTR;
		this.baseStats[1] = baseVIT;
		this.baseStats[2] = baseAGI;
		this.baseStats[3] = baseINT;
		this.baseStats[4] = baseWIS;
		this.baseStats[5] = baseSPR;
		this.baseStats[6] = baseLUCK;
		this.baseStats[7] = healthMax;
		this.baseStats[8] = manaMax;
		this.addExp(exp, false);
		this.health = this.getHealthMax();
		this.mana = this.getManaMax();
	}
	
	public int getExperience(){
		return this.exp;
	}
	
	public int getMATK(){ // TODO: account for temporary attributes
		return (int)((super.getMATK()/100f)*matk)+10;
	}
	
	public int getMDEF(){ // TODO: account for temporary attributes
		return (int)((super.getMDEF()/100f)*mdef)+10;
	}
	
	public int getATK(){ // TODO: account for temporary attributes
		return (int)((super.getATK()/100f)*atk)+10;
	}
	
	public int getDEF(){ // TODO: account for temporary attributes
		return (int)((super.getDEF()/100f)*def)+10;
	}
	
	public static int getLevel(int experience){
		return (int)Math.floor(Math.sqrt(experience))+1;
	}
	
    
        private boolean addExp(int exp, boolean announcement){

	    	if(this.level == ZombieLord.MAX_PLAYER_LEVEL){
			this.exp = 0;
			return false;
		}
		this.exp += exp;
		boolean hasLevel = false;
		while(this.exp > getExperienceForLevel(level+1)){
			// level up!
			
			hasLevel = true;
			gainLevel();
			if(this.level == ZombieLord.MAX_PLAYER_LEVEL){
				this.exp = 0;
				break;
			}
		}
		if(hasLevel && announcement)
			ZombieLord.announce(this.getName()+" has attained level "+level+"!");
		return hasLevel;

       }

	public boolean addExperience(int exp){
		
	    return addExp(exp, true);
	}
	
	public void gainLevel(){
		level++;
		this.exp -= getExperienceForLevel(level);

		int points = 7;
		

		// every level you get $points points to distribute
		// these are distributed according to baseStats
		// read up on roulette wheel selection if you want
		int str = (int)(Math.random()*baseStats[0]);
		int vit = (int)(Math.random()*baseStats[1]);
		int agi = (int)(Math.random()*baseStats[2]);
		int intel = (int)(Math.random()*baseStats[3]);
		int wis = (int)(Math.random()*baseStats[4]);
		int spr = (int)(Math.random()*baseStats[5]);
		int luck = (int)(Math.random()*baseStats[6]);
		
		int total = str+vit+agi+intel+wis+spr+luck;	       
		
		while(points > 0){
		    // draw a stat from the roulette wheel
		    int chosen = (int)(Math.random()*total);
		    if(chosen <= str){
			// strength
			this.addStrength(1);
		    } else if(chosen <= str+vit) {
			// vitality
			this.addVitality(1);
		    } else if(chosen <= str+vit+agi) {
			// agility
			this.addAgility(1);
		    } else if(chosen <= str+vit+agi+intel) {
			// intelligence
			this.addIntelligence(1);
		    } else if(chosen <= str+vit+agi+intel+wis) {
			// wisdom
			this.addWisdom(1);
		    } else if(chosen <= str+vit+agi+intel+wis+spr) {
			// spirit
			this.addSpirit(1);
		    } else {
			// luck
			this.addLuck(1);
		    }
		    points --;
		}
		
	}

        @Override
        public int getHealthMax() {
	    return Math.min(9999,(super.getHealthMax() +this.baseStats[7]+this.getVitality()*5));
	}

        @Override
        public int getManaMax() {
	    return Math.min(999,(super.getManaMax() +this.baseStats[8]+this.getWisdom()*2));
	}
    
	public int getMana(){
		return mana;
	}
	
	public int getHealth(){
		return health;
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getBaseDelay(){
		return (int)(Math.max(50, Math.min(100, 187/level))*0.5f);
	}
	
	public void equipWeapon(Weapon w){
		if(this.weapon != null)
			this.weapon.unEquip(this);
		this.weapon = w;
		this.weapon.equip(this);
	}
}
