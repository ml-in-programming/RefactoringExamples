package name_pending.Entities.Items;

import name_pending.Game;
import name_pending.Entities.Entity;

/**
 * Other items are not entities.
 * 
 * This exists so an item can be an entity when it is on the ground
 * @author Hawox
 *
 */
public class ItemDrop extends Entity{
	
	Item item;
	
	public ItemDrop(Game theGame, Item item, int x, int y, int speed)
	{
		super(theGame, x, y, speed, "ItemDrop");
		this.item = item;
		//Set the entity sprite to the item sprite
		setSprite(item.getSprite().clone()); // I don't call this in the onCreate because item will still be null
		//setSprite(ResourceDataBank.getSprite("Player.png"));
		//getSprite().setAnimation(1); //get it's animation setup since it was not called in onCreate
	}
	
	public void onCreate()
	{
		super.onCreate();
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	
}
