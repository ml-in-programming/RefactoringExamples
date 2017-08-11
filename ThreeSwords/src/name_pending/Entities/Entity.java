package name_pending.Entities;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

import name_pending.Game;
import name_pending.Sprite;
import name_pending.DataBanks.ResourceDataBank;
import name_pending.Pathing.Pathable;

/****
 * 
 * @author Hawox
 *
 * All entities in the game should have this as a parent 
 * 	-so they can have their internal events processed
 * 
 * Here to copy paste important methods to other subclasses


public void onCreate()
{

}

public void onDelete()
{

}

public void checkCollisions()
{

}

public void paintMe()
{

}

public void step()
{

}

public void keyCheck(int keyCode,boolean pressed)
{

}

public void mouseCheck(MouseEvent event,String eventType)
	{

	}


 */
public abstract class Entity extends Pathable implements Cloneable{

	//Basic variables all entity should have
	private int x = 0;
	private int y = 0;

	//The distance they will move in the next step
	private int dx = 0;
	private int dy = 0;
	
	private int reflectingTime = 0;

	//Where they were related to sprite drawn to the screen last frame
	private int lastViewX = 0;
	private int lastViewY = 0;
	
	//If it's an entity in the world it should have a sprite
	private Sprite sprite = null;
	
	//How fast the entity moves, but also used in calculation crit/dodge
	private int speed = 0; 
	
	//true if things can not pass threw them
	private boolean solid = false;
	private boolean moveThrewSolids = false;
	
	//Gets input from HID devices
	private EntityHIDListener entityHIDListener;
	
	//Direction the entity is facing
	private String direction = "right";
	
	//If they are on the players side or not
	private boolean friendly;

	/*
	 * I didn't want to add this before, now I am trying to remember why.
	 * Oh well, lets see how it ends up
	 */
	private Game theGame = null;

	//Defaults to delete me so if a blank entity exists it will be removed from the entity hash
	private String name = "deleteMe"; //Name before list edit
	private String nameInList = "deleteMe"; //this name will have a number added to the end so entities in the list don't all have the same name
	
	Point reletivePosition = new Point(x,y);
	
	//The cost of going threw this object in the AStar pathing script | Negitive = unpassable
	double pathingCost = 0.0;
	
	//Need this with all the different moving methods
	//Used mainly to keep the speed at which we are to follow the pathing one
	private int currentMovingSpeed = 0;

	/**
	 * 
	 * @param theGame
	 * @param x
	 * @param y
	 * @param SpeedString
	 */
	protected Entity(Game theGame, int x, int y, int Speed, String name)
	{
		super(theGame.getPathingThread());
		this.theGame = theGame;
		this.setX(x);
		this.setY(y);
		this.setSpeed(speed);
		//Lets see if changing their names helps with java.util.ConcurrentModificationException
		int number = 0;
		for(Entity e : theGame.getEntityHash())
			if(e.getName() == name.split("_")[0])
				number++;
		//Name in the entity
		this.name = name;
		//Name in the list
		this.nameInList = (name + "_" + Integer.toString(number));
		onCreate();
		//All entities should set their sprites in the onCreate, add this here to give each entity it's own sprite object
		//setSprite(getSprite().clone()); //Not needed?
		//Start the sprites animation
		this.sprite.setAnimation(1);
		
		//add listener
		entityHIDListener = new EntityHIDListener(this);
		getTheGame().getFrame().addKeyListener(entityHIDListener);
		getTheGame().getGameArea().addMouseListener(entityHIDListener);
	}

	/*
         * Collision detection between the current sprite and another sprite
         */
	// TODO move to Sprite
    public static boolean collidesWith(Sprite sprite, Sprite otherSprite, boolean pixelPerfect) {
        boolean isColliding=false;

        Rectangle r1 = Sprite.getBounds(sprite);
        Rectangle r2 = Sprite.getBounds(otherSprite);

        if(r1.intersects(r2)) {
            if(pixelPerfect) {
                isColliding = sprite.pixelPerfectCollision(otherSprite, r1, r2);
            }else {
                isColliding = true;
            }
        }

        return isColliding;
    }

	//The script that is run for the entity upon it's creation
	public void onCreate()
	{
		//Setup the sprite
		//setSprite(theGame.getSpriteLoader().getSprite("Error.png"));
		//if(theGame.getResourceLoader().getImages().containsKey("Error.png"))
		setSprite(ResourceDataBank.getSprite("Error.png"));
	}

	//Objects can't really be deleted, so this is ran when the object is removed form the entity list
	public void onDelete()
	{
		//I don't want to save a reference to theGame in each object
		//So we are renaming them "deleteMe" and having theGame remove them from it's list
		this.setName("deleteMe");
		theGame.getEntityHash().remove(this);
	}

	//Collision processing
	/**
	 * @return false if it should not move to the next space
	 */
	public boolean checkCollisions()
	{
		//see if you are hit by a projectile
		HashSet<Entity> collision = this.checkForCollision();
		if(collision != null)
		{
			for(Entity e : collision)
			{
				if(e instanceof Projectile)
				{
					Projectile p = (Projectile) e;
					//Take damage and delete projectile

					//friendly entities don't take damage from friendly projectiles
					if(this.friendly != e.friendly)
					{
						//if being take damage, otherwise do nothing but block the projectile for now
						if(this instanceof Being)
						{
							Being being = (Being) this;
							being.takeDamage(p.getDamage(), null);
							
							//if you take damage from an opposite team and are an npc, aggro onto what shot at you
							if(this instanceof Enemy)
							{
								Enemy enemy = (Enemy) being;
								enemy.setAggroed(true);
								enemy.setAggroedTo(p.getSource());
							}
						}
						
						//delete projectile
						e.onDelete();
					}
				}
			}
		}
		
		//don't move if you collide with a solid
		if(this.moveThrewSolids == false)
		{
			try{
				for(Entity e : this.checkForCollision())
				{
					if(e.isSolid())
					{
						//undo move
						return false;
					}
				}
			}catch(NullPointerException e){}
		}
		return true;
	}

	//Used to draw whatever is related to the entity to the main game panel
	public void paintMe(Graphics g)
	{
		//move it's x to the orintation of the room view
		Point origin = getTheGame().getGameArea().getOriginPoint();

		int newX = this.getX() - origin.x;
		int newY = this.getY() - origin.y;
		
		newX -= (getSprite().getWidth() /2);
		newY -= (getSprite().getHeight() /2);
		getSprite().setPosition(newX, newY);
		
		setLastViewX(newX);
		setLastViewY(newY);


		this.sprite.paint(g);
		this.sprite.continueAnimation();
		if(theGame.isDEBUG())
		{
			g.setColor(Color.RED);
			g.drawString(getNameInList(), getSprite().getX(), getSprite().getY()-10);
			g.drawString("X: " + Integer.toString(getX()) + "  Y: " + Integer.toString(getY()), getSprite().getX() - 20, getSprite().getY()-20);
			g.drawRect(getSprite().getX(), getSprite().getY(), getSprite().getWidth(), getSprite().getHeight());
			
			//Draw it's path points
			if(pathPoints.size() > 0)
			{
				Point lastPoint = pathPoints.get(0);
				for(Point p : pathPoints)
				{
					g.setColor(Color.pink);
					g.drawOval(p.x - origin.x, p.y - origin.y, 10, 10);
					g.drawLine(p.x - origin.x, p.y - origin.y, lastPoint.x - origin.x, lastPoint.y - origin.y);
					lastPoint = p;
				}
			}
		}
	}

	//Is checked every frame of the game
	public void step()
	{
		//see if the entity is moving and move it
		if( (dx != 0) || (dy != 0) )
			this.moveMe();
		
		//If the entity is out of the bounds of the Room then delete it UNLESS IT IS THE PLAYER
		if(!(this instanceof Player))
		{
			if(   (this.getX() > getTheGame().getGameArea().getCurrentRoom().getWidth()) || (this.getX() < 0 )  )
			{
				//Out of x bounds
				this.onDelete();
			}
			if(   (this.getY() > getTheGame().getGameArea().getCurrentRoom().getHeight()) || (this.getY() < 0 )  )
			{
				//out of y bounds
				this.onDelete();
			}
		}
		
		//reduce reflect time
		setReflectingTime(getReflectingTime() - 1);
		
		//See if we should change our move threw cost
		if(this.isSolid())
		{
			this.pathingCost = -1.0;
		}
	}
	
	public HashSet<Entity> checkForCollision()
	{
		try{
			HashSet<Entity> returns = new HashSet<Entity>();
			for(Entity e : theGame.getEntityHash())
			{
				//We don't want to add itself to the list
				if(e == this)
					continue;
				//See if they have collided
				if(collidesWith(this.getSprite(), e.getSprite(), true))
				//if(Sprite.collidesWith(this.getSprite(), e.getSprite(), false));
				{
					returns.add(e);
				}
			}
			//To make it easier for other methods to see if there was a collision or not, we will return null on no collision
			if(returns.isEmpty())
				return null;
			else
				return returns;
		}catch(NullPointerException e) { } //should mean theres an empty list and nothing should happen
		return null;
	}
	
	
	
	/**
	 * Overloading this method to check for specific collisions
	 */
	public HashSet<Entity> checkForCollision(String name)
	{
		//Since we are checking for an exact collision lets see if one even exists
		try{
			HashSet<Entity> returns = new HashSet<Entity>();
			for(Entity e : theGame.getEntityHash())
			{
				//Check for the right name
				if(e.getName() == name)
				{
					//We don't want to add itself to the list
					if(e == this)
						continue;
					//See if they have collided
					if(collidesWith(this.getSprite(), e.getSprite(), true))
					{
						returns.add(e);
					}
				}
			}
			//To make it easier for other methods to see if there was a collision or not, we will return null on no collision
			if(returns.isEmpty())
				return null;
			else
				return returns;
		}catch(NullPointerException e) { } //should mean theres an empty list and nothing should happen
		return null;
	}

	//move the entity based on it's direction
	public void moveMe()
	{
		//If we have a path we want to adjust our speed to follow it, otherwise keep on our merry way
		if(this.pathPoints.size() > 0)
		{
			this.adjustDestinationToPath();
		}
		
		//We want to check for collisions every 1 pixel movement to be more accurate with collision checking
		//positive movement
		if(dx > 0)
		{
			for(int i =0; i < dx; i++)
			{
				x += 1;
				if(this.checkCollisions() == false)
					x-=2;
			}
		}else //Negative movement
		{
			for(int i = 0; i < (dx* -1); i++)
			{
				x -= 1;
				if(this.checkCollisions() == false)
					x+=2;
			}
		}
		
		//positive movement
		if(dy > 0)
		{
			for(int i =0; i < dy; i++)
			{
				y += 1;
				if(this.checkCollisions() == false)
					y-=2;
			}
		}else //Negative movement
		{
			for(int i = 0; i < (dy* -1); i++)
			{
				y -= 1;
				if(this.checkCollisions() == false)
					y+=2;
			}
		}
	}

	//Override to allow the entity to be moved to an exact x y
	public void moveMe(int x, int y)
	{
		setX(x);
		setY(y);
	}
	
	/**
	 * Don't set a path if we are currently in line for a path
	 * Instead just start walking towards the location without a path
	 * @param point
	 * @param speed
	 */
	public void setDestination(Point point, int speed)
	{
		this.currentMovingSpeed = speed;
		if(this.isWaitingOnPath() == false)
		{
			this.needPath(new Point(this.x, this.y), point);
		}else
		{
			moveToPoint(point, speed);
		}
	}
	
	/**
	 * Taken out of setDestination after pathing was added
	 */
	public void moveToPoint(Point point, int speed)
	{
		//Only set your destination if you are not reflecting
		if(getReflectingTime() < 1)
		{
			//I almost had it, oh well. Code changes thanks to: http://stackoverflow.com/questions/7448729/moving-an-object-from-point-to-point-in-a-linear-path

			int targetX = point.x - this.getX();
			int targetY = point.y - this.getY();

			//pythagorean theorem
			double distance = Math.sqrt(targetX * targetX + targetY * targetY);

			//movement
			setDx( (int) ((targetX / distance) * speed));
			setDy( (int) ((targetY / distance) * speed));
		}
	}

	//Make sure that the entity is following the path along to each node
	private void adjustDestinationToPath()
	{
		Point target = this.getPathPoints().get(0);
		moveToPoint(target, this.currentMovingSpeed);
	}
	
	public void reflectPerfect(int numberOfFrames, int overTime)
	{
		//If there is no time, then move it that far instantly
		if(overTime == 0)
		{
			//setDx((getDx() * numberOfFrames) * -1);
			//setDy((getDx() * numberOfFrames) * -1);
			moveMe( getX() + ((getDx() * numberOfFrames) * -1), getY() + ((getDy() * numberOfFrames) * -1) );
		}
		//else move it that far over that many frames
		else {
			setReflectingTime(overTime);
			setDx(getDx() * -1);
			setDy(getDy() * -1);
		}
		
		//update their position now
		//moveMe();
		
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
	 * Will be triggered by the FrameMouseListener event
	 * @param event Event that was passed from the MouseListener
	 * @param eventType Compatable Strings "Clicked", "Pressed", and "Released.
	 */
	public void mouseCheck(MouseEvent event,String eventType)
	{

	}
	
	class EntityHIDListener implements KeyListener, MouseListener
	{
		Entity parent;
		
		EntityHIDListener(Entity entity)
		{
			this.parent = entity;
		}

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
		
		
		/*
		private void sendMouseEvent(MouseEvent event, String eventType)
		{
			for (Entity e: theGame.getEntityHash())
			{
				e.mouseCheck(event, eventType);
			}
		}
		
		private void sendKeyEvent(int keyCode, boolean pressed)
		{
			for (Entity e: theGame.getEntityHash())
			{
				e.keyCheck(keyCode, pressed);
			}
		}*/
		
		private void sendMouseEvent(MouseEvent event, String eventType)
		{
			parent.mouseCheck(event, eventType);
		}
		
		private void sendKeyEvent(int keyCode, boolean pressed)
		{
				parent.keyCheck(keyCode, pressed);
		}
		
		
		//Not currently used
		public void mouseEntered(MouseEvent e) {}

		public void mouseExited(MouseEvent e) {}

		public void keyTyped(KeyEvent e) {}
	}

	
	public Entity clone()
	{
		try {
            return (Entity) super.clone();
        }catch (CloneNotSupportedException e) {
            System.out.println("Clone failed.");
            return null;
        }
	}

	/**
	 * Getters and setters
	 */

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public Game getTheGame() {
		return theGame;
	}

	public void setTheGame(Game theGame) {
		this.theGame = theGame;
	}

	public String getNameInList() {
		return nameInList;
	}

	public void setNameInList(String nameInList) {
		this.nameInList = nameInList;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Point getReletivePosition() {
		return reletivePosition;
	}

	public void setReletivePosition(Point reletivePosition) {
		this.reletivePosition = reletivePosition;
	}

	public int getLastViewX() {
		return lastViewX;
	}

	public void setLastViewX(int lastViewX) {
		this.lastViewX = lastViewX;
	}

	public int getLastViewY() {
		return lastViewY;
	}

	public void setLastViewY(int lastViewY) {
		this.lastViewY = lastViewY;
	}

	public boolean isSolid() {
		return solid;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	public boolean isMoveThrewSolids() {
		return moveThrewSolids;
	}

	public void setMoveThrewSolids(boolean moveThrewSolids) {
		this.moveThrewSolids = moveThrewSolids;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public EntityHIDListener getEntityHIDListener() {
		return entityHIDListener;
	}

	public void setEntityHIDListener(EntityHIDListener entityHIDListener) {
		this.entityHIDListener = entityHIDListener;
	}

	public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}

	public int getReflectingTime() {
		return reflectingTime;
	}

	public void setReflectingTime(int reflectingTime) {
		this.reflectingTime = reflectingTime;
	}

	public double getPathingCost() {
		return pathingCost;
	}

	public void setPathingCost(double pathingCost) {
		this.pathingCost = pathingCost;
	}


}
