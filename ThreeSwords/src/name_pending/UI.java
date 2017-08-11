package name_pending;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Vector;

import name_pending.DataBanks.PlayerData;
import name_pending.Entities.Player;

/**
 * This will be in charge of drawing everything in the game that is considered the game UI
 * @author Hawox
 *
 */
public class UI {
	private Game theGame;
	private UiHIDListener uiHIDListener;
	
	private HashSet<UiButton> uiButtons = new HashSet<UiButton>();
	
	//Decides whether or not game windows are drawn
	//private boolean doDrawInventory = false;
	
	UI(Game game)
	{
		theGame = game;
		this.uiHIDListener = new UiHIDListener();
		getTheGame().getFrame().addKeyListener(uiHIDListener);
		this.uiButtons.add(new UiButton(game, game.getGameArea().getWidth() - 10, 20, "inventory"));
		this.uiButtons.add(new UiButton(game, game.getGameArea().getWidth() - 10, 50, "equipment"));
		Game.addText(theGame.getConsole(), "UI loaded.");
	}


	//Do all the fun drawy stuff when this is called
	public void drawUI(Graphics g)
	{	
		//Draw player stuff only if there is a player on screen
		Player player = theGame.getPlayer();
		if(player != null)
			drawPlayerStuff(g, player);

		drawGameWindows(g);

		//draw ui buttons
		for(UiButton uib : this.uiButtons)
			uib.paintMe(g);
		
		/**DEBUG STUFF!**/
		if(theGame.isDEBUG())
			drawDebugStuff(g);
	}
	
	private void drawPlayerStuff(Graphics g, Player player)
	{
		PlayerData playerData = Player.getPlayerData(theGame);
		int frameWidth = theGame.getFrame().getWidth();
		//int frameHeight = theGame.getFrame().getHeight();
		//Draw player related HUD
		
		//exp bar
		int expbarX = 5;
		int expbarY = 5;
		int expbarWidth = frameWidth - 20;
		int expWidth = 0;
		int expPercent = (int) ( ( (float) playerData.getExperiance() / (float) playerData.getExperianceTillLevel() ) * 100);
		if(playerData.getExperiance() == 0) //avoid dividing by zero
			expWidth = 0;
		else
			expWidth = (int) ( ( (float) playerData.getExperiance() / (float) playerData.getExperianceTillLevel() ) * expbarWidth);
		//Color expColor = Color.WHITE;
		g.setColor(Color.BLACK);
		g.fillRect(expbarX, expbarY, expbarWidth, 12);
		g.setColor(Color.WHITE);
		g.fillRect(expbarX, expbarY, expWidth, 12);
		g.setColor(Color.BLACK);
		g.drawRect(expbarX, expbarY, expbarWidth, 12);
		g.setColor(Color.white);
		g.setXORMode(Color.red);
		g.drawString(Integer.toString(expPercent) + "%", ( expbarX + ( expbarWidth / 2) ) - 10, expbarY + 11);
		//g.drawString("Experiance", expbarX + 1, expbarY + 11);
		g.setPaintMode();
		
		//Level
		//g.drawString("Level: " + Integer.toString(getTheGame().getPlayerData().getLevel()), frameWidth-100, 20);
		//g.setColor(Color.white);
		g.setXORMode(Color.red);
		g.drawString("Level " + Integer.toString(Player.getPlayerData(getTheGame()).getLevel()), expbarX + 1, expbarY + 11);
		g.setPaintMode();
		
		//Health bar
		int healthbarX = 5;
		int healthbarY = 20;
		int healthbarWidth = 100;
		int healthWidth = 0;
		int healthPercent = (int) ( ( (float) theGame.getPlayer().getHealth() / (float) Player.getPlayerData(theGame).getMaxHealth() ) * 100);
		if(getTheGame().getPlayer().getHealth() == 0) //avoid dividing by zero
			healthWidth = 0;
		else
			healthWidth = (int) ( ( (float) theGame.getPlayer().getHealth() / (float) Player.getPlayerData(theGame).getMaxHealth() ) * healthbarWidth);
		g.setColor(Color.RED);
		g.fillRect(healthbarX, healthbarY, healthbarWidth, 12);
		g.setColor(Color.GREEN);
		g.fillRect(healthbarX, healthbarY, healthWidth, 12);
		g.setColor(Color.BLACK);
		g.drawRect(healthbarX, healthbarY, healthbarWidth, 12);
		g.setColor(Color.white);
		g.setXORMode(Color.red);
		g.drawString(Integer.toString(healthPercent) + "%", ( healthbarX + ( healthbarWidth / 2) ) - 10, healthbarY + 11);
		//g.drawString("Experiance", expbarX + 1, expbarY + 11);
		g.setPaintMode();

		//Fatigue bar
		int fatiguebarX = 5;
		int fatiguebarY = 35;
		int fatiguebarWidth = 100;
		int fatigueWidth = 0;
		int fatiguePercent = (int) ( ( (float) Player.getPlayerData(theGame).getFatigue() / (float) Player.getPlayerData(theGame).getMaxFatigue() ) * 100);
		if(Player.getPlayerData(getTheGame()).getFatigue() == 0) //avoid dividing by zero
			fatigueWidth = 0;
		else
			fatigueWidth = (int) ( ( (float) Player.getPlayerData(theGame).getFatigue() / (float) Player.getPlayerData(theGame).getMaxFatigue() ) * fatiguebarWidth);
		g.setColor(Color.BLUE);
		g.fillRect(fatiguebarX, fatiguebarY, fatiguebarWidth, 12);
		g.setColor(Color.YELLOW);
		g.fillRect(fatiguebarX, fatiguebarY, fatigueWidth, 12);
		g.setColor(Color.BLACK);
		g.drawRect(fatiguebarX, fatiguebarY, fatiguebarWidth, 12);
		g.setColor(Color.white);
		g.setXORMode(Color.red);
		g.drawString(Integer.toString(fatiguePercent) + "%", ( fatiguebarX + ( fatiguebarWidth / 2) ) - 10, fatiguebarY + 11);
		//g.drawString("Experiance", expbarX + 1, expbarY + 11);
		g.setPaintMode();
		
		
		
		
		
		//Attack time bar
		int attackTimebarX = 100;/*getTheGame().getPlayer().getX() - getTheGame().getGameArea().getOriginPoint().x*/;
		int attackTimebarY = 100; /*( getTheGame().getPlayer().getY() -  - getTheGame().getGameArea().getOriginPoint().y) + 40;*/
		int attackTimebarWidth = 32;
		int attackTimeWidth = 0;
		//int attackTimePercent = (int) ( ( (float) theGame.getPlayer().getAttackDelay() / (float) theGame.getPlayer().getAttackDelayLast() ) * 100);
		if(theGame.getPlayer().getAttackDelay() == 0) //avoid dividing by zero
			attackTimeWidth = 0;
		else
			attackTimeWidth = (int) ( ( (float) theGame.getPlayer().getAttackDelay() / (float) theGame.getPlayer().getAttackDelayLast() ) * attackTimebarWidth);
		//g.setColor(Color.Blue);
		//g.fillRect(attackTimebarX, attackTimebarY, attackTimebarWidth, 12);
		g.setColor(Color.YELLOW);
		g.fillRect(attackTimebarX, attackTimebarY, attackTimeWidth, 2);
		g.setColor(Color.BLACK);
		g.drawRect(attackTimebarX, attackTimebarY, attackTimebarWidth, 2);
		g.setColor(Color.white);
		g.setXORMode(Color.red);
		//g.drawString(Integer.toString(attackTimePercent) + "%", ( attackTimebarX + ( attackTimebarWidth / 2) ) - 10, attackTimebarY + 11);
		//g.drawString("Experiance", expbarX + 1, expbarY + 11);
		g.setPaintMode();

	}


	private void drawDebugStuff(Graphics g)
	{
		/*
		g.setColor(Color.YELLOW);
		g.drawString("Sprite Array Size: " + theGame.getResourceLoader().getImages().size(), 200, 200);
		String[] wrap = UI.wrapText(theGame.getResourceLoader().getImages().toString(), 100);
		for(int i=0; i<wrap.length; i++)
		{
			g.drawString(wrap[i], 200, 215+(15*i));
		}
		Sprite s = theGame.getResourceLoader().getSprite("Error.png");
		g.drawString(s.toString(), 200, 150);
		s.setPosition(100, 100);
		s.paint(g);
		*/
		
	}
	
	public void drawGameWindows(Graphics g)
	{
		theGame.getGameWindowManager().paintWindows(g);
	}
	
	
	/** Keycheck method **/
	public void keyCheck(int keyCode, boolean pressed)
	{
		
	}
	
	/**
	 * Static Methods
	 */
	
	/**
	 * Obtained at http://progcookbook.blogspot.com/2006/02/text-wrapping-function-for-java.html
	 * Author Robert Hanson (I think)
	 * @param text
	 * @param len
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static String [] wrapText (String text, int len)
	{
	  // return empty array for null text
	  if (text == null)
	  return new String [] {};

	  // return text if len is zero or less
	  if (len <= 0)
	  return new String [] {text};

	  // return text if less than length
	  if (text.length() <= len)
	  return new String [] {text};

	  char [] chars = text.toCharArray();
	  Vector lines = new Vector();
	  StringBuffer line = new StringBuffer();
	  StringBuffer word = new StringBuffer();

	  for (int i = 0; i < chars.length; i++) {
	    word.append(chars[i]);

	    if (chars[i] == ' ') {
	      if ((line.length() + word.length()) > len) {
	        lines.add(line.toString());
	        line.delete(0, line.length());
	      }

	      line.append(word);
	      word.delete(0, word.length());
	    }
	  }

	  // handle any extra chars in current word
	  if (word.length() > 0) {
	    if ((line.length() + word.length()) > len) {
	      lines.add(line.toString());
	      line.delete(0, line.length());
	    }
	    line.append(word);
	  }

	  // handle extra line
	  if (line.length() > 0) {
	    lines.add(line.toString());
	  }

	  String [] ret = new String[lines.size()];
	  int c = 0; // counter
	  for (Enumeration e = lines.elements(); e.hasMoreElements(); c++) {
	    ret[c] = (String) e.nextElement();
	  }

	  return ret;
	}
	
	
	class UiHIDListener implements KeyListener
	{

		public void keyPressed(KeyEvent e) {
			sendKeyEvent(e.getKeyCode(), true);
		}

		public void keyReleased(KeyEvent e) {
			sendKeyEvent(e.getKeyCode(), false);
		}

		/*@Override
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
			this.
		}*/
		
		private void sendKeyEvent(int keyCode, boolean pressed)
		{
			keyCheck(keyCode, pressed);
		}
		
		
		/*Not currently used
		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}*/

		public void keyTyped(KeyEvent e) {}
	}
	
	
	
	
	/**
	 * 
	 * Getters and setters
	 * @return
	 */
	
	
	public Game getTheGame() {
		return theGame;
	}


	public void setTheGame(Game theGame) {
		this.theGame = theGame;
	}


	public UiHIDListener getUiHIDListener() {
		return uiHIDListener;
	}


	public void setUiHIDListener(UiHIDListener uiHIDListener) {
		this.uiHIDListener = uiHIDListener;
	}


	public HashSet<UiButton> getUiButtons() {
		return uiButtons;
	}


	public void setUiButtons(HashSet<UiButton> uiButtons) {
		this.uiButtons = uiButtons;
	}


	/*public boolean isDrawInventory() {
		return doDrawInventory;
	}


	public void setDrawInventory(boolean drawInventory) {
		this.doDrawInventory = drawInventory;
	}*/
	
	//All of it's other draw methods should be private because nothing else should call them
}
