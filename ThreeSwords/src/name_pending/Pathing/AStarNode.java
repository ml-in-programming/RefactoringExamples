package name_pending.Pathing;

import java.awt.Point;

public class AStarNode {
	private double f; //Total cost of the node
	private double g; //Total cost to get to the node from the start
	private double h; //Total cost to get to the node from the End
	private Point at;
	private AStarNode parent; //Node that added this node to the list
	private boolean passable;
	
	AStarNode()
	{
		f = g = h = 0;
		at = null;
		parent = null;
		passable = false;
	}
	
	AStarNode(double f, double g, double h, Point at, AStarNode parent, boolean passable)
	{
		this.f = f;
		this.g = g;
		this.h = h;
		this.at = at;
		this.parent = parent;
		this.passable = passable;
	}
	
	public boolean compareIsLower(AStarNode other)
	{
		return ((this.getF() < other.getF()) ? true : false);
	}
	
	
	
	
	/*
	 * Getters and setters
	 */

	public double getF() {
		return f;
	}

	public void setF(int f) {
		this.f = f;
	}

	public double getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}
	
	public Point getAt()
	{
		return at;
	}
	
	public void setAt(Point at)
	{
		this.at = at;
	}

	public AStarNode getParent() {
		return parent;
	}

	public void setParent(AStarNode parent) {
		this.parent = parent;
	}

	public boolean isPassable() {
		return passable;
	}

	public void setPassable(boolean passable) {
		this.passable = passable;
	}
	
}
