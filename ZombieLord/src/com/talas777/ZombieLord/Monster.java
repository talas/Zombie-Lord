package com.talas777.ZombieLord;
import java.util.LinkedList;


public class Monster {

	
	/**
	 * filename of monsters avatar
	 */
	public String textureName;
	
	/**
	 * name of the creature, MUST be uniqe
	 */
	private String name;
	
	/**
	 * experience given when defeated
	 */
	public int exp;
	
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
	 * level of monster, for calculations
	 */
	public int level;
	
	/**
	 * strength of monsters physical attack, for calculations
	 */
	public float attackStrength;
	
	/**
	 * List of attacks this monster has
	 */
	private LinkedList<CombatAction> combatActions;
	
	/**
	 * The chance of carrying out each action in combat
	 */
	private LinkedList<Float> combatActionWeights;
	
	/**
	 * Simplified constructor for most monsters
	 * @param name
	 * @param textureName
	 * @param exp
	 * @param health_max
	 * @param mana_max
	 * @param level
	 * @param attack
	 */
	public Monster(String name, String textureName, int exp, int health_max, int mana_max, int level, float attack){
		this.name = name;
		this.textureName = textureName;
		this.exp = exp;
		this.health = health_max;
		this.mana = mana_max;
		this.mana_max = mana_max;
		this.health_max = health_max;
	}
	/**
	 * Constructor for monsters that doesnt start with full health
	 * @param name
	 * @param textureName
	 * @param exp
	 * @param health
	 * @param mana
	 * @param health_max
	 * @param mana_max
	 * @param level
	 * @param attack
	 */
	public Monster(String name, String textureName, int exp, int health, int mana, int health_max, int mana_max, int level, float attack){
		this.name = name;
		this.textureName = textureName;
		this.exp = exp;
		this.health = health;
		this.mana = mana;
		this.mana_max = mana_max;
		this.health_max = health_max;
	}
	
	public LinkedList<CombatAction> getCombatActions(){
		return combatActions;
	}
	
	/**
	 * 
	 * @param action
	 * @param chance, Note that this is a weight for picking a random action.
	 */
	public void addCombatAction(CombatAction action, float chance){
		combatActions.add(action);
		this.combatActionWeights.add(chance);
	}
	
	public CombatAction getMonsterAction(int health, int mana, Party party, Combat combat){
		// Uses all the information available to make a decission for the monster.
		//TODO: use info, write function
		
		return null;
	}
	
	public String getName(){
		return this.name;
	}
}
