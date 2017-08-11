package name_pending.Windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

import name_pending.Game;

/**
 * This class will be the base controller for all the windows
 * I.E. What will be used to create and destory windows etc
 * @author Hawox
 *
 */
public class GameWindowManager {
	
	Point mouseClick = new Point (0,0);
	
	//All the hotkeys that are used in the inventory manager
	

	protected Game theGame = null;
	//Will house all the game active windows
	private HashSet<GameWindow> gameWindowHash = new HashSet<GameWindow>();
	
	private GameWindowManagerHIDListener gameWindowManagerHIDListener;
	
	public GameWindowManager(Game game)
	{
		this.theGame = game;
		//create our inventory
		getGameWindowHash().add(new GameWindowInventory(theGame, 600, 300, 190, 290));
		getGameWindowHash().add(new GameWindowEquipment(theGame, 300, 300, 190, 290));
		
		gameWindowManagerHIDListener = new GameWindowManagerHIDListener();
		getTheGame().getFrame().addKeyListener(gameWindowManagerHIDListener);
		getTheGame().getGameArea().addMouseListener(gameWindowManagerHIDListener);
		Game.addText(theGame.getConsole(), "Game Window Manager Loaded.");
	}
	
	/**
	 * Will need to check on mouse events and key events to know when clicked or when it needs to close, etc
	 * Check for keyboard input
	 * @param keyCode key used
	 * @param pressed state = [true] pressed | [false] released
	 */
	public void keyCheck(int keyCode,boolean pressed) //copied from Entity
	{
		if(pressed == false)
		{
			if(keyCode == getTheGame().getHotkeys().getHOTKEY_inventory())
			{
				this.openInventory();
			}
			
			if(keyCode == getTheGame().getHotkeys().getHOTKEY_equipment())
			{
				this.openEquipment();
			}
		}
	}

	/**
	 * Will be triggered by the FrameMouseListener event
	 * @param event Event that was passed from the MouseListener
	 * @param eventType Compatable Strings "clicked", "pressed", and "released.
	 */
	public void mouseCheck(MouseEvent event,String eventType)
	{
		//show where the mouse clicked
		if(getTheGame().isDEBUG())
			this.mouseClick = new Point(event.getX(), event.getY());
			
		for(GameWindow gw: getGameWindowHash())
			gw.mouseCheck(event, eventType);
	}

	/**
	 * Paint all the windows ALL THE WINDOWS!
	 * @param g Graphics
	 */
	public void paintWindows(Graphics g)
	{	
		for(GameWindow gw: getGameWindowHash())
			gw.paintMe(g);
		
				if(getTheGame().isDEBUG())
				{
					g.setColor(Color.PINK);
					g.drawRect(this.mouseClick.x, this.mouseClick.y, 1, 1);
				}
	}
	
	/*
	 * Updates all the windows
	 */
	public void updateWindows()
	{
		for(GameWindow gw : getGameWindowHash())
			gw.update();
	}
	
	/*
	 * Override to update a specific window
	 */
	public void updateWindows(String name)
	{
		for(GameWindow gw : getGameWindowHash())
			if(gw.getName() == name)
				gw.update();
	}
	
	
	//Methods for opening windows
	public void openInventory()
	{
		for(GameWindow gw : gameWindowHash)
		{
			if(gw.getName() == "InventoryWindow")
			{
				gw.toggleVisibility();
				break;
			}
		}
	}
	
	public void openEquipment()
	{
		for(GameWindow gw : gameWindowHash)
		{
			if(gw.getName() == "EquipmentWindow")
			{
				gw.toggleVisibility();
				break;
			}
		}
	}
	
	
	class GameWindowManagerHIDListener implements KeyListener, MouseListener
	{

		public void keyPressed(KeyEvent e) {
			sendKeyEvent(e.getKeyCode(), true);
		}

		public void keyReleased(KeyEvent e) {
			sendKeyEvent(e.getKeyCode(), false);
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
			sendMouseEvent(event, "clicked");
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			sendMouseEvent(event, "pressed");
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			sendMouseEvent(event, "released");
		}
		
		
		
		private void sendMouseEvent(MouseEvent event, String eventType)
		{
			theGame.getGameWindowManager().mouseCheck(event, eventType);
		}
		
		private void sendKeyEvent(int keyCode, boolean pressed)
		{
			theGame.getGameWindowManager().keyCheck(keyCode, pressed);
		}
		
		
		//Not currently used
		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void keyTyped(KeyEvent e) {}
	}
	
	/**
	 * Getters and setters
	 */
	
	
	public Game getTheGame() {
		return theGame;
	}

	public void setTheGame(Game theGame) {
		this.theGame = theGame;
	}

	public HashSet<GameWindow> getGameWindowHash() {
		return gameWindowHash;
	}

	@SuppressWarnings("unused")
	private void setGameWindowHash(HashSet<GameWindow> gameWindowHash) {
		this.gameWindowHash = gameWindowHash;
	}
}
