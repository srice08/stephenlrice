/*
	Stephen Rice
	Main.java
	
	Written September 20th, 2013
	
	Faciliate the use of Sieve.java
*/


public class Main
{
	
	public static void main(String args[])
	{
		int primeValue = -1;
	
		//If user does not enter a value, use a default of 100
		if(args.length == 0)
		{
			primeValue = 1000;
		}
		else if(Long.parseLong(args[0]) <= 0 || Long.parseLong(args[0]) > Integer.MAX_VALUE)
		{
			System.out.println("Input was either less than 0 or greater than 2^31 - 1");
			System.exit(-1);
		}
		else
		{
			primeValue = Integer.parseInt(args[0]);
		}
		
		//Create an array of booleans and set them all to true
		Boolean[] primes = new Boolean[primeValue];
		for(int i = 0; i < primeValue; i++)
		{
			primes[i] = true;
		}
		
		//Solve Primes
		SieveInt primeSieve = new SieveInt(primes);
		
		primeSieve.printPrimes();
		System.out.println("\nThis took " + (primeSieve.endTime - primeSieve.startTime) + " milliseconds to calculate.");
	}
	
	/*
	This version uses the standard sieve class
	public static void main(String args[])
	{
		long primeValue = -1;
	
		//If user does not enter a value, use a default of 100
		if(args.length == 0)
		{
			primeValue = 1000;
		}
		else
		{
			primeValue = Long.parseLong(args[0]);
		}
		
		//Create an array of booleans and set them all to true
		//Boolean[] primes = new Boolean[primeValue];
		ArrayList<Boolean> primes = new ArrayList<Boolean>();
		for(int i = 0; i < primeValue; i++)
		{
			primes.add(true);
		}
		
		//Solve Primes
		Sieve primeSieve = new Sieve(primes);
		
		primeSieve.printPrimes();
		System.out.println("\nThis took " + (primeSieve.endTime - primeSieve.startTime) + " milliseconds to calculate.");
	}*/
}