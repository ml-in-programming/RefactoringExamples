package name_pending.DataBanks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import name_pending.Main;
import name_pending.Sprite;

/**
 * This class will load on sprites on game start and will send out the sprites that are asked for
 *  to other objects
 * @author Hawox
 * 
 * EDIT 1: Going to have it store Buffered Images and let the objects make their own sprites otherwise multiple objects share sprites.
 * 			Changed this classes name to ResourceLoader from SpriteLoader
 */

public class ResourceDataBank {
	private HashMap<String, BufferedImage> images = new HashMap<String, BufferedImage>();
	
	public void loadImages()
	{
		//Not sure how to get a relative path exactly so I'm hoping this is a good work around
		String url = this.getClass().getResource("Sprites/").getPath();
		
		//get a list of all the files that are in the sprites folder
		File folder = new File(url);
		File[] files = folder.listFiles();
		//Put the files in a hashset so we don't have to worry about arrangement
		HashSet<File> hashFiles = new HashSet<File>();
		for(File f : files)
		{
			if(f.isFile()) //also get rid of the directories
				hashFiles.add(f);
		}
		
		//remove all the files that are not png's
		for(File f : hashFiles)
		{
			//Split by "." because the last segment should be the extension
			//String[] split = f.getName().split(".");
			//if(!(split[split.length].equals("png"))) //not a png
			if(!(f.getName().endsWith(".png")))
			{
				//remove the non png
				hashFiles.remove(f);
			}
		}
		
		//At this point we should only have the .pngs that are in the sprites folder
		for(File f : hashFiles)
		{
			try{
				//Add a new sprite to the list with the file name and the file image
				//this.sprites.put(f.getName(), new Sprite(ImageIO.read(this.getClass().getResource(f.getPath()))));
				this.images.put(f.getName(), ImageIO.read(f));
			}catch(IOException e){}
			
		}
	}
	
	/**
	 * Since this now stores images, this was added to allow objects to obtain sprites easier
	 */
	
	/*public Sprite getSprite(String name)
	{
		return new Sprite(this.getImages().get(name));
	}*/
	
	public static Sprite getSprite(String name)
	{
		try{
			return new Sprite(ImageIO.read(new File (ResourceDataBank.class.getResource("Sprites/" + name).getPath()))).clone();
		}catch(IOException e){}
		return null;
	}
	
	public AudioInputStream getSound(String soundName)
	{
		try{
			return AudioSystem.getAudioInputStream(Main.class.getResourceAsStream("Music" + soundName));
		}catch(IOException | UnsupportedAudioFileException e){}
		return null;
	}

	/**
	 * Getters and setters
	 */
	public HashMap<String, BufferedImage> getImages() {
		return images;
	}

	public void setImages(HashMap<String, BufferedImage> sprites) {
		this.images = sprites;
	}
}
