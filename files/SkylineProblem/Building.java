/*
 * Stephen Rice
 * Main.java
 * Programmed for Advanced Data Structures & Algorithms
 * Written on April 24, 2013
 *
 * 
 * Stores building data with left, right and height.
 * 
 */

public class Building implements Comparable<Building>
{
	private int right;
	private int height;
	private int left;

	public Building(int l, int h, int r)
	{
		setRight(r);
		setHeight(h);
		setLeft(l);
	}
	
	public void printBuilding()
	{
		System.out.print( "(" + getLeft() + ", " + getHeight() + ", " + getRight() + ")");
	}

	public int getRight() 
	{
		return right;
	}

	public void setRight(int right) 
	{
		this.right = right;
	}

	public int getHeight() 
	{
		return height;
	}

	public void setHeight(int height) 
	{
		this.height = height;
	}

	public int getLeft() 
	{
		return left;
	}

	public void setLeft(int left) 
	{
		this.left = left;
	}

	//Allows sorting of buildings based on Left
	@Override
	public int compareTo(Building o) 
	{
		if(this.getLeft() < o.getLeft() )
		{
			return -1;
		}
		
		if(this.getLeft() == o.getLeft())
		{
			return 0;
		}
		
		return 1;
	}


	
}
