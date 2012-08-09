/* Zombie Lord - A story driven roleplaying game
* Copyright (C) 2012  Talas (talas777@gmail.com)
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package com.talas777.ZombieLord;

import java.util.LinkedList;

public class Inventory {
	
	
	private LinkedList<Items> items;
	private int maxSize;
	
	public Inventory(int size){
		this.items = new LinkedList<Items>();
		this.maxSize = size;
	}
	
	/**
	 * Tries to add count of type item to this inventory.
	 * If any amount of the item could be added, then this function returns true.
	 * If nothing could be added the function returns false.
	 * @param type
	 * @param count
	 * @return boolean, true if anything could be added. Otherwise false.
	 */
	public boolean addItem(Item type, byte count){
		if(count < 0){
			System.err.println("[WARN]Tried to add negative count of item "+type.name+"!");
			return false; // couldnt add anything
		}
		Items i = findItems(type);
		if(i != null){
			// we have items of this type, try adding more to the stack
			// NOTE, only 1 stack of each item type is allowed.
			System.out.println("append");
			boolean couldAddSome = i.increase(count);
			if(couldAddSome)
				return true;
			else
				return false; // couldnt add anything
		}
		System.out.println("new");
		
		if(addNewItem(type, count))
			return true;
		return false; // couldnt add anything
	}
	
	public void removeItem(Item type, byte count){
		if(count < 0){
			System.err.println("[WARN]Tried to remove negative count of item "+type.name+"!");
			return; // couldnt remove anything
		}
		Items i = findItems(type);
		if(i != null){
			System.out.println("append");
			boolean couldRemove = i.decrease(count);
			if(!couldRemove)
				System.err.println("[WARN]Tried to remove too many items of type "+type.name+" from the inventory!");
			else {
				if(i.count <= 0){
					// delete it.
					items.remove(i);
				}
			}
		}
		else {
			System.err.println("[WARN]Tried to remove item that doesn't exist in the inventory "+type.name+"!");
		}
	}
	
	public LinkedList<Item> getCombatItems(){
		LinkedList<Item> combatItems = new LinkedList<Item>();
		
		for(Items i : this.items){
			if(i.type.isCombatItem()){
				combatItems.add(i.type);
			}
		}
		
		return combatItems;
	}
	
	public int getItemCount(Item item){
		
		for(Items i : this.items){
			if(i.type == item)
				return i.count;
		}
		
		return 0;
	}
	
	public LinkedList<Item> getItems(){
		LinkedList<Item> items = new LinkedList<Item>();
		
		for(Items i : this.items){
			items.add(i.type);
		}
		
		return items;
	}
	
	private boolean addNewItem(Item type, byte count){
		if(items.size() >= maxSize)
			return false; // full
		else {
			items.add(new Items(type, count));
			
			return true;
		}
	}
	
	private Items findItems(Item type){
		if(type == null)
			System.out.println("Null er tull.");
		for(Items i : items){
			if(i.type.equals(type))
				return i;
		}
		return null;
	}
	
	
	private class Items {
		public final Item type;
		public byte count;
		
		public Items(Item type, byte count){
			this.type = type;
			this.count = count;
		}
		
		public boolean increase(int num){
			if(count >= type.maxStack)
				return false;
			
			count += Math.abs(num);
			if(count > type.maxStack){
				count = type.maxStack;
			}
			return true;
		}
		
		public boolean decrease(int num){
			if(count < num){
				return false;
			}
			else {
				count -= Math.abs(num);
				return true;
			}
		}
	}
}
