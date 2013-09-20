/*
 * Stephen Rice
 * Main.java
 * Programmed for Advanced Data Structures & Algorithms
 * Written on April 24, 2013
 * 
 * 
 * Create randomized building's then run the Skyline solver
 * 
 */

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class Main 
{	
	public static void main(String[] args) 
	{
		int numBuildings = -1;
		long startTime, endTime;
		Random r = new Random();
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		//Read in the number of buildings to be created
		if(args.length == 0)
		{
			numBuildings = 50;
		}
		else
		{
			numBuildings = Integer.parseInt(args[0]);
		}
		
		int randomizationNum = numBuildings * 2;
		
		//Create X number of random buildings
		for(int i = 0; i < numBuildings; i++)
		{
			int left = r.nextInt(randomizationNum);
			int height = r.nextInt(20) + 5;
			int right = left + r.nextInt(10) + 5;
			
			buildings.add(new Building(left, height, right));
		}
		
		//Sort on the R value
		Collections.sort(buildings);
		
		//Print out the original buildings: This is commented out for speed reasons
		
		System.out.print("Buildings: ");
		for(int i = 0; i < buildings.size(); i++)
		{
			buildings.get(i).printBuilding();
			System.out.print(", ");
		}
		System.out.print("\n");
		
		//Create a skyline list, then call solver
		ArrayList<HeightPoint> skyline = new ArrayList<HeightPoint>();
		Solver solver = new Solver(buildings);
		skyline = solver.solution;
		
		//Print out the solution
		System.out.print("Skyline: ");
		for(int i = 0; i < skyline.size(); i++)
		{
			skyline.get(i).print();
			System.out.print(", ");
		}
		System.out.print("\n");
		
		System.out.println("Time Spent Solving: " + (solver.endTime - solver.startTime) + " milliseconds");
	}

}
