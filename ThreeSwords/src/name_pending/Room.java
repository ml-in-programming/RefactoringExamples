package name_pending;

/**
 * This will house everything that will exist in each game room such as the size of the room, it's background etc
 * @author Hawox
 *
 */
public class Room {

	int width = 2000;
	int height = 2000;
	
	Room(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	
	
	
	
	
	/**
	 * Getters and setters
	 */

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
}
