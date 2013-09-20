/*
 * Stephen Rice
 * Main.java
 * Programmed for Advanced Data Structures & Algorithms
 * Written on April 24, 2013
 *
 * 
 * Find the skyline of a list of buildings
 * 
 */

import java.util.ArrayList;

public class Solver 
{
	ArrayList<HeightPoint> solution;
	long startTime;
	long endTime;
	
	//Begin solving the problem and time it
	public Solver(ArrayList<Building> buildings)
	{
		startTime = System.currentTimeMillis();
		solution = skylineSolver(buildings);
		endTime = System.currentTimeMillis();
	}
	
	//Skyline Solver
	public ArrayList<HeightPoint> skylineSolver(ArrayList<Building> buildings)
	{
		ArrayList<HeightPoint> Left, Right;
		
		//Base Case
		if(buildings.size() == 1)
		{
			//Create a list of Height Points from a single building
			ArrayList<HeightPoint> newList = new ArrayList<HeightPoint>();
			newList.add( new HeightPoint(buildings.get(0).getLeft(), buildings.get(0).getHeight() ));
			newList.add(new HeightPoint(buildings.get(0).getRight(), 0));

			return newList;
		}
		
		//Divide the list of buildings in half and recursively call skyline solver
		int n = buildings.size() / 2;
		ArrayList<Building> tempLeft = new ArrayList<Building>();
		for(int i = 0; i < n; i++)
		{
			tempLeft.add(buildings.remove(i));
		}
		Left = skylineSolver(tempLeft);
		
		ArrayList<Building> tempRight = new ArrayList<Building>();
		for(int i = 0; i < buildings.size(); i++)
		{
			tempRight.add(buildings.remove(i));
		}
		Right = skylineSolver( tempRight ) ;
		
		//Return the combination of Left and Right
		return combine(Left, Right);
	}
	
	public ArrayList<HeightPoint> combine(ArrayList<HeightPoint> Left, ArrayList<HeightPoint> Right)
	{
		//Set heights of current skylines to zero
		int hR = 0, hL = 0;
		ArrayList<HeightPoint> skyline = new ArrayList<HeightPoint>();
		
		do
		{
			//If Left list is empty and right is not
			if(Left.size() == 0 && Right.size() > 0)
			{
				hR = Right.get(0).getY();
				skyline.add(Right.remove(0));
			}
			//If right list is empty and left is not
			else if(Left.size() > 0 && Right.size() == 0)
			{
				hL = Left.get(0).getY();
				skyline.add(Left.remove(0));
			}
			//If both lists are empty
			else if(Left.size() == 0 && Right.size() == 0)
			{
				break;
			}
			//If the right skyline is closer to start than the left skyline
			else if(Left.get(0).getX() > Right.get(0).getX())
			{
				//Store the height of the right skyline
				hR = Right.get(0).getY();
				//If the Right skyline is higher than the left skyline, add the right skyline
				if(hR > hL)
				{
					skyline.add( Right.remove(0));
				}
				//Otherwise change the height to the left skyline
				else
				{
					if(hL != skyline.get(skyline.size() - 1).getY())
					{
						skyline.add(new HeightPoint(Right.get(0).getX(), hL));
					}
					Right.remove(0);
				}
			}
			//If the left building is closer to the start than the right building
			else if(Left.get(0).getX() < Right.get(0).getX())
			{
				//Store the height of the left skyline
				hL = Left.get(0).getY();
				//If the left skyline is higher than the right skyline, add the left skyline
				if(hL > hR)
				{
					skyline.add( Left.remove(0) );
				}
				//Otherwise change the height to the right skyline
				else
				{
					if(hR != skyline.get(skyline.size() - 1).getY())
					{
						skyline.add(new HeightPoint(Left.get(0).getX(), hR));
					}
					Left.remove(0);
				}
			}
			//If both buildings start at the same place
			else
			{
				//Store the height of both skylines
				hL = Left.get(0).getY();
				hR = Right.get(0).getY();
				//Add the highest skyline to the array
				skyline.add(new HeightPoint(Right.get(0).getX(), Math.max(Left.get(0).getY(), Right.get(0).getY())));
				Left.remove(0);
				Right.remove(0);
			}
				
		} while(Right.size() != 0 || Left.size() != 0);
		
		return skyline;
	}

}
