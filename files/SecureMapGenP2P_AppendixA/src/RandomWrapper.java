import java.util.Random;

/*
 * Stephen Rice
 * P2P Map Generation
 * Created 4/10/2014
 * 
 *RandomWrapper.java: Wraps an RNG and allows a seed to be set  
 */
public class RandomWrapper 
{
	private static long seed;
	private static Random random;
	
	//Save and set a new seed in the RNG
	public static void setSeed(long inSeed)
	{
		seed = inSeed;
		random = new Random(seed);
	}
	
	//Return the current seed
	public static long getSeed()
	{
		return seed;
	}
	
	//Return the next random number
	public static double getRandom()
	{
		return random.nextDouble();
	}
}
