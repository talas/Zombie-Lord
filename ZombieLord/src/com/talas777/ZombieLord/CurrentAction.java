package com.talas777.ZombieLord;

public class CurrentAction {
	public final CombatAction action;
	public final Combatant primaryTarget;
	public final Combatant secondaryTarget;
	public final Combatant caster;
	
	public CurrentAction(CombatAction action, Combatant caster){
		this.action = action;
		this.caster = caster;
		this.primaryTarget = null;
		this.secondaryTarget = null;
	}
	
	public CurrentAction(CombatAction action, Combatant caster, Combatant target){
		this.action = action;
		this.caster = caster;
		this.primaryTarget = target;
		this.secondaryTarget = null;
	}
}
