package name_pending.Entities.Items;

import name_pending.Entities.Being;
import name_pending.Entities.Projectile;
import name_pending.Game;

import java.awt.*;

public class ItemWeapon extends Item{
	//Variables weapons should have
	private int minDamage = 1;
	private int maxDamage = 2;
	private int distance = 1; //how many pixals a ranged weapon will go or how many pixals a melee weapon can reach
	
	//Should be set by it's children classes
	private String weaponType = "None";
	private boolean ranged = false;
	
	String[] bonuses; //used for bonus stats
	
	
	//Modifiers used later on in game

	/**
	 * 
	 * @param game
	 * @param name
	 * @param decription
	 * @param rarity
	 * @param minDamage
	 * @param maxDamage
	 * @param distance
	 * @param spriteName
	 */
	public ItemWeapon(Game game, String name, String decription, String rarity, String type, int minDamage, int maxDamage, int distance, String[] bonuses, String spriteName) {
		super(game, name, decription, rarity, type, spriteName);
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.distance = distance;
		this.bonuses = bonuses;
	}


	public int getMinDamage() {
		return minDamage;
	}


	public void setMinDamage(int minDamage) {
		this.minDamage = minDamage;
	}


	public int getMaxDamage() {
		return maxDamage;
	}


	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}


	public int getDistance() {
		return distance;
	}


	public void setDistance(int distance) {
		this.distance = distance;
	}


	public String getWeaponType() {
		return weaponType;
	}


	public void setWeaponType(String weaponType) {
		this.weaponType = weaponType;
	}


	public boolean isRanged() {
		return ranged;
	}


	public void setRanged(boolean ranged) {
		this.ranged = ranged;
	}

	// TODO move to Being
    public void fireProjectile(Point target, int lifeSpan, String spriteName, boolean friendly, Being being)
    {
        //get a random dmg number from the weapon
        int projectileDamage = being.getTheGame().getRandomGenerator().nextInt(getMaxDamage() - getMinDamage()) + getMinDamage();
        //fire the arrow at the speed of the ranged weapon
        Projectile projectile = new Projectile(being.getTheGame(), being, being.getX(), being.getY(), getDistance(), projectileDamage, lifeSpan, friendly, spriteName);

        Point moveTo = target;
        projectile.setDestination(moveTo, getDistance());
        being.getTheGame().getEntityHash().add(projectile);

    }
}
