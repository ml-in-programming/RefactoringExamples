package name_pending;

import java.awt.event.KeyEvent;

/**
 * Contains all the hotkeys for the game
 * @author Hawox
 *
 */
public class Hotkeys {
	private int HOTKEY_inventory = KeyEvent.VK_I;
	private int HOTKEY_equipment = KeyEvent.VK_E;
	private int HOTKEY_moveUp = KeyEvent.VK_W;
	private int HOTKEY_moveLeft = KeyEvent.VK_A;
	private int HOTKEY_moveRight = KeyEvent.VK_D;
	private int HOTKEY_moveDown = KeyEvent.VK_S;
	
	
	public int getHOTKEY_inventory() {
		return HOTKEY_inventory;
	}
	public void setHOTKEY_inventory(int hOTKEY_inventory) {
		HOTKEY_inventory = hOTKEY_inventory;
	}
	public int getHOTKEY_equipment() {
		return HOTKEY_equipment;
	}
	public void setHOTKEY_equipment(int hOTKEY_equipment) {
		HOTKEY_equipment = hOTKEY_equipment;
	}
	public int getHOTKEY_moveUp() {
		return HOTKEY_moveUp;
	}
	public void setHOTKEY_moveUp(int hOTKEY_moveUp) {
		HOTKEY_moveUp = hOTKEY_moveUp;
	}
	public int getHOTKEY_moveLeft() {
		return HOTKEY_moveLeft;
	}
	public void setHOTKEY_moveLeft(int hOTKEY_moveLeft) {
		HOTKEY_moveLeft = hOTKEY_moveLeft;
	}
	public int getHOTKEY_moveRight() {
		return HOTKEY_moveRight;
	}
	public void setHOTKEY_moveRight(int hOTKEY_moveRight) {
		HOTKEY_moveRight = hOTKEY_moveRight;
	}
	public int getHOTKEY_moveDown() {
		return HOTKEY_moveDown;
	}
	public void setHOTKEY_moveDown(int hOTKEY_moveDown) {
		HOTKEY_moveDown = hOTKEY_moveDown;
	}
	
}
