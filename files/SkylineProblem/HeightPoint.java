/*
 * Stephen Rice
 * Main.java
 * Programmed for Advanced Data Structures & Algorithms
 * Written on April 24, 2013
 *
 *
 * HeightPoint represents a change in the skyline's height at point x to point y
 * 
 */

public class HeightPoint 
{
	private int x;
	private int y;
	
	//Constructor
	public HeightPoint(int nx, int ny)
	{
		setX(nx);
		setY(ny);
	}
	
	//Print out (X, Y)
	public void print()
	{
		System.out.print("(" + getX() + ", " + getY() + ")");
	}

	//Getters & Setters
	public int getX() 
	{
		return x;
	}

	public void setX(int x) 
	{
		this.x = x;
	}

	public int getY() 
	{
		return y;
	}

	public void setY(int y) 
	{
		this.y = y;
	}
}
