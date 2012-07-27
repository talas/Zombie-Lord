package com.talas777.ZombieLord;

import java.util.LinkedList;

/**
 * 
 * @author talas
 * Note, this class is pixel perrfect. all positions are counted as pixels, not box2d stuffs.
 */
public class MonsterArea {

	public int minx, maxx;
	public int miny, maxy;
	
	private LinkedList<MonsterSetup> monsterSetups;
	private LinkedList<Float> setupWeights;
	
	public float encounterChance;
	
	public MonsterArea(int minx, int maxx, int miny, int maxy, float encounterChance){
		this.minx = minx;
		this.miny = miny;
		this.maxx = maxx;
		this.maxy = maxy;
	}
	
	public void addMonsterSetup(MonsterSetup setup, float setupWeight){
		monsterSetups.add(setup);
		setupWeights.add(setupWeight);
	}
	
	public boolean isInside(int posx, int posy){
		return (posx >= minx && posx <= maxx && posy >= miny && posy <= maxy);
	}
	
	public MonsterSetup getRandomSetup(){
		//TODO: getRandomSetup, make it random (with weights)
		return monsterSetups.getFirst();
	}
}
