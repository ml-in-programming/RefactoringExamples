package name_pending.DataBanks;

import java.util.HashSet;

import name_pending.Game;
import name_pending.Resistance;
import name_pending.Entities.Enemy;

public class EnemyDataBank {
	//Storage for all enemies
	HashSet<EnemyTemplate> enemies = new HashSet<EnemyTemplate>();
	Game theGame;
	
	EnemyTemplate nullEnemy;
	
	public EnemyDataBank(Game theGame)
	{
		this.theGame = theGame;
		nullEnemy = new EnemyTemplate(theGame, 0, 0, 0, "Null", "Error.png", 0, 0, 0, 0, null);
		loadEnemies();
	}
	
	public Enemy getEnemy(String name, int x, int y)
	{
		for(EnemyTemplate e : enemies)
		{
			if(e.name == name)
			{
				Enemy returnMe = (Enemy) e.get();
				returnMe.setX(x);
				returnMe.setY(y);
				return returnMe;
			}
		}
		
		return nullEnemy.get();
	}
	
	public Enemy getEnemy(String name)
	{
		return getEnemy(name, 0, 0);
	}
	
	//Use this so we don't actually store a bunch of ojects
	private class EnemyTemplate
	{
		Game theGame;
		int x;
		int y;
		int speed;
		String name;
		String spriteName;
		int health;
		int defence;
		int attack;
		int dexterity;
		Resistance[] resist;
		
		EnemyTemplate(Game theGame, int x, int y, int speed, String name, String spriteName, int health,
			int defence, int attack, int dexterity, Resistance[] resist)
			{
				this.theGame = theGame;
				this.x = x;
				this.y = y;
				this.speed = speed;
				this.spriteName = spriteName;
				this.name = name;
				this.health = health;
				this.defence = defence;
				this.attack = attack;
				this.dexterity = dexterity;
				this.resist = resist;
			}
		
		public Enemy get()
		{
			return new Enemy(theGame, x, y, speed, name, spriteName, health,
			defence, attack, dexterity, resist);
		}
	}
	
	private boolean loadEnemies()
	{
		enemies.add(new EnemyTemplate(theGame, 0, 0, 7, "Zombie", "Zombie.png", 10, 5, 5, 2, null));
		Game.addText(theGame.getConsole(), "Enemy Data Bank loaded.");
		return true;
	}
}
