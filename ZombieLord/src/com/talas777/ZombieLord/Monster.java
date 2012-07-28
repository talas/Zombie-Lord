package com.talas777.ZombieLord;
import java.util.LinkedList;


public class Monster extends Combatant{

	
	/**
	 * filename of monsters avatar
	 */
	public String textureName;

	
	/**
	 * strength of monsters physical attack, for calculations
	 */
	public float attackStrength;

	
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
		super(name, health_max, health_max, mana_max, mana_max, exp, level);
		this.textureName = textureName;
		this.combatActionWeights = new LinkedList<Float>();
	}
	/**
	 * Constructor for monsters that doesnt start with full health and/or mana
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
		super(name, health, health_max, mana, mana_max, exp, level);
		this.textureName = textureName;
		this.combatActionWeights = new LinkedList<Float>();
	}
	

	
	/**
	 * 
	 * @param action
	 * @param chance, Note that this is a weight for picking a random action.
	 */
	public void addCombatAction(CombatAction action, float chance){
		super.addCombatAction(action);
		this.combatActionWeights.add(chance);
	}
	
	/**
	 * Adds an action with a default weight of 1
	 * @param action
	 */
	@Override
	public void addCombatAction(CombatAction action){
		super.addCombatAction(action);
		this.combatActionWeights.add(1f);
	}
	
	public CombatAction getMonsterAction(int health, int mana, Party party, Combat combat){
		// Uses all the information available to make a decission for the monster.
		//TODO: use info, write function
		
		return null;
	}
	

}
