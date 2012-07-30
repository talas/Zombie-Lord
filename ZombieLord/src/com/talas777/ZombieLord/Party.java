package com.talas777.ZombieLord;

public class Party {

	public static final int ID_MAIN = 0;
	public static final int ID_GF = 1;
	public static final int ID_SIDE = 2;
	
	public Party(){
		this.activeMembers = new int[]{0};
	}
	
	private PartyMember[] members = new PartyMember[5];
	
	private int[] activeMembers;
	
	public String getMemberName(int id){
		if(members[id] != null)
		{
			return members[id].getName();
		}
		else
			return "Zombie Lord";
	}
	
	public int getMemberHealth(int id){
		return 100;
	}
	
	public boolean hasCombatItem(){
		return false; // TODO: actually, we have no items at all..
	}
	
	public void setMemberHealth(int id, int health){
		//TODO: setMemberHealth
	}
	
	public void addMember(PartyMember member){
		members[member.id] = member;
	}
	
	public PartyMember[] getActiveMembers(){
		PartyMember[] active = new PartyMember[this.activeMembers.length];
		for(int i = 0; i < activeMembers.length; i++){
			active[i] = members[this.activeMembers[i]];
		}
		return active;
	}
	
	public int[] getInactiveMembers(){
		return new int[]{1};
	}
	
	public void setActiveMembers(int first, int second, int third){
		//TODO: setActiveMembers
	}
}
