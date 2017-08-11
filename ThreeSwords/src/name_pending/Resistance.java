package name_pending;

import name_pending.Entities.Being;

public class Resistance {
	//Type of buff, how much of it, and the time of it
		String name = "none";
		int amount = 0;
		
		public Resistance(String name, int amount)
		{
			this.name = name;
			this.amount = amount;
		}

	/**
	 * being methods
	 */

	//Checks whether or not the entity has resistances to the element
	// TODO move to Being
	public static int checkResistance(Being being, String name)
	{
		//See if it exists in the set
		for(Resistance r : being.getResistances())
			if(r.getName() == name)
				return r.getAmount();
		return 0;
	}


	public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		
		//Stuff for the gui
}
