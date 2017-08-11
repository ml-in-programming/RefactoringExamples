package name_pending.Windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import name_pending.DataBanks.PlayerData;
import name_pending.Game;
import name_pending.Inventory;
import name_pending.Sprite;
import name_pending.Entities.Player;
import name_pending.Entities.Items.Item;

public class GameWindowEquipment extends GameWindow{
	ArrayList<GameWindowEquipmentSlot>	equipmentSlots = new ArrayList<GameWindowEquipmentSlot>();
	/**
	 * 
	 * @param theGame
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	GameWindowEquipment(Game theGame, int x, int y, int width, int height) {
		super(theGame, x, y, width, height, "EquipmentWindow", "playergui");
		//populateInventorySlots();
	}
	
	public void populateEquipmentSlots()
	{
		//Variables are oddly named because I stole this code from UI and then broke it into different sections
		//try{
		int row = 1;
		int vert = 1;
		int startx = getX() - 25;
		int starty = getY() - 15;
		int drawx = startx;
		int drawy = starty;
		Inventory inventory = Player.getPlayerData(theGame).getEquipment();
		Item item = null;
		for(int i=0; i<inventory.getMaxSize(); i++)
		{
			if(PlayerData.getItems(inventory).size() > i)
				item = PlayerData.getItems(inventory).get(i);
			else
				item = new Item();
			this.equipmentSlots.add(new GameWindowEquipmentSlot(this, drawx+(35*vert), drawy+(35*row), 32, 32, item, i+1));
			//check if at end of row
			vert++;
			if( (drawx+(35*vert+1) + 32) > this.getWidth() + this.getX() )
			{
				drawx = startx;
				row ++;
				vert = 1;
			}
		}
		//}catch(IndexOutOfBoundsException e){}
	}

	public void paintMe(Graphics g)
	{
		if(isVisiable())
		{
			super.paintMe(g);
			//Only paint this if the player exists
			Player player = theGame.getPlayer();
			if(player != null)
			{
				//Fill
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(getX(), getY(), getWidth(), getHeight());
				g.setColor(Color.CYAN);
				g.fillRect(getX(), getY(), 190, 15);

				//text
				g.setColor(Color.black);
				g.drawString("Equipment", this.getX() + 65, this.getY() + 12);

				//borders
				g.setColor(Color.BLACK);
				g.drawRect(getX(), getY(), getWidth(), getHeight());
				g.drawRect(getX(), getY(), 190, 15);

				//Draw the items
				//populateInventorySlots();
				try{
				for(GameWindowEquipmentSlot gwis : this.equipmentSlots)
				{
					gwis.paintMe(g);
				}
				}catch(ConcurrentModificationException e){}
			}
		}
	}
	
	public void toggleVisibility()
	{
		super.toggleVisibility();
		if(isVisiable())
			this.populateEquipmentSlots();
	}
	
	public void update()
	{
		super.update();
		this.populateEquipmentSlots();
	}
	
	public void mouseCheck(MouseEvent event, String eventType)
	{
		super.mouseCheck(event, eventType);
		//Check if our slots need it
		for(GameWindowEquipmentSlot gwis : this.equipmentSlots)
		{
			gwis.mouseCheck(event, eventType);
		}
	}

	/**
	 * Internal classes!
	 */
	
	public class GameWindowEquipmentSlot extends GameWindowSlot
	{
		int slotNumber = 0;
		
		//These will contain items so we need them to hold item objects
		Item item;
		//PopupListener popupListener = new PopupListener(this);
		
		GameWindowEquipmentSlot(GameWindow parentWindow, int x, int y,
				int width, int height, Item item, int slotNumber) {
			super(parentWindow, x, y, width, height);
			this.item = item;
			this.slotNumber = slotNumber;
			
			//create our menu
			getPopup().add(makeMenuItem("Unequip"));
		}
		
		public void paintMe(Graphics g)
		{
			g.setColor(Color.WHITE);
			g.fillRect(getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.RED);
			g.drawString(Integer.toString(slotNumber), getX()+16, getY()+16);
			Sprite s = null;
			try{
				if(item != null)
					s = item.getSprite();
				if(s != null)
				{
					s.setPosition(getX(), getY());
					s.paintOrig(g);
				}
			}catch(IndexOutOfBoundsException e){}
		}
		
		/**
		 * Menus we want are:
		 * 	Use
		 *  Equip
		 *  Move to slot x
		 *  Drop
		 * @return 
		 */
		public void rightClicked(MouseEvent event)
		{
			super.rightClicked(event);
			
			//popup.setBounds(mousePoint.x, mousePoint.y, popup.getWidth(), popup.getHeight());
			//popup.setVisible(true);
			
			//only paint if the window is visiable
			if(this.getParentWindow().isVisiable())
				getPopup().show(getParentWindow().getTheGame().getGameArea(), event.getPoint().x, event.getPoint().y);
			//getPopup().show(getParentWindow().getTheGame().getFrame(), getX(), getY());
			
			//this.parentWindow.getTheGame().getFrame().addMouseListener(popupListener);
		}
	}
}
