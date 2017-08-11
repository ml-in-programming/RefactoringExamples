package name_pending.Windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * The use will vary with these. This should just rest as a parent for other slot classes
 * @author Hawox
 *
 */
public class GameWindowSlot implements ActionListener{
	//All should stem from window classes. Not sure if we will ever need them though
	private GameWindow parentWindow;
	private int x;
	private int y;
	private int width;
	private int height;
	
	//All slots should have something happebn when you right click them
	//Needed to get our popup working
	private JPopupMenu popup = new JPopupMenu();
	//private JMenuItem menuItem;
	
	GameWindowSlot(GameWindow parentWindow, int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parentWindow = parentWindow;
	}
	
	public void mouseCheck(MouseEvent event,String eventType)
	{
		//If the mouse is right clicked on these a list window popup should appear
		//Might work with other ways of doing this later
		
		
		//Right clicked
		if(event.getButton() == MouseEvent.BUTTON3)
		{
			//See if it was within out borders
			int mousex = event.getPoint().x;
			int mousey = event.getPoint().y;
			//           check x                                      check y
			if(  (mousex > x) && (mousex < (width+x))  &&  (mousey > y) && (mousey < (y+height))  )
			{
				rightClicked(event);
			}
		}
	}
	
	//Used to pass the above info to child classes
	protected void rightClicked(MouseEvent event)
	{

	}

	protected JMenuItem makeMenuItem(String label) {
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(this);
		return item;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		
	}

	/* This dosen't work so it's just called in each child
	 * public void actionPerformed(ActionEvent event) {
	 *
		if(event.getActionCommand() == "Equip")
		actionHappened(event);
	}

	//used to pass the event to the child classes
	private void actionHappened(ActionEvent event)
	{
		
	}*/ 

	public GameWindow getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(GameWindow parentWindow) {
		this.parentWindow = parentWindow;
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

	public JPopupMenu getPopup() {
		return popup;
	}

	public void setPopup(JPopupMenu popup) {
		this.popup = popup;
	}

	/*public JMenuItem getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(JMenuItem menuItem) {
		this.menuItem = menuItem;
	}*/
}

