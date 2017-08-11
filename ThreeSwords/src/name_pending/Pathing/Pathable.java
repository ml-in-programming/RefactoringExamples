package name_pending.Pathing;

import java.awt.Point;
import java.util.ArrayList;

public class Pathable {
	/*List of points that creates the path the object will take
	 * Each point is a node in the A* algorithm
	 */
	protected ArrayList<Point> pathPoints = new ArrayList<Point>();
	private AStarPathing aStarPathing;
	private boolean waitingOnPath = false;
	
	protected Pathable(AStarPathing asp)
	{
		aStarPathing = asp;
	}
	
	public void needPath(Point at, Point target)
	{
		aStarPathing.addMe(this, at, target);
		waitingOnPath = true;
	}
	
	public void unqueue()
	{
		aStarPathing.removeMe(this);
	}
	
	//Called from the A* thread to give this a path
	public void setPath(ArrayList<Point> newPathPoints)
	{
		this.pathPoints = newPathPoints;
	}

	public ArrayList<Point> getPathPoints() {
		return pathPoints;
	}

	public void setPathPoints(ArrayList<Point> pathPoints) {
		this.pathPoints = pathPoints;
	}

	public AStarPathing getaStarPathing() {
		return aStarPathing;
	}

	public void setaStarPathing(AStarPathing aStarPathing) {
		this.aStarPathing = aStarPathing;
	}

	public boolean isWaitingOnPath() {
		return waitingOnPath;
	}

	public void setWaitingOnPath(boolean waitingOnPath) {
		this.waitingOnPath = waitingOnPath;
	}
}
