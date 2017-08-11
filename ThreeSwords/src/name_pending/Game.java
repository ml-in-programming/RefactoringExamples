package name_pending;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JFrame;

import name_pending.DataBanks.EnemyDataBank;
import name_pending.DataBanks.ItemDataBank;
import name_pending.DataBanks.PlayerData;
import name_pending.DataBanks.ResourceDataBank;
import name_pending.Entities.Entity;
import name_pending.Entities.Player;
import name_pending.Pathing.AStarPathing;
import name_pending.Windows.GameWindowManager;


	//Pet cat AND THIS https://i.chzbgr.com/maxW500/6831610368/h9D0FC08E/

/**
 * Code snipits to remember
 * Set cursor to normal
 * setCursor (Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
 *
 */
public class Game
{
	//quick checks to see if the game is is test or debug mode
	private static boolean TESTING = true;
	private boolean DEBUG = true;
	private TestRoom testRoom = new TestRoom(this);
	private boolean fullscreen = false;
	
	//Houses all of the games sprites
//	ResourceDataBank spriteLoader = new ResourceDataBank();

	//This will contain every entity currently in the game to run all of their internal functions
	private HashSet<Entity> entityHash = new HashSet<Entity>();
	//EDIT:Moved to the GameWindowManagerClass
	//This will contain every window currently on screen and run all of their interal finctions
	//private HashSet<GameWindow> gameWindowHash = new HashSet<GameWindow>();
	
	//the keylistener
//	private FrameKeyListener frameKeyListener;
	//the mouse listener
//	private FrameMouseListener frameMouseListener;

	//The main game thread loop
	private GameLoop gameLoop = null;
	
	//all the hotkeys
	private Hotkeys hotkeys = new Hotkeys();

	//Graphicsy Stuff
	private JFrame frame = new JFrame();
	private GameArea gameArea;
	private ConsoleWindow console = new ConsoleWindow();
	private UI ui; //base UI for the game
	
	//Manages all the windows in the game
	private GameWindowManager gameWindowManager;
	
	//This will hold all the important data for players
	public PlayerData playerData = new PlayerData(this);
	
	//Al of our random number needs
	Random randomGenerator = new Random();

	//All of our fun databanks
	private EnemyDataBank enemyDataBank;
	private ItemDataBank itemDataBank;

	private Sprite currentCursor = null;
	
	//Pathing thread
	AStarPathing pathingThread;

	// TODO move to ConsoleWindow
	public static void addText(ConsoleWindow consoleWindow, String s)
	{
		consoleWindow.console.append("\n>>" + s);
	}

	//Launch when the game starts
	public void go()
	{
		//Get the console loaded before anyhting happens so we can see checks on everything
		setupConsole();

		//Startup the pathing thread
		pathingThread = new AStarPathing(this);
		Thread thePathingThread = new Thread(pathingThread);
		thePathingThread.start();
		
		//load our databanks
		enemyDataBank = new EnemyDataBank(this);
		itemDataBank = new ItemDataBank(this);
		
		//setup panels
		gameArea = new GameArea(this);		
		
		setupFrame();
		
		gameWindowManager = new GameWindowManager(this);
		ui = new UI(this);

		//setup the  mouse and key listeners
//		frameKeyListener = new FrameKeyListener(this);
//		frame.addKeyListener(frameKeyListener);
//		frameMouseListener = new FrameMouseListener(this);
//		frame.addMouseListener(frameMouseListener);

		if(TESTING)
			testRoom.start();

		//load the sprites
//		spriteLoader.loadImages();
		
		//Start the loop
		gameLoop = new GameLoop(this);
		addText(console, "Game done getting setup.");
		gameLoop.run();
		
		//this.playSound("fieldTheme.mp3");
	}

	//Sets all of our default variables to get the frame ready for action
	private void setupFrame()
	{	
		//Create our frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(gameArea);
		
		//frame.setSize(1280,768);
		//frame.setSize(800,600);
		frame.setSize(1024, 768);
		frame.setResizable(false);
		frame.setVisible(true);
		
		//gameArea.setSize(800, 600);
		
		//frame.add();
		
		if(fullscreen)
		{
			//frame.setUndecorated(true);
			/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		    frame.setBounds(0,0,screenSize.width, screenSize.height);*/
			/*
			GraphicsDevice gd;
			if(gd.isFullScreenSupported())
				gd.setFullScreenWindow(getFrame());*/
		}

		//Gets double buffering ready
		//frame.createBufferStrategy(2);
		
				//loadCursors();
		addText(console, "Frame Loaded.");
	}
	
	//creates the console in a seperate window
	private void setupConsole()
	{
		//Create our frame
		console.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setSize(1280,768);
		//frame.setSize(800,600);
		console.setSize(400, 200);
		console.setLocation(new Point(1100,500));
		console.setTitle("Console");
		console.setVisible(true);
		console.setResizable(false);
		addText(console, "Console loaded.");
	}

	//Load all of the games cursors into Memory
	/*
	 * Theres probably a way to do this based on loading everything in a folder and assigning the files
	 * names as the string but I want to remember and have control over what cursors I have at my disposal
	 * so this might be some clunky code
	 */
/*	public void loadCursors()
	{
		//Empty the hashset so we don't get any duplicates
		this.getCursors().clear();

		//Load in all the cursors individually
		Sprite sprCrossheir = null;
		try
		{
			sprCrossheir = new Sprite(ImageIO.read(this.getClass().getResource("Entities/Sprites/Cursor_Crossheir.png")));
		}catch(IOException e){}

		//add them all to the hashmap
		this.getCursors().put("Crossheir", sprCrossheir);
	}*/

	/**
	 * Returns true if it changed the cursor
	 * @param name -- String tied to the cursor sprite
	 */
	public boolean changeCursor(String name, Point clickPoint)
	{
		Sprite newCursor = null;

		//if(this.getResourceLoader().getImages().containsKey(name))
		//{
			//Change the variable
			newCursor = ResourceDataBank.getSprite(name);
			newCursor.setRefPixel(clickPoint.x, clickPoint.y);
			//Now actually change the cursor that's drawn
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = newCursor.getImage();
			Cursor c = toolkit.createCustomCursor(image, new Point(15,15), name);
			frame.setCursor(c);
			return true;
		//}else
		//	return false;	//Cursor does not exist
			
		
		/*if(this.getCursors().containsKey(name))
		{
			//Change the variable
			newCursor = this.getCursors().get(name);
			//Now actually change the cursor that's drawn
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = newCursor.getImage();
			Cursor c = toolkit.createCustomCursor(image, new Point(15,15), name);
			frame.setCursor(c);
			return true;
		}else
			return false;	//Cursor does not exist
			*/
		/*
		if(newCursor != null)
		{
			this.setCurrentCursor(newCursor);
			return true;
		}else
			return false; // cursor change failed at some point*/
	}


	
	

	//Lots of other methods are going to want to get the player object so lets make a centeral method here
	public Player getPlayer()
	{
		//Get the player
		Player player = null;
		for(Entity e : this.getEntityHash())
		{
			if(e.getName() == "Player")
			{
				player = (Player)e;
				break;
			}
		}
		return player;
	}
	
	public void playSound(String soundName)
	{
		//Media hit = new Media(soundName);
		//MediaPlayer mediaPlayer = new MediaPlayer(hit);
		//mediaPlayer.play();
		/*try {
	          Clip clip = AudioSystem.getClip();
	          AudioInputStream inputStream = this.getResourceLoader().getSound(soundName);
	          clip.open(inputStream);
	          clip.start(); 
	        } catch (Exception e) {
	          e.printStackTrace();
	        }*/
	}
	

	/*****
	 * Getters and setters
	 */

	/* MODIFIED
	 * This is causing too many concurrent modifications exceptions.
	 * Lets return a clone of it instead
	 */
	@SuppressWarnings("unchecked")
	public HashSet<Entity> getEntityHash() {
		return entityHash;
	}

	public void setEntityHash(HashSet<Entity> entityHash) {
		this.entityHash = entityHash;
	}

	/*public FrameKeyListener getFrameKeyListener() {
		return frameKeyListener;
	}

	public void setFrameKeyListener(FrameKeyListener frameKeyListener) {
		this.frameKeyListener = frameKeyListener;
	}*/

	public static boolean isTESTING() {
		return TESTING;
	}

	public static void setTESTING(boolean tESTING) {
		TESTING = tESTING;
	}

	public boolean isDEBUG() {
		return DEBUG;
	}

	public void setDEBUG(boolean dEBUG) {
		DEBUG = dEBUG;
	}

	public TestRoom getTestRoom() {
		return testRoom;
	}

	public void setTestRoom(TestRoom testRoom) {
		this.testRoom = testRoom;
	}

	public GameLoop getGameLoop() {
		return gameLoop;
	}

	public void setGameLoop(GameLoop gameLoop) {
		this.gameLoop = gameLoop;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	public Sprite getCurrentCursor() {
		return currentCursor;
	}

	public UI getUi() {
		return ui;
	}

	public void setUiv(UI ui) {
		this.ui = ui;
	}

/*	public ResourceDataBank getResourceDataBank() {
		return spriteLoader;
	}

	public void setResourceDataBank(ResourceDataBank spriteLoader) {
		this.spriteLoader = spriteLoader;
	}*/

	public void setUi(UI ui) {
		this.ui = ui;
	}

	/*public FrameMouseListener getFrameMouseListener() {
		return frameMouseListener;
	}

	public void setFrameMouseListener(FrameMouseListener frameMouseListener) {
		this.frameMouseListener = frameMouseListener;
	}*/

	public GameWindowManager getGameWindowManager() {
		return gameWindowManager;
	}

	public void setGameWindowManager(GameWindowManager gameWindowManager) {
		this.gameWindowManager = gameWindowManager;
	}

	public void setPlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}

	public Random getRandomGenerator() {
		return randomGenerator;
	}

	public void setRandomGenerator(Random randomGenerator) {
		this.randomGenerator = randomGenerator;
	}

	public GameArea getGameArea() {
		return gameArea;
	}

	public void setGameArea(GameArea gameArea) {
		this.gameArea = gameArea;
	}

	public Hotkeys getHotkeys() {
		return hotkeys;
	}

	public void setHotkeys(Hotkeys hotkeys) {
		this.hotkeys = hotkeys;
	}

	public EnemyDataBank getEnemyDataBank() {
		return enemyDataBank;
	}

	public void setEnemyDataBank(EnemyDataBank enemyDataBank) {
		this.enemyDataBank = enemyDataBank;
	}

	public ItemDataBank getItemDataBank() {
		return itemDataBank;
	}

	public AStarPathing getPathingThread() {
		return pathingThread;
	}

	public void setPathingThread(AStarPathing pathingThread) {
		this.pathingThread = pathingThread;
	}

	public ConsoleWindow getConsole() {
		return console;
	}

	public void setConsole(ConsoleWindow console) {
		this.console = console;
	}

	//Different method for this above
	/*public void setCurrentCursor(Sprite currentCursor) {
		this.currentCursor = currentCursor;
	}*/
}




/************* Osolete code ***********************
 * /**
 * Runs in every step the game takes
 * Should be called from inside a runnable
 *
	public void step()
	{
		for(Entity e : entityHash)
		{
			//remove all entities that are called for deletion
			if(e.getName() == "deleteMe")
			{
				entityHash.remove(e);
			}

			//check for any collisions with entities
			e.checkCollisions();

			//run any step events for the entities
			e.step();
		}

	}*
 *
 * Moved to it's own thread class
 */
