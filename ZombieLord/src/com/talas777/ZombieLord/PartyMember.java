package com.talas777.ZombieLord;

public class PartyMember {

	public final int id;
	public final String name;
	
	private long experience = 0;
	private byte level = 1; // 127 = max, can loop! HAHA!! Negativ lvl 4 U:
	
	private int health = 100;
	private int mana = 0;
	
	public static long getExperienceForLevel(int level){
		return level*level;
	}
	
	public PartyMember(int id, String name){
		this.id = id;
		this.name = name;
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
}
