package name_pending.Entities.Items;

import name_pending.Game;

public class ItemMelee extends ItemWeapon{

	/**
	 * 
	 * @param game
	 * @param name
	 * @param decription
	 * @param rarity
	 * @param type
	 * @param minDamage
	 * @param maxDamage
	 * @param distance
	 * @param bonuses
	 */
	public ItemMelee(Game game, String name, String decription, String rarity, int minDamage, int maxDamage, int distance, String[] bonuses) {
		super(game, name, decription, rarity, "meleeweapon", minDamage, maxDamage, distance, bonuses, "ItemSword.png");
		setWeaponType("melee");
		setRanged(false);
	}

}
