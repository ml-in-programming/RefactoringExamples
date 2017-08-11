package name_pending.Entities;

import java.awt.Graphics;
import java.awt.Point;

import name_pending.Game;
import name_pending.Sprite;
import name_pending.DataBanks.ResourceDataBank;

public class Tile{
	
	Game theGame;
	int x = 0;
	int y = 0;
	
	Sprite sprite;

	public Tile(Game theGame, String spriteName) {
		this.theGame = theGame;
		this.sprite = ResourceDataBank.getSprite(spriteName);
	}

	public void paintMeAt(int xi, int yi, Graphics g) {
		this.x = (xi);
		this.y = (yi);		
		
			//move it's x to the orintation of the room view
			Point origin = theGame.getGameArea().getOriginPoint();

			int newX = x - origin.x;
			int newY = y - origin.y;
			
			newX -= (sprite.getWidth() /2);
			newY -= (sprite.getHeight() /2);
			sprite.setPosition(newX, newY);

			this.sprite.paint(g);
			this.sprite.continueAnimation();
	}

	public Sprite getSprite() {
		return this.sprite;
	}
	
}
