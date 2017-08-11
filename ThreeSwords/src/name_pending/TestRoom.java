package name_pending;

import java.awt.Point;

import name_pending.Entities.Enviroment;
import name_pending.Entities.Player;
import name_pending.Entities.Items.ItemDrop;

/**
 * Used to check stuff out in the games test room
 *
 */
public class TestRoom {
	
	Game theGame;
	
	TestRoom(Game theGame)
	{
		this.theGame = theGame;
	}
	
	public void start()
	{
		//theGame.getFrame().setSize(800, 600);
		theGame.getGameArea().setCurrentRoom(new Room(2000, 2000));
		
		//add the player
		//change the cursor
		theGame.changeCursor("CursorCrossheir.png", new Point(15,15));
		theGame.getEntityHash().add(new Player(theGame, 800, 800));
		//theGame.getEntityHash().add(new Player(theGame, 400, 100));
		//create a bow
		//AH HAH! Note to self, you can't use a single Item for multiple ItemDrops. That was stupid of me.
		//ItemBow bow = new ItemBow(theGame, "SuperBow", "This is the SuperBow of epicness", "white", 10, 100, 150, null);
		//put it in the players inventory

		for(int i=0; i<10; i++){
			ItemDrop itemDrop = new ItemDrop(theGame, theGame.getItemDataBank().getRangedWeapon("SuperBow"), 1100 + (50*i), 1000 + (50*i), 0);
			ItemDrop itemDropSword = new ItemDrop(theGame, theGame.getItemDataBank().getItem("SuperSword"), 900 + (50*i), 1000 + (50*i), 0);
			theGame.getEntityHash().add(itemDrop);
			theGame.getEntityHash().add(itemDropSword);
		}
		
		theGame.getEntityHash().add(new Enviroment(theGame, 1002, 1000, 0, "Center"));
		
		theGame.getEntityHash().add(theGame.getEnemyDataBank().getEnemy("Zombie", 1100, 900));
	//	for(int i=0; i<10; i++){
			//ItemDrop itemDrop = new ItemDrop(theGame, new ItemBow(theGame, "SuperBow", "This is the SuperBow of epicness", "white", 10, 100, 150, null), 900 + (50*i), 1000, 0);
	//		Enviroment envi = new Enviroment(theGame, 950, 1000 + (50*i), 0, "Rock");
	//		theGame.getEntityHash().add(envi);
	//	}
		/*
		ItemDrop itemDropa = new ItemDrop(theGame, new ItemBow(theGame, "SuperBow", "This is the SuperBow of epicness", "white", 10, 100, 150, null), 300, 350);
		theGame.getEntityHash().add(itemDropa);
		
		ItemDrop itemDropb = new ItemDrop(theGame, new ItemBow(theGame, "SuperBow", "This is the SuperBow of epicness", "white", 10, 100, 150, null), 300, 400);
		theGame.getEntityHash().add(itemDropb);*/
		Game.addText(theGame.getConsole(), "Testroom loaded.");
	}

}
