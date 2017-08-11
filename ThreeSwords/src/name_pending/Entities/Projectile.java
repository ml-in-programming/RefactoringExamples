package name_pending.Entities;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import name_pending.Game;
import name_pending.DataBanks.ResourceDataBank;

public class Projectile extends Entity{
	//If the bullet will hurt the player or enemies
	//private boolean friendly = false;
	private int damage = 0;
	
	private int lifeSpan = 1;
	
	private Entity source;
	
	/**
	 * 
	 * @param theGame
	 * @param x
	 * @param y
	 * @param speed
	 * @param damage
	 * @param lifeSpan
	 * @param friendly
	 * @param sprite
	 */
	public Projectile(Game theGame, Entity source, int x, int y, int speed, int damage, int lifeSpan, boolean friendly, String spriteName) {
		super(theGame, x, y, speed, "Projectile");
		//this.friendly = friendly;
		this.damage = damage;
		setSprite(ResourceDataBank.getSprite(spriteName));
		this.lifeSpan = lifeSpan;
		this.setFriendly(friendly);
		this.source = source;
		//getSprite().setRefPixel(getSprite().getWidth() / 2, getSprite().getWidth() / 2);
	}

	public void onCreate()
	{
		super.onCreate();
	}

	public void onDelete()
	{
		super.onDelete();
	}

	public boolean checkCollisions()
	{
		return super.checkCollisions();
	}

	public void paintMe(Graphics g)
	{
		super.paintMe(g);
	}

	public void step()
	{
		super.step();
		setLifeSpan(getLifeSpan() - 1);
		if(getLifeSpan() <= 0)
			this.onDelete();
	}

	public void keyCheck(int keyCode,boolean pressed)
	{
		super.keyCheck(keyCode, pressed);
	}

	public void mouseCheck(MouseEvent event,String eventType)
	{
		super.mouseCheck(event, eventType);
	}
	
	
	
	
	
	
	/*public boolean isFriendly() {
		return friendly;
	}

	public void setFriendly(boolean friendly) {
		this.friendly = friendly;
	}*/

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void setLifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	public Entity getSource() {
		return source;
	}

	public void setSource(Entity source) {
		this.source = source;
	}
	
}
