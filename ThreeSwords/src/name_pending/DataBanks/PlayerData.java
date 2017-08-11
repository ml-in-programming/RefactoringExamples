package name_pending.DataBanks;

import name_pending.Game;
import name_pending.Inventory;
import name_pending.Resistance;
import name_pending.Entities.Items.Item;

import java.util.ArrayList;


/**
 * I have split the player and the players data into two parts. This will house everything player related as in stat's and inventory.
 * The other player object will manage collisions etc
 * @author Hawox
 *
 */
public class PlayerData {
	private static String STARTING_NAME = "Player";
	private static int STARTING_HEALTH = 1000;
	private static int STARTING_DEFENCE = 0;
	private static int STARTING_ATTACK = 1;
	private static int STARTING_SPEED = 50;
	private static int STARTING_DEXTERITY = 0;
	private static Resistance[] STARTING_RESISTANCE = null;
	private static int MAX_LEVEL = 30;
	private static int STARTING_MAX_FATIGUE = 1000;
	
	//player specific stats 23514
	private int level = 0;
//	private int maxMana = 1000;
//	private int mana = maxMana;
//	private int maxStamina = 1000;
//	private int stamina = maxStamina;
	private int maxFatigue = STARTING_MAX_FATIGUE;
	private int fatigue = 0;
	private int maxHealth = STARTING_HEALTH;
	private int experianceTillLevel = 5;
	private int experiance = 0;
	private int totalExperiance = experiance;
	
	Game thegame;
	
	//Players inventory
	private Inventory inventory = new Inventory("Players Inventory", 15);
	private Inventory equipment = new Inventory("Players Equipment", 8);
	
	public PlayerData(Game theGame)
	{
		this.thegame = theGame;
		for(int i=0; i<9; i++)
		{
			getItems(this.getEquipment()).add(i, null);
			//this.getEquipment().getItems().add(i,new ItemBow(theGame, "SuperBow", "This is the SuperBow of epicness", "white", 10, 100, 150, null), 50 + (50*i), 50 + (50*i));
		}
	}

	// TODO move to Inventory
    public static ArrayList<Item> getItems(Inventory inventory) {
        return inventory.items;
    }

    public void addExp(int amount)
	{
		this.setTotalExperiance(this.getTotalExperiance() + amount);
		if(this.getLevel() < PlayerData.getMAX_LEVEL())
		{
			this.setExperiance(this.getExperiance() + amount);
			if(this.getExperiance() >= this.getExperianceTillLevel())
				this.levelUp();
		}
	}
	
	public void levelUp()
	{
		
		//reset exp
		this.setExperiance(this.getExperiance() - this.getExperianceTillLevel());
		//incress exp till level
		this.setExperianceTillLevel( (int) ( (experianceTillLevel + ( experianceTillLevel * .1)) + ( (10 + (2*this.getLevel()) ) *this.getLevel()) ) );
		//incress player level
		this.setLevel(this.getLevel()+1);
		
		//Max level stuff
		if(this.getLevel() == PlayerData.getMAX_LEVEL())
		{
			this.setExperianceTillLevel(1);
			this.setExperiance(1);
		}
	}
	
	
	/*
	 * 0 Clockwork weapon
	 * 1 Melee weapon
	 * 2 Ranged Weapon
	 * 3 Head
	 * 4 Chest
	 * 5 Gloves
	 * 6 Boots
	 * 7 Amulet
	 * 8 Ring
	 */
	
	/**
	 * I don't want to have to remember the above stats so I am going to add in different methods to clean up the code
	 *  And make it more readable
	 */
	public Item getClockworkWeapon()
	{
		try{
		return getItems(getEquipment()).get(0);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getMeleeWeapon()
	{
		try{
		return getItems(getEquipment()).get(1);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getRangedWeapon()
	{
		try{
		return getItems(getEquipment()).get(2);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;}
	}
	
	public Item getHead()
	{
		try{
		return getItems(getEquipment()).get(3);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getChest()
	{
		try{
		return getItems(getEquipment()).get(4);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getGloves()
	{
		try{
		return getItems(getEquipment()).get(5);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getBoots()
	{
		try{
		return getItems(getEquipment()).get(6);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getAmulet()
	{
		try{
		return getItems(getEquipment()).get(7);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	public Item getRing()
	{
		try{
		return getItems(getEquipment()).get(8);
		}catch(java.lang.IndexOutOfBoundsException e){ return null;	}
	}
	
	
	
	public boolean setClockworkWeapon(Item item)
	{
		if(getType(item) == "clockworkweapon")
		{
			getItems(this.getEquipment()).set(0, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setMeleeWeapon(Item item)
	{
		if(getType(item) == "meleeweapon")
		{
			getItems(this.getEquipment()).set(1, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setrangedWeapon(Item item)
	{
		if(getType(item) == "rangedweapon")
		{
			getItems(this.getEquipment()).set(2, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setHead(Item item)
	{
		if(getType(item) == "head")
		{
			getItems(this.getEquipment()).set(3, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setChest(Item item)
	{
		if(getType(item) == "chest")
		{
			getItems(this.getEquipment()).set(4, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setGloves(Item item)
	{
		if(getType(item) == "gloves")
		{
			getItems(this.getEquipment()).set(5, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setBoots(Item item)
	{
		if(getType(item) == "boots")
		{
			getItems(this.getEquipment()).set(6, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setAmulet(Item item)
	{
		if(getType(item) == "amulet")
		{
			getItems(this.getEquipment()).set(7, item);
			return true;
		}else
			return false;
	}	
	
	public boolean setRing(Item item)
	{
		if(getType(item) == "ring")
		{
			getItems(this.getEquipment()).set(8, item);
			return true;
		}else
			return false;
	}

	// TODO: move to Item
	public static String getType(Item item) {
		return item.type;
	}

	/**
	 * Getters and setters
	 */

	public static String getSTARTING_NAME() {
		return STARTING_NAME;
	}

	public static void setSTARTING_NAME(String sTARTING_NAME) {
		STARTING_NAME = sTARTING_NAME;
	}

	public static int getSTARTING_HEALTH() {
		return STARTING_HEALTH;
	}

	public static void setSTARTING_HEALTH(int sTARTING_HEALTH) {
		STARTING_HEALTH = sTARTING_HEALTH;
	}

	public static int getSTARTING_DEFENCE() {
		return STARTING_DEFENCE;
	}

	public static void setSTARTING_DEFENCE(int sTARTING_DEFENCE) {
		STARTING_DEFENCE = sTARTING_DEFENCE;
	}

	public static int getSTARTING_ATTACK() {
		return STARTING_ATTACK;
	}

	public static void setSTARTING_ATTACK(int sTARTING_ATTACK) {
		STARTING_ATTACK = sTARTING_ATTACK;
	}

	public static int getSTARTING_SPEED() {
		return STARTING_SPEED;
	}

	public static void setSTARTING_SPEED(int sTARTING_SPEED) {
		STARTING_SPEED = sTARTING_SPEED;
	}

	public static int getSTARTING_DEXTERITY() {
		return STARTING_DEXTERITY;
	}

	public static void setSTARTING_DEXTERITY(int sTARTING_DEXTERITY) {
		STARTING_DEXTERITY = sTARTING_DEXTERITY;
	}

	public static Resistance[] getSTARTING_RESISTANCE() {
		return STARTING_RESISTANCE;
	}

	public static void setSTARTING_RESISTANCE(Resistance[] sTARTING_RESISTANCE) {
		STARTING_RESISTANCE = sTARTING_RESISTANCE;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	/*public int getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(int maxMana) {
		this.maxMana = maxMana;
	}

	public int getMaxStamina() {
		return maxStamina;
	}

	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}*/

	public int getExperianceTillLevel() {
		return experianceTillLevel;
	}

	public void setExperianceTillLevel(int experianceTillLevel) {
		this.experianceTillLevel = experianceTillLevel;
	}

	public int getExperiance() {
		return experiance;
	}

	public void setExperiance(int experiance) {
		this.experiance = experiance;
	}

	public int getTotalExperiance() {
		return totalExperiance;
	}

	public void setTotalExperiance(int totalExperiance) {
		this.totalExperiance = totalExperiance;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Inventory getEquipment() {
		return equipment;
	}

	public void setEquipment(Inventory equipment) {
		this.equipment = equipment;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public static int getMAX_LEVEL() {
		return MAX_LEVEL;
	}

	public static void setMAX_LEVEL(int mAX_LEVEL) {
		MAX_LEVEL = mAX_LEVEL;
	}

	public static int getSTARTING_MAX_FATIGUE() {
		return STARTING_MAX_FATIGUE;
	}

	public static void setSTARTING_MAX_FATIGUE(int sTARTING_MAX_FATIGUE) {
		STARTING_MAX_FATIGUE = sTARTING_MAX_FATIGUE;
	}

	public int getMaxFatigue() {
		return maxFatigue;
	}

	public void setMaxFatigue(int maxFatigue) {
		this.maxFatigue = maxFatigue;
	}

	public int getFatigue() {
		return fatigue;
	}

	public void setFatigue(int fatigue) {
		this.fatigue = fatigue;
	}

	public Game getThegame() {
		return thegame;
	}

	public void setThegame(Game thegame) {
		this.thegame = thegame;
	}


}
