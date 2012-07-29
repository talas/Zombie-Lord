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
	//public static final int TARGET_LAST_ATTACKER = 6;
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
	
	/**
	 * Full constructor
	 * @param name Name of the action
	 * @param description Friendly description
	 * @param minLevel minimum xp level to use the action
	 * @param maxLevel maximum xp level to use the action
	 * @param hpCost health consumed by using the action
	 * @param mpCost mana consumed by using the action
	 * @param healthChange how this action affects the health of the target
	 * @param targetType what this action targets
	 */
	public CombatAction(String name, String description, int minLevel, int maxLevel, int hpCost, int mpCost, float healthChange, int targetType){
		this.name = name;
		this.description = description;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.hpCost = hpCost;
		this.mpCost = mpCost;
		this.healthChange = healthChange;
		this.targetType = targetType;
	}
	
	/**
	 * Constructor for most monster actions
	 * @param name
	 * @param mpCost
	 * @param healthChange
	 * @param targetType
	 */
	public CombatAction(String name, int mpCost, float healthChange, int targetType){
		this(name, "",0,0,0,mpCost,healthChange,targetType);
	}
	
	public CombatAction(String name, String description, int mpCost, float healthChange, int targetType){
		this(name, description,0,0,0,mpCost,healthChange,targetType);
	}
}
