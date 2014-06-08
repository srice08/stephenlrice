/*
 * Stephen Rice
 * P2P Map Generation
 * Created 4/10/2014
 * 
 * MapGenerator.java: Given a map size, generates a new random map
 */

public class MapGenerator 
{
	//Stores the map
	private double[][] map; 
	private char[][] cMap;
	
	//Size of the map
	private int mapDim;
	
	//Map statistics
	private double largest, smallest, mean;
	
	//Map Created
	private boolean mapCreated;
	
	public MapGenerator(int inSize)
	{
		//Save the provided map size (assume size is checked prior)
		mapDim = inSize;
		
		mapCreated = false;
	}
	
	//Generate the Map
	private void generateMap()
	{
		System.out.println("Map Generator is starting");
		//Create the emtpy maps
		map = new double[mapDim][mapDim];
		cMap = new char[mapDim][mapDim];
			
		//Init corners to 0
		map[0][0] = 0;
		map[mapDim -1][0] = 0;
		map[0][mapDim -1] = 0;
		map[mapDim - 1][mapDim - 1] = 0;
		
		//Call recursively to sample, providing the 4 corners and a variance of .5
		sample(0,0, mapDim - 1, mapDim - 1, .5);
				
		//Calculate the means and maxes
		calculateStatistics();
				
		//Begin populating the character map
		LandAndWater(1.5);
		Resources();
		TreesAndDesert();
		
		mapCreated = true;
		System.out.println("Map Generator is finished");
	}
	
	//Recursive Sample: Takes two corners (A, D) to determine the sub grid
	private void sample(int x1, int y1, int x2, int y2, double random)
	{
		/*
		 Capitals: Original corner points
		 Lower: New generated points
		 
		 A - a - B
		 b - c - e
		 C - d - D
		 
		 */
		
		//Check to ensure that midpoints exist
		if(x2 - x1 > 1)
		{
			//Get height maps
			double A = map[x1][y1];
			double B = map[x2][y1];
			double C = map[x1][y2];
			double D = map[x2][y2];
			
			int newX = (x2+x1) / 2;
			int newY = (y2+y1) / 2;
			
			//Determine a,b,c,d,e
			
			map[newX][y1] = (A + B) / 2; //a
			map[x1][newY] = (A + C) / 2; //b
			
			//Ensure first point is always positive (avoids lake in middle)
			if(x1 == 0 && x2 == mapDim - 1)
			{
				map[newX][newY] = (A + B + C + D) / 4 + (RandomWrapper.getRandom() * random/2  ); //c
			}
			else
			{
				map[newX][newY] = (A + B + C + D) / 4 + (RandomWrapper.getRandom() * random - random/2  ); //c
			}
			
			map[newX][y2] = (C + D) / 2; //d
			map[x2][newY] = (B + D) / 2; //e

			//Recursively call sub quadrants
			sample(x1,y1,newX, newY, random/1.60); //A-c
			sample(newX, y1, x2, newY,  random/1.6); // a-e
			sample(x1, newY, newX, y2, random/1.6); // b-d
			sample(newX, newY, x2, y2, random/1.6);// c-D
		}
	}
	
	//Calculate the largest, smallest and mean values in the map
	private void calculateStatistics()
	{
		double large = Integer.MIN_VALUE;
		double small = Integer.MAX_VALUE;
		double sum = 0;
		
		for(int i = 0; i < mapDim; i++)
		{
			for(int j = 0; j < mapDim; j++)
			{
				//Check Largest
				if(map[j][i] > largest)
				{
					small = map[j][i]; 
				}
				
				if(map[j][i] < smallest)
				{
					large = map[j][i];
				}
				
				sum += map[j][i];
			}
		}
		
		largest = large;
		smallest = small;
		mean = sum / (mapDim * mapDim);
	}

	
	/*These functions scan through the map and generate land, water, forests, deserts
	 * and resources
	 * 
	 * KEY
	 * W: Water (< mean)
	 * G: Grasslands (default land)
	 * F: Forest
	 * D: Desert
	 */
	
	//Based on each heighpoint's value, compare to the mean and 
	//set land or water. The meanRatio will determine the amount of land vs. amount of water
	private void LandAndWater(double meanRatio)
	{
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				//Water
				if(map[j][i] < mean * meanRatio)
				{
					cMap[j][i] = 'W';
				}
			
				//Grasslands
				else
				{
					cMap[j][i] = 'G';
				}
			}
		}
	}

	//Generates a simplex noise map and creates trees and deserts
	//Note: Simplex noise is very simple and further refinements to the parameters (or another nosie generator)
	//may provide better results
	private void TreesAndDesert()
	{
		//Generate the simplex noise seed values
		//SimplexNoise.seedP();
		
		//Create the simplex noise object
		SimplexNoise sng = new SimplexNoise();
		float[][] humidity = sng.generateOctavedSimplexNoise(mapDim, mapDim, 2, 2.4f, .04f);
	
		//Set up desert and trees
		for(int i = 0; i < mapDim; i++)
		{
			for(int j = 0; j < mapDim; j++)
			{
				if(humidity[j][i] > .8 && cMap[j][i] == 'G')
				{
					cMap[j][i] = 'F';
				}
			
			
				if(humidity[j][i] < -1.5 && cMap[j][i] == 'G')
				{
					cMap[j][i] = 'D';
				}
			}
		}
	}

	//Generic Resource Generation. Full map should use specific resource generators
	//to create the proper distribution
	//Note: This function is also responsible for many of the random numbers that are generated
	private void Resources()
	{
		for(int i = 0; i < mapDim; i++)
		{
			for(int j = 0; j < mapDim; j++)
			{
				if(RandomWrapper.getRandom() > .9 && cMap[j][i] == 'G')
				{
					cMap[j][i] = 'R';
				}
			}
		}
	}
	
	//Return the map (or generate it if it hasn't been done)
	public char[][] getMap()
	{
		//If the map has not been created, make it.
		if(!mapCreated)
		{
			generateMap();
			return cMap;
		}
		
		//Otherwise return the previously generated map
		return cMap;
	}
	
	//Return the map, allow the user to specify the creation of a new map, even if the old one has been created
	public char[][] getMap(boolean newMap)
	{
		//If the user wants a new map, do that no matter what
		if(newMap)
		{
			generateMap();
			return cMap;
		}
		
		//Otherwise check if the "old" map exists
		if(!mapCreated)
		{
			generateMap();
			return cMap;
		}
		
		//Return the old map
		return cMap;
	}
}
