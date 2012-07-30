package com.talas777.ZombieLord;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class Combatant {
	
	private Sprite drawSprite;
	
	/*
	 * X position of sprite
	 */
	//private float posx = 0;
	
	/**
	 * if the combatant is attacking and should therefore be drawn a little towards its target.
	 */
	private boolean movedAhead = false;
	
	public Combatant(String name, int health, int health_max, int mana, int mana_max, int exp, int level){
		this.name = name;
		this.health = health;
		this.health_max = health_max;
		this.mana = mana;
		this.mana_max = mana_max;
		this.exp = exp;
		this.level = level;
		this.combatActions = new LinkedList<CombatAction>();
		this.drawSprite = null;
		
	}
	
	/**
	 * Used when in combat to determine how long the creature has to wait to
	 * make another action
	 */
	public double actionTimer = 0;
	
	/**
	 * name of the creature, MUST be uniqe
	 */
	private final String name;
	
	/**
	 * What state the creature is in
	 * Can be normal, poisoned, slow, haste, stop, stone, paralyzed, fury, weakness or doom
	 */
	private int state;
	
	public static final int STATE_NORMAL = 0;
	public static final int STATE_POISONED = 1;
	public static final int STATE_SLOW = 2;
	public static final int STATE_HASTE = 3;
	public static final int STATE_STOP = 4;
	public static final int STATE_STONE = 5;
	public static final int STATE_PARALYZED = 6;
	public static final int STATE_FURY = 7;
	public static final int STATE_WEAKNESS = 8;
	public static final int STATE_DOOM = 9;
	
	/**
	 * health at the start of battle
	 */
	public int health;
	/**
	 * max health
	 */
	public int health_max;
	
	/**
	 * mana at the start of battle
	 */
	public int mana;
	/**
	 * max mana
	 */
	public int mana_max;
	
	/**
	 * experience given when defeated
	 */
	public int exp;
	
	/**
	 * level of monster, for calculations
	 */
	public int level;
	
	/**
	 * List of attacks this monster has
	 */
	private LinkedList<CombatAction> combatActions;
	
	/**
	 * dont use this one for monsters!
	 * @param action
	 */
	public void addCombatAction(CombatAction action){
		combatActions.add(action);
	}
	
	public String getName(){
		return this.name;
	}
	
	public LinkedList<CombatAction> getCombatActions(){
		return combatActions;
	}
	
	public int getState(){
		return state;
	}
	
	public void setSprite(Sprite s){
		this.drawSprite = s;
		//this.posx = s.getX();
	}
	
	public Sprite getSprite(){
		return this.drawSprite;
	}
	
	public void setState(int newState){
		state = newState;
	}
	
	public abstract int getBaseDelay();
	
	public void setMoveAhead(boolean state){
		if(state == this.movedAhead)
			return; // already moved, do nothing
		else {
			int movePixels = 200;
			if((state && this instanceof Monster) || (!state && this instanceof PartyMember)){ // move left
				// I'm a little ashamed of this strange method
				movePixels *= -1;
			}
			
			//else -> move right
			//this.posx += movePixels;
			this.drawSprite.setX(this.drawSprite.getX() + movePixels);
			this.movedAhead = state;
		}
	}
	
	/*public float getX(){
		return this.posx;
	}*/
}
