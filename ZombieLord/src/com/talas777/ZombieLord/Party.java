package com.talas777.ZombieLord;

public class Party {

	public static final int ID_MAIN = 0;
	public static final int ID_GF = 1;
	public static final int ID_SIDE = 2;
	
	public Party(){

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
	
	public void setMemberHealth(int id, int health){
		//TODO: setMemberHealth
	}
	
	public void addMember(PartyMember member){
		members[member.id] = member;
	}
	
	
	public int getMemberMana(int id){
		return 0;
	}
	
	public void setMemberMana(int id, int mana){
		//TODO: setMemberMana
	}
	
	public int[] getMemberIds(){
		return new int[]{0};
	}
	
	public int[] getActiveMembers(){
		return new int[]{0};
	}
	
	public int[] getInactiveMembers(){
		return new int[]{1};
	}
	
	public void setActiveMembers(int first, int second, int third){
		//TODO: setActiveMembers
	}
}
