package name_pending.Entities;

import name_pending.Game;
import name_pending.DataBanks.ResourceDataBank;

public class Enviroment extends Entity{

	public Enviroment(Game theGame, int x, int y, int Speed, String name) {
		super(theGame, x, y, Speed, name);
	}
	
	public void onCreate()
	{
		this.setSprite(ResourceDataBank.getSprite("ErrorEnviroment.png"));
		this.setSolid(true);
	}

}
