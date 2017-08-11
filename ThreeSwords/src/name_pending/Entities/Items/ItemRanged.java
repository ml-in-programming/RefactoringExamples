package name_pending.Entities.Items;

import name_pending.Game;

public class ItemRanged extends ItemWeapon{

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
	public ItemRanged(Game game, String name, String decription, String rarity, int minDamage, int maxDamage, int distance, String[] bonuses, String spriteName) {
		super(game, name, decription, rarity, "rangedweapon", minDamage, maxDamage, distance, bonuses, spriteName);
		setWeaponType("ranged");
		setRanged(true);
	}

}
