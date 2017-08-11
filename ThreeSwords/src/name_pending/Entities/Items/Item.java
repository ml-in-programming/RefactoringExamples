package name_pending.Entities.Items;

import name_pending.Game;
import name_pending.Sprite;
import name_pending.DataBanks.ResourceDataBank;

/** The base class for every item in the game **/
public class Item implements Cloneable{
//	private Game theGame;
	//stats all items should have
	private String name = "Empty Item";
	private String decription = "I am an empty item. Here me ROAWR!";
	private String rarity = "white";
	public String type = "nothing";
	private Sprite sprite = null;
	
	/**
	 * 
	 * @param game
	 * @param name
	 * @param decription
	 * @param rarity
	 */
	public Item(Game game, String name, String decription, String rarity, String type, String spriteName)
	{
		this.sprite = ResourceDataBank.getSprite("ItemError.png");
		if(ResourceDataBank.getSprite(spriteName) != null)
			this.sprite = ResourceDataBank.getSprite(spriteName);
		//this.theGame = game;
		this.name = name;
		this.decription = decription;
		this.rarity = rarity;
		this.type = type;

		//Set the sprite to an error sprite
		//game.getResourceDataBank().getSprite("ItemError.png");
	}

	/**
	 * Override for when they just need a blank item. Usually for fallback
	 */
	public Item(){
		this.sprite = ResourceDataBank.getSprite("ItemError.png");
		this.name = "Null Item";
		this.decription = "Nothing Here.";
		this.rarity = "white";
		this.type = "NoType";
	}

	public Item clone()
	{
		try {
            return (Item) super.clone();
        }catch (CloneNotSupportedException e) {
            System.out.println("Clone failed.");
            return null;
        }
	}
	
	/*public Game getTheGame() {
		return theGame;
	}

	public void setTheGame(Game theGame) {
		this.theGame = theGame;
	}*/

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDecription() {
		return decription;
	}

	public void setDecription(String decription) {
		this.decription = decription;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setType(String type) {
		this.type = type;
	}

//	public String getType() {
//		return type;
//	}
}
