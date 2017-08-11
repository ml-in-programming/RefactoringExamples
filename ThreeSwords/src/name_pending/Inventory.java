package name_pending;

import java.util.ArrayList;

import name_pending.DataBanks.PlayerData;
import name_pending.Entities.Items.Item;

/**
 * Inventory will not be a single object to handle the players specific inventory
 * 
 * This calls will basicly be an item array
 * 
 * It can be used for the contents of chests, banks, players inventory, etc
 * @author Hawox
 *
 */
public class Inventory {
	//We'll store these into arrays because order will matter
	//EDIT 1: Lets try a list!
	public ArrayList<Item> items = new ArrayList<Item>();
	private int maxSize = 1;
	private String name;
	/**
	 * 
	 * @param name
	 * @param maxSize
	 */
	public Inventory(String name, int maxSize)
	{
		this.name = name;
		this.maxSize = maxSize;
	}
	
	
	/**
	 * We're using this instead of the built in list because we will need to check if the inventory if full before adding in more items
	 * @param item
	 * @return True if item was added
	 */
	public boolean addItem(Item item)
	{
		if(this.items.size() < maxSize)
		{
			this.items.add(item);
			return true;
		}else
			return false;
	}
	
	
	public boolean isFull()
	{
		if(PlayerData.getItems(this).size() > (this.maxSize - 1))
			return true;
		return false;
	}
	
	
	
	/**
	 * Getters and setters
	 */
	
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	
}
