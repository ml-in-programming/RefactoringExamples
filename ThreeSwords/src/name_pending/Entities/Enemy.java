package name_pending.Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;

import name_pending.Game;
import name_pending.Resistance;
import name_pending.Sprite;
import name_pending.DataBanks.ResourceDataBank;


public class Enemy extends Being{

	//aggro variables
	private boolean aggroed;
	private Entity aggroedTo;
	private Rectangle lineOfSight; //used in collision check to see if it will aggro to something
	private int lineOfSightWidth = 300;
	private int lineOfSightHeight = 150;
	private ArrayList<String> aggroTo = new ArrayList<String>();
	
	private String spriteName = "Error.png";
	
	//movement variables
	private boolean wander = true;
	private Point moveDestination = new Point(this.getX(), this.getY());

	/**
	 * 
	 * @param theGame
	 * @param x
	 * @param y
	 * @param speed
	 * @param name
	 * @param health
	 * @param defence
	 * @param attack
	 * @param dexterity
	 * @param resist
	 */
	public Enemy(Game theGame, int x, int y, int speed, String name, String spriteName, int health,
			int defence, int attack, int dexterity, Resistance[] resist) {
		super(theGame, x, y, speed, name, health, defence, attack, dexterity, resist);
		this.spriteName = spriteName;
		this.setSprite(ResourceDataBank.getSprite(spriteName));
		setAggroed(false);
		setAggroedTo(null);
		
		aggroTo.add("Player");
		
		//change this to account for it's current direction
		lineOfSight = new Rectangle (this.getX(), this.getY() - (lineOfSightHeight / 2), lineOfSightWidth, lineOfSightHeight);
	}
	
	public void onCreate()
	{
		super.onCreate();
	}
	
	public void paintMe(Graphics g)
	{
		super.paintMe(g);
		//draw our aggro bounds
		g.setColor(new Color(225, 225, 0, 150));
		g.drawRect(lineOfSight.x - getTheGame().getGameArea().getOriginPoint().x, lineOfSight.y - getTheGame().getGameArea().getOriginPoint().y, lineOfSight.width, lineOfSight.height);
		
		//draw aggro
		if(this.isAggroed())
		{
			Sprite aggro = ResourceDataBank.getSprite("Aggroed.png");
			aggro.setPosition(this.getX() - getTheGame().getGameArea().getOriginPoint().x - 16, this.getY() - getTheGame().getGameArea().getOriginPoint().y - 64);
			aggro.paint(g);
		}
		
		g.setColor(Color.cyan);
		g.drawString(Integer.toString(this.getHealth()), this.getX() - getTheGame().getGameArea().getOriginPoint().x - 50, this.getY() - getTheGame().getGameArea().getOriginPoint().y - 50);
	}

	public void step()
	{
		super.step();
		//if wandering have a chance of picking a new close move destination
		if(this.isWander())
		{
			//chance of moving out of 1000
			int moveChance = 50;
			int moveChanceRoll = getTheGame().getRandomGenerator().nextInt(1000) + 1;
			
			if(moveChanceRoll < moveChance)
			{
				//move somewhere within 100 of itself
				this.moveDestination = new Point( (this.getX() + getTheGame().getRandomGenerator().nextInt(200) - 100), (this.getY() + getTheGame().getRandomGenerator().nextInt(200) - 100) );
				this.setDestination(moveDestination, this.getSpeed());
			}
		}
		
		//stop moving if you reach your target
		if( (this.getDx() != 0) || (this.getDy() != 0) )
		{
			//check if it's speed is in the wrong direction to it's target
			if( (getDx() > 0 && getX() > getMoveDestination().x) || (getDx() < 0 && getX() < getMoveDestination().x) )
			{
				if( (getDy() > 0 && getY() > getMoveDestination().y) || (getDy() < 0 && getY() < getMoveDestination().y) )
				{
					//Heading in the wrong direction of it's target. End it's movement
					setDy(0);
					setDx(0);
				}
			}
		}
		//update your direction
		//Gets dx and dy, convert them to positive and see which one is bigger, then see if it is positive or negative for it's direction.
		int dxPositive = Math.abs(getDx());
		int dyPositive = Math.abs(getDy());
		
		//left / right dominate
		if(dxPositive >= dyPositive)
		{
			if(getDx() > 0)
				this.setDirection("right");
			else
				this.setDirection("left");

		}
		else //up / down dominate
		{
			if(getDy() > 0)
				this.setDirection("down");
			else
				this.setDirection("up");
		}

		//check if the player was aggroed
		//get the rectangles reletive to the screen
		Rectangle thisRectangle = lineOfSight;
		Rectangle otherRectangle;
		for(Entity e :getTheGame().getEntityHash()) {
			if(this.getAggroTo().contains(e.getName()))
			{
				e.getSprite();
				//add the origin so that the sprite in based in the game area as is the aggro rectangle
				otherRectangle = Sprite.getBounds(e.getSprite());
				otherRectangle.x += getTheGame().getGameArea().getOriginPoint().x;
				otherRectangle.y += getTheGame().getGameArea().getOriginPoint().y;
				if( (thisRectangle.intersects(otherRectangle)) || (thisRectangle.contains(otherRectangle)))
				{
					this.setAggroed(true);
					this.setAggroedTo(e);
					this.setWander(false);
					break;
				}
			}
		}
		
		//if aggroed follow aggoed entity
		if(this.isAggroed())
		{
			if(this.aggroedTo != null)
			{
				this.setDestination(new Point(getAggroedTo().getX(),getAggroedTo().getY()), this.getSpeed() );
			}//aggro nolonger exists
			else
			{
				this.setAggroed(false);
				this.setWander(true);
			}
		}
	}
	
	public void moveMe()
	{
		super.moveMe();
		//move your aggro box with you
		int width = (int) this.getLineOfSightWidth();
		int height = (int) this.getLineOfSightHeight();
		
		//this.setDirection("down");
		//morph this based on direction faced
		switch(this.getDirection())
		{
		case "right":
				lineOfSight = new Rectangle (this.getX(), this.getY() - (height / 2), width, height);
				break;
		case "left":
				lineOfSight = new Rectangle (this.getX() - width, this.getY() - (height / 2), width, height);
				break;
		case "up":
				lineOfSight = new Rectangle (this.getX() - (height / 2), this.getY() - width, height, width);
				break;
		case "down":
				lineOfSight = new Rectangle (this.getX() - (height / 2), this.getY(), height, width);
				break;
		}
	}
	
	public boolean checkCollisions()
	{
		//up here in case code needs to run first
		boolean returnMe = super.checkCollisions();

		//IF you hit player or non-friendly projectile, then reflect back
		HashSet<Entity> collision = this.checkForCollision();
		if(collision != null)
		{
			for(Entity e : collision)
			{
				//player
				if(e.getName() == "Player")
				{
					Player p = (Player) e;
					//damage player and then refelct
					p.takeDamage(getAttack(), null);
					this.reflectPerfect(1, 0);
					returnMe = false;
				}
				//projectile
				if(e.getName() == "Projectile")
				{
					Projectile p = (Projectile) e;
					
					this.takeDamage(p.getDamage(), null);
					this.reflectPerfect(2, 0);
					if(this.aggroed == false)
					{
						this.aggroed = true;
						this.aggroedTo = p.getSource();
					}
					p.onDelete();
					returnMe = false;
				}
			}
		}

		
		
		
		return returnMe;
	}
	
	
	
	
	public boolean isAggroed() {
		return aggroed;
	}

	public void setAggroed(boolean arrgoed) {
		this.aggroed = arrgoed;
	}

	public Entity getAggroedTo() {
		return aggroedTo;
	}

	public void setAggroedTo(Entity aggroedTo) {
		this.aggroedTo = aggroedTo;
	}

	public Rectangle getLineOfSight() {
		return lineOfSight;
	}

	public void setLineOfSight(Rectangle lineOfSight) {
		this.lineOfSight = lineOfSight;
	}

	public boolean isWander() {
		return wander;
	}

	public void setWander(boolean wander) {
		this.wander = wander;
	}

	public Point getMoveDestination() {
		return moveDestination;
	}

	public void setMoveDestination(Point moveDestination) {
		this.moveDestination = moveDestination;
	}

	public int getLineOfSightWidth() {
		return lineOfSightWidth;
	}

	public void setLineOfSightWidth(int lineOfSightWidth) {
		this.lineOfSightWidth = lineOfSightWidth;
	}

	public int getLineOfSightHeight() {
		return lineOfSightHeight;
	}

	public void setLineOfSightHeight(int lineOfSightHeight) {
		this.lineOfSightHeight = lineOfSightHeight;
	}

	public ArrayList<String> getAggroTo() {
		return aggroTo;
	}

	public void setAggroTo(ArrayList<String> aggroTo) {
		this.aggroTo = aggroTo;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public void setSpriteName(String spriteName) {
		this.spriteName = spriteName;
	}


}
