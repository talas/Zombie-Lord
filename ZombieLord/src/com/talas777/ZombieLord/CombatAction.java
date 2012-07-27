package com.talas777.ZombieLord;

public class CombatAction {

	/*
	 * Because there will be few actions but many animations, all animations
	 * will be hard-coded somewhere else (?) 
	 */
	
	public static final int TARGET_ENEMY_SINGLE = 0;
	public static final int TARGET_ENEMY_ALL = 1;
	public static final int TARGET_SELF = 2;
	public static final int TARGET_ALLY_SINGLE = 3;
	public static final int TARGET_ALLY_ALL = 4;
	public static final int TARGET_ENEMY_RANDOM = 5;
	public static final int TARGET_LAST_ATTACKER = 6;
	public static final int TARGET_RANDOM = 7;
	public static final int TARGET_ALL = 8;
	public static final int TARGET_ALL_OTHER = 9;
	
	public String name;
	public String description;
	
	public int minLevel;
	public int maxLevel;
	
	public int hpCost;
	public int mpCost;
	
	/**
	 * Can be negative to deal damage, or positive to heal.
	 */
	public float healthChange;
	
	public int targetType;
}
