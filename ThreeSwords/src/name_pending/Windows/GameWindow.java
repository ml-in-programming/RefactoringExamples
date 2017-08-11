package name_pending.Windows;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import name_pending.Game;

/**
 * This will be the parent class for all the game windows that will popup in game
 */
public class GameWindow {
	
	//Variables all windows will need
	Game theGame = null;
	
	//This is assuming all my windows will be rectangles later on
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean visiable = false;

	//These are here incase I need them in the future.
	private String name = "NoNameWindow";
	private String windowType = "NoWindowType";
	
	/**
	 * 
	 * @param theGame Base Game instance
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param name
	 * @param type
	 */
	GameWindow(Game theGame, int x, int y, int width, int height, String name, String type)
	{
		this.theGame = theGame;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.windowType = type;
	}

	/**
	 * Will need to check on mouse events and key events to know when clicked or when it needs to close, etc
	 * Check for keyboard input
	 * @param keyCode key used
	 * @param pressed state = [true] pressed | [false] released
	 */
	public void keyCheck(int keyCode,boolean pressed) //copied from Entity
	{

	}
	
	/**
	 * Draws the window to the frame
	 * @param g
	 */
	public void paintMe(Graphics g)
	{
		
	}
	
	/**
	 * Will be triggered by the FrameMouseListener event
	 * @param event Event that was passed from the MouseListener
	 * @param eventType Compatable Strings "clicked", "pressed", and "released.
	 */
	public void mouseCheck(MouseEvent event,String eventType)
	{

	}
	
	public void toggleVisibility()
	{
		if(isVisiable() == true)
			setVisiable(false);
		else
			setVisiable(true);
	}
	
	/**
	 * Called when the data inside the window needs to be updated
	 * Ex: Inventory updated when an item is picked up
	 */
	public void update()
	{
		
	}
	
	/**
	 * Remove it from the windows list which should delete it
	 */
	public void onDelete()
	{
		this.theGame.getGameWindowManager().getGameWindowHash().remove(this);
	}
	
	/**
	 * Getters and setters
	 * @return
	 */

	public Game getTheGame() {
		return theGame;
	}

	public void setTheGame(Game theGame) {
		this.theGame = theGame;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWindowType() {
		return windowType;
	}

	public void setWindowType(String windowType) {
		this.windowType = windowType;
	}

	public boolean isVisiable() {
		return visiable;
	}

	public void setVisiable(boolean visiable) {
		this.visiable = visiable;
	}
}