package com.talas777.ZombieLord;

public class ActionCategory {
	
	
	private final byte id;

	public ActionCategory(byte id){
		this.id = id;
	}
	
	public String getCategoryName(){
		switch(id){
		case 0:
			return "Offensive Magic";
		case 1:
			return "Defensive Magic";
		case 2:
			return "Special";
		case 3:
			return "Summon";
		case 4:
			return "Attack";
		default:
			return "Monster Ability";
		}
		
	}
}
