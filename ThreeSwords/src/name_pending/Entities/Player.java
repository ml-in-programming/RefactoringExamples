package name_pending.Entities;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;

import name_pending.Game;
import name_pending.Sprite;
import name_pending.DataBanks.ResourceDataBank;
import name_pending.Entities.Items.ItemDrop;
import name_pending.Entities.Items.ItemMelee;
import name_pending.Entities.Items.ItemRanged;

public class Player extends Being{
	
	private boolean shootingRanged;
	private boolean shootingMelee;
	
	private int mana;
	private int stamina;
	
	private Point mousePoint;
	
	PlayerMouseMotionListener playerMouseMotionListener;

	@SuppressWarnings("static-access")
	public Player(Game theGame, int x, int y)
	{
		super(theGame, x, y, theGame.getPlayerData().getSTARTING_SPEED(), theGame.getPlayerData().getSTARTING_NAME(), theGame.getPlayerData().getMaxHealth(), theGame.getPlayerData().getSTARTING_DEFENCE(), theGame.getPlayerData().getSTARTING_ATTACK(), theGame.getPlayerData().getSTARTING_DEXTERITY(), theGame.getPlayerData().getSTARTING_RESISTANCE());
		this.setFriendly(true);
	}

	/*
	 * Entity methods
	 */

	public void onCreate()
	{
		super.onCreate();
		setSprite(ResourceDataBank.getSprite("Player.png"));
		playerMouseMotionListener = new PlayerMouseMotionListener();
		getTheGame().getGameArea().addMouseMotionListener(playerMouseMotionListener);
		Game.addText(getTheGame().getConsole(), "Player Created.");
	}

	public void onDelete()
	{
		Game.addText(getTheGame().getConsole(), "Player deleted.");
		super.onDelete();
	}

	public boolean checkCollisions()
	{
		boolean returnMe = super.checkCollisions(); //uphere incase it has other code that needs to run before this stuff
		
		//collides with item on the ground
		HashSet<Entity> collision = this.checkForCollision("ItemDrop");
		if(collision != null)
		{
			for(Entity e : collision)
			{
				//Pickup items on the ground and remove the item drop

				if(e.getName() == "ItemDrop")
				{
					ItemDrop id = (ItemDrop) e;
					//see if we have room in our inventory
					if(!(getTheGame().getPlayerData().getInventory().isFull()))
					{
						//Delete drop and add it's item to inventory < reverse that
						this.getTheGame().getPlayerData().getInventory().addItem(id.getItem());
						id.onDelete();

						//Update our inventory with the new item
						this.getTheGame().getGameWindowManager().updateWindows("InventoryWindow");
						break;
					}
				}
			}
		}
		return returnMe;
	}

	public void paintMe(Graphics g)
	{
		//Draw this before the player so it is drawn behind it
		//if equip then draw the item equip
		Sprite weaponSprite = null;
		ItemRanged rangedWeapon = (ItemRanged) this.getTheGame().getPlayerData().getRangedWeapon();
		if(rangedWeapon != null)
		{
			weaponSprite = rangedWeapon.getSprite().clone();
		}
		if(weaponSprite != null)
		{
			weaponSprite.setPosition(getSprite().getX() - 8, getSprite().getY() -8);
			weaponSprite.paint(g);
		}

		super.paintMe(g);
		
		
		ItemMelee meleeWeapon = (ItemMelee) this.getTheGame().getPlayerData().getMeleeWeapon();
		weaponSprite = null;
		if(meleeWeapon != null)
		{
			weaponSprite = meleeWeapon.getSprite().clone();
		}
		if(weaponSprite != null)
		{
			weaponSprite.setPosition(getSprite().getX() + 5, getSprite().getY() + 5);
			weaponSprite.paint(g);
		}

	}

	public void step()
	{
		super.step();
		
		if(isShootingRanged() || isShootingMelee())
		{
			if(isCanShoot())
			{
				if(this.getAttackDelay() == 0)
				{
					if(isShootingMelee())
					{
						ItemMelee meleeWeapon = (ItemMelee) getTheGame().getPlayerData().getMeleeWeapon();
						int newX = getTheGame().getGameArea().getOriginPoint().x + mousePoint.x;
						int newY = getTheGame().getGameArea().getOriginPoint().y + mousePoint.y;
						
						//newX = ((int) ((mousePoint.x - newX) / 10));
						//newY = ((int) ((mousePoint.y - newY) / 10)); //10 is how many frames it will take to get to that location
						
						meleeWeapon.fireProjectile(new Point(newX, newY), 5, "Slash.png", true, Player.this);
						setAttackDelay(8); //one second
					}
					if(isShootingRanged())
					{
						ItemRanged rangedWeapon = (ItemRanged) getTheGame().getPlayerData().getRangedWeapon();
						int newX = getTheGame().getGameArea().getOriginPoint().x + mousePoint.x;
						int newY = getTheGame().getGameArea().getOriginPoint().y + mousePoint.y;
						rangedWeapon.fireProjectile(new Point(newX, newY), 66, "Arrow.png", true, Player.this);
						setAttackDelay(15); //one second
					}
				}
			}
		}
		
		//Decress attack timer
		if(getAttackDelay() > 0)
			setAttackDelay(getAttackDelay() - 1);
		
		if(getTheGame().isDEBUG())
			getTheGame().getPlayerData().addExp(1);
	}

	public void keyCheck(int keyCode,boolean pressed)
	{
		super.keyCheck(keyCode, pressed);
		//Key pressed down
		if(pressed == true)
		{
			String horizMovement = "none";
			String vertMovement = "none";
			if(keyCode == getTheGame().getHotkeys().getHOTKEY_moveUp())
			{
				//this.setDy(this.getSpeed() * -1); // move up
				vertMovement = "up";
			}
			if(keyCode == getTheGame().getHotkeys().getHOTKEY_moveDown())
			{
				//this.setDy(this.getSpeed()); //move down
				vertMovement = "down";
			}
			if(keyCode == getTheGame().getHotkeys().getHOTKEY_moveLeft())
			{
				//this.setDx(this.getSpeed() * -1); //move left
				horizMovement = "left";
			}
			if(keyCode == getTheGame().getHotkeys().getHOTKEY_moveRight()) //move right
			{
				//this.setDx(this.getSpeed());
				horizMovement = "right";
			}
			//Set the destination to the right direction
			switch(vertMovement)
			{
			case "up":
				switch(horizMovement)
				{
				case "left":
					this.setDestination(new Point(getX() - getSpeed(), getY() - getSpeed()), getSpeed());
					break;
				case "right":
					this.setDestination(new Point(getX() + getSpeed(), getY() - getSpeed()), getSpeed());
					break;
				default:
					//just moveing up
					this.setDestination(new Point(getX(), getY() - getSpeed()), getSpeed());
					break;
				}
				break;
				
			case "down":
				switch(horizMovement)
				{
				case "left":
					this.setDestination(new Point(getX() - getSpeed(), getY() + getSpeed()), getSpeed());
					break;
				case "right":
					this.setDestination(new Point(getX() + getSpeed(), getY() + getSpeed()), getSpeed());
					break;
				default:
					//just moveing down
					this.setDestination(new Point(getX(), getY() + getSpeed()), getSpeed());
					break;
				}
				break;
			
			default:
				//no vertical movement
				switch(horizMovement)
				{
				case "left":
					this.setDestination(new Point(getX() - getSpeed(), getY()), getSpeed());
					break;
				case "right":
					this.setDestination(new Point(getX() + getSpeed(), getY()), getSpeed());
					break;
				default:
					//no movement
					break;
				}
			}
			
			
		}else{ // Key released
			if( (keyCode == getTheGame().getHotkeys().getHOTKEY_moveUp()) || (keyCode == getTheGame().getHotkeys().getHOTKEY_moveDown()) )
			{
				this.setDy(0);
			}
			if( (keyCode == getTheGame().getHotkeys().getHOTKEY_moveLeft()) || (keyCode == getTheGame().getHotkeys().getHOTKEY_moveRight()) )
			{
				this.setDx(0);
			}
		}
	}
	
	public void mouseCheck(MouseEvent event,String eventType)
	{
		//check if we need to fire a ranged weapon
		if(event.getButton() == MouseEvent.BUTTON3)
		{
			if(eventType == "pressed")
			{
				ItemRanged rangedWeapon = (ItemRanged) getTheGame().getPlayerData().getRangedWeapon();
				if(rangedWeapon != null)
				{
					setShootingRanged(true);
				}
			}else
			if(eventType == "released")
				setShootingRanged(false);
		}
		
		//check if we need to fire a ranged weapon
		if(event.getButton() == MouseEvent.BUTTON1)
		{
			if(eventType == "pressed")
			{
				ItemMelee meleeWeapon = (ItemMelee) getTheGame().getPlayerData().getMeleeWeapon();
				if(meleeWeapon != null)
				{
					setShootingMelee(true);
				}
			}else
				if(eventType == "released")
					setShootingMelee(false);
		}
	}
	
	//used to get the position of the mouse
	class PlayerMouseMotionListener implements MouseMotionListener
	{

		@Override
		public void mouseDragged(MouseEvent event) {
			setMousePoint(event.getPoint());
		}

		@Override
		public void mouseMoved(MouseEvent event) {
			setMousePoint(event.getPoint());
		}
		
	}
	
	

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getStamina() {
		return stamina;
	}

	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	public boolean isShootingRanged() {
		return shootingRanged;
	}

	public void setShootingRanged(boolean shootingRanged) {
		this.shootingRanged = shootingRanged;
	}

	public Point getMousePoint() {
		return mousePoint;
	}

	public void setMousePoint(Point mousePoint) {
		this.mousePoint = mousePoint;
	}

	public boolean isShootingMelee() {
		return shootingMelee;
	}

	public void setShootingMelee(boolean setShootingMelee) {
		this.shootingMelee = setShootingMelee;
	}

	public PlayerMouseMotionListener getPlayerMouseMotionListener() {
		return playerMouseMotionListener;
	}

	public void setPlayerMouseMotionListener(
			PlayerMouseMotionListener playerMouseMotionListener) {
		this.playerMouseMotionListener = playerMouseMotionListener;
	}

}