package com.talas777.ZombieLord;

public class PartyMember extends Combatant{

	public final int id;
	
	private long experience = 0;
	private byte level = 1; // 127 = max, can loop! HAHA!! Negativ lvl 4 U:
	
	private int health = 100;
	private int mana = 0;
	
	public static long getExperienceForLevel(int level){
		return level*level;
	}
	
	public PartyMember(int id, String name, int healthMax, int healthNow, int manaMax, int manaNow, int exp){
		super(name, healthNow, healthMax, manaNow, manaMax, exp, getLevel(exp));
		this.id = id;
	}
	
	public static int getLevel(int experience){
		return (int)Math.floor(Math.sqrt(experience))+1;
	}
	
	public boolean addExperience(int exp){
		experience += exp;
		boolean hasLevel = false;
		while(experience > getExperienceForLevel(level+1)){
			// level up!
			level++;
			hasLevel = true;
		}
		return hasLevel;
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
}
