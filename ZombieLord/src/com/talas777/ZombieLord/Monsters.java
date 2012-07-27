package com.talas777.ZombieLord;

import java.util.HashMap;
public class Monsters {

	
	HashMap<String,Monster> monsterList;
	
	public Monsters(){
		monsterList = new HashMap<String,Monster>();
	}
	
	public void addMonster(Monster monster){
		monsterList.put(monster.getName(), monster);
	}
	
	public Monster getMonster(String name){
		return monsterList.get(name);
	}
}
