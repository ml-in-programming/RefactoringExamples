package name_pending.Pathing;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import name_pending.Game;
import name_pending.Entities.Entity;


/*
 * Learned my A* knowledge from mostly here: http://theory.stanford.edu/~amitp/GameProgramming/
 * Also based nearly all this code off of it
 */
public class AStarPathing implements Runnable{
	
	//How far apart the nodes are for pathing in both width and height
	static int NODE_DISTANCE = 8;
	static double BASE_MOVE_COST = 1.0;
	
	Game theGame = null;
	
	public AStarPathing(Game theGame)
	{
		this.theGame = theGame;
	}

	private ArrayList<PathingData> waitingList = new ArrayList<PathingData>();
	private AStarSearch aStarSearch = new AStarSearch();
	
	@Override
	public void run() {
		do{
			
			if(waitingList.size() > 0)
			{
				//Get a timer to time how long it took to find the path
				long start = System.currentTimeMillis();
				
				PathingData pd = waitingList.get(0);
				//set the goal
				aStarSearch.goal = pd.goal;
				aStarSearch.start = pd.start;

				pd.pathable.setPath(new ArrayList<Point>(aStarSearch.compute(pd.start)));
				pd.pathable.setWaitingOnPath(false);
				Game.addText(theGame.getConsole(), "AStarPathing pathset.");
				waitingList.remove(pd);
				Game.addText(theGame.getConsole(), "AStarPathing<< Item removed. Current wait:" + waitingList.size());
				if(pd.pathable instanceof Entity)
				{
					Entity e = (Entity) pd.pathable;
					Game.addText(theGame.getConsole(), "Found a path in " + Long.toString(System.currentTimeMillis()-start) + " ms for entity named: " + e.getName());
				}
			}
			//Let the thread sleep for a bit to get some rest
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}while(true);
	}
	
	/**
	 * Adds pathable to the pathing queue
	 * @param pathable
	 */
	public void addMe(Pathable pathable, Point at, Point target)
	{
		this.waitingList.add(new PathingData(target, at, pathable));
		//theGame.getConsole().addText("AStarPathing<< New Item in waiting list. Current wait:" + waitingList.size());
	}
	
	public void removeMe(Pathable p)
	{
		this.waitingList.remove(p);
	}
	
	//All the data that will be needed for each path
	public class PathingData{
		Point goal;
		Point start;
		Pathable pathable;
		PathingData(Point target, Point location, Pathable pathable)
		{
			this.goal = target;
			this.start = location;
			this.pathable = pathable;
		}
	}
	
	
	class AStarSearch extends AStar<Point>
	{
		Point goal = null;
		Point start = null;

		@Override
		protected boolean isGoal(Point node) {
			//check if the goal is within the reletive bounds of the node
			Rectangle nodeReletiveBounds = new Rectangle(node.x - (NODE_DISTANCE/2), node.y - (NODE_DISTANCE/2), NODE_DISTANCE * 2, NODE_DISTANCE * 2);
			if(nodeReletiveBounds.contains(goal))
			{
				goal = null;
				start = null;
				return true;
			}
			return false;
		}
		
		
		//Gets the cost based on the diagonal distance from the goal
		/*
		 * and it will not take into account ground that costs more to move over
		 * 
		 */
		@Override
		protected Double g(Point from, Point to) {
			/*Base cost is the distance from the start*/
			/*
			 * EDIT: Don't do the above because that will not take into account this distance this has already traveled, instead get the cost from the last node in the path.
			 * This way it will take into account the cost of the path up untill that point insead of just a straight line from the start of the path
			 */
			//use to because from is the previous node
			double cost = BASE_MOVE_COST * Math.max(Math.abs(to.x-goal.x), Math.abs(to.y-goal.y));
			//cost = to

			/* add the cost of any entity costs in the node area */
			//See if there are any entities within the space of this node
			//copy of the entity list so changes don't result in ConcurrentModificationExceptions
			HashSet<Entity> entityHashCopy = (HashSet<Entity>) theGame.getEntityHash();
			if(entityHashCopy.size() > 0)
			{
				for(Entity e : entityHashCopy)
				{
					Rectangle spriteRectangle = new Rectangle(e.getX(), e.getY(), e.getSprite().getWidth(),e.getSprite().getHeight());
					//If this node is within the entity
					if(spriteRectangle.contains(to))
					{
						//add the entities movement cost to this nodes cost
						cost += e.getPathingCost();
					}
				}
			}
			
			return cost;
						
			//The distance to the node will be it's starting cost
			//int targetX = to.x - from.x;
			//int targetY = to.y - from.y;

			//pythagorean theorem
			//double distanceTo = Math.sqrt(Math.abs(targetX * targetX) + Math.abs(targetY * targetY));
			
			//Cost to move
			//double costToMove = 1.0;
			
			//Diagonal distance Heuristic
			//double g = (costToMove * Math.max(Math.abs(from.x - to.x), Math.abs(from.y - to.y)));
			
			//return g;
		}

		@Override
		/* Get the cost based on it's distance to the goal 
		 * Don't take anything else into account for this because this is just a rough guess
		 * IE this does not account for obstacles in the way like g does */
		protected Double h(Point from, Point to) {
			//use to because from is the previous node
			
			//int xLength = goal.x - to.x;
			//int yLength = goal.y - to.y;

			//pythagorean theorem
			//double distanceTo = Math.sqrt(xLength * xLength + yLength * yLength);
			
			//Diagonal distance Heuristic
			//Add one to this so that AStar will perfer h over g; causing it to break ties and save processing time
			return ( (BASE_MOVE_COST * Math.max(Math.abs(to.x - goal.x), Math.abs(to.y - goal.y))) -1.0);
		}

		@Override
		protected List<Point> generateSuccessors(Point node) {
			//get a list of all the nodes next to the node provided
			
			List<Point> returnMe = new ArrayList<Point>();
			
			//top left
			returnMe.add(new Point(node.x - NODE_DISTANCE, node.y - NODE_DISTANCE));
			//top
			returnMe.add(new Point(node.x, node.y - NODE_DISTANCE));
			//top right
			returnMe.add(new Point(node.x + NODE_DISTANCE, node.y - NODE_DISTANCE));
			//right
			returnMe.add(new Point(node.x + NODE_DISTANCE, node.y));
			//bottom right
			returnMe.add(new Point(node.x + NODE_DISTANCE, node.y + NODE_DISTANCE));
			//bottom
			returnMe.add(new Point(node.x, node.y + NODE_DISTANCE));
			//bottom left
			returnMe.add(new Point(node.x - NODE_DISTANCE, node.y + NODE_DISTANCE));
			//left
			returnMe.add(new Point(node.x - NODE_DISTANCE, node.y));
			
			List<Point> removeList = new ArrayList<Point>();
			
			//copy of the entity list so changes don't result in ConcurrentModificationExceptions
			HashSet<Entity> entityHashCopy = (HashSet<Entity>) theGame.getEntityHash().clone();
			
			//check if any of the nodes above are solid and need to be removed
			for(Point p : returnMe)
			{
				if(entityHashCopy.size() > 0)
					for(Entity e : entityHashCopy)
					{
						Rectangle spriteRectangle = new Rectangle(e.getX(), e.getY(), e.getSprite().getWidth(),e.getSprite().getHeight());
						//If this node is within the entity
						if(spriteRectangle.contains(p))
						{
							removeList.add(p);
							continue;
						}
					}
			}
			
			if(removeList.size() > 0)
			{
				for(Point p : removeList)
				{
					returnMe.remove(p);
				}
			}

			return returnMe;
		}

		
	}

}
