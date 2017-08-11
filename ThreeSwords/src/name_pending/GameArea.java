package name_pending;

import java.awt.*;
import java.util.ConcurrentModificationException;

import javax.swing.JPanel;

import name_pending.Entities.Entity;
import name_pending.Entities.Tile;

@SuppressWarnings("serial")
public class GameArea extends JPanel {
	private Game theGame;
	private Room currentRoom;
	//Where the center of the screen is in the view of the game world
	private Point originPoint = new Point(0,0);

	GameArea(Game theGame)
	{
		this.theGame = theGame;
		//this.setPreferredSize(new Dimension(8000, 6000));
		this.currentRoom = new Room(2000,2000);
		
	}

	

	public void paintComponent(Graphics g)
	{
		Graphics offgc;
		Image offscreen = null;
		Rectangle box = g.getClipBounds();

		// create the offscreen buffer and associated Graphics
		offscreen = createImage(box.width, box.height);
		offgc = offscreen.getGraphics();
		// clear the exposed area
		offgc.setColor(getBackground());
		offgc.fillRect(0, 0, box.width, box.height);
		offgc.setColor(getForeground());
		// do normal redraw
		offgc.translate(-box.x, -box.y);
		paint(offgc);
		// transfer offscreen to window
		g.drawImage(offscreen, box.x, box.y, this);

		//clear the image to draw onto
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		//tile the ground
		Tile tile = new Tile(theGame, "tileGrass.png"); //getTheGame().getResourceLoader().getSprite("tileGrass.png");
		for(int xi = 0; xi < this.currentRoom.width; xi += tile.getSprite().getWidth())
		{
			for(int yi = 0; yi < this.currentRoom.height; yi += tile.getSprite().getHeight())
			{
				tile.paintMeAt(xi, yi, g);
			}
		}
		
		//get our new origin at the playerswwww
		if(getTheGame().getPlayer() != null)
		{
			//set at player
			Point center = new Point(getTheGame().getPlayer().getX(), getTheGame().getPlayer().getY());
			//move to "top left" of screen
			int newX = center.x - (this.getWidth() / 2);
			int newY = center.y - (this.getHeight() / 2);
			this.setOriginPoint(new Point(newX, newY));
		}

		//Draw our entities
		try{
			for(Entity e : theGame.getEntityHash())
			{
				e.paintMe(g);
			}
		}catch(ConcurrentModificationException e){}

		
		//Draw the ui last so it goes ontop of everything
		try{
			theGame.getUi().drawUI(g);
		}catch(NullPointerException e){}
			
		//Draw point at screen center
		if(theGame.isDEBUG())
			g.drawRect(theGame.getFrame().getWidth()/2 - 1, theGame.getFrame().getHeight()/2 - 1, 3, 3);
		
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, this.currentRoom.width, this.currentRoom.height);
		
	}

	public Game getTheGame() {
		return theGame;
	}

	public void setTheGame(Game theGame) {
		this.theGame = theGame;
	}

	public Point getOriginPoint() {
		return originPoint;
	}

	public void setOriginPoint(Point reletivePoint) {
		this.originPoint = reletivePoint;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}
}