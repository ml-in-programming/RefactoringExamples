package name_pending;

import java.util.ConcurrentModificationException;
import java.util.HashSet;

import name_pending.Entities.Entity;

public class GameLoop implements Runnable{

	//Pass an instance of theGame to handle all the fun stuff
	Game theGame = null;

	int frames = 0;

	GameLoop(Game game)
	{
		this.theGame = game;
	}

	@Override
	public void run() {
		Game.addText(theGame.getConsole(), "Gameloop Started.");
		do{
		HashSet<Entity> entities = theGame.getEntityHash();
		
		/***Run entity step events***/
		this.steps(entities);
		
		/***Check for collisions***/
		this.collisions(entities);

		/***Paint everything***/
		this.paintStuff(entities);

		//1000 ms = 1 second
		//33 ms is around 30 fps
		try {
			Thread.sleep(33);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} //We want to get 30 fpsish
	}while(true);
		//realized this causes problems, not sure what I was thinking, let's do a while loop
		//this.run(); // we never want the game loop to stop running

	}

	/**
	 * Moved down here to make the run methods not as cramped with data
	 * @param entities
	 */
	private void collisions(HashSet<Entity> entities)
	{
		try{
		for(Entity e : entities)
		{
			e.checkCollisions();
		}
		}catch(ConcurrentModificationException e){}
	}

	private void paintStuff(HashSet<Entity> entities)
	{
		//theGame.getGameArea().repaint();
		theGame.getFrame().repaint();
	}

	private void steps(HashSet<Entity> entities)
	{
		try{
		for(Entity e : entities)
		{
			e.step();
		}
		}catch(ConcurrentModificationException e){}
	}

}
