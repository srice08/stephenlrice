/*
	Stephen Rice
	Sieve.java
	
	Written September 20th, 2013
	
	Utilize the algorithm laid out in the Sieve of Eratosthenes to find the primes under a given value
*/

import java.util.ArrayList;

public class SieveInt
{
	private Boolean[] primes;
	public long startTime, endTime;
	
	public SieveInt(Boolean[] newPrimes)
	{
		//Copy passeed array into a local array
		primes = new Boolean[newPrimes.length];
		for(int i = 0; i < newPrimes.length; i++)
		{
			primes[i] = newPrimes[i];
		}

		primes[0] = false;
		primes[1] = false;
		
		startTime = System.currentTimeMillis();
		solve();
		endTime = System.currentTimeMillis();
	}
	
	//Loop through the array and mark all values that are not prime
	private void solve()
	{
		for(int i = 0; i < primes.length; i++)
		{
			if(primes[i] == true)
			{
				solveValue(i);
			}
		}
	}
	
	//Given a value, mark all multiples as not prime
	private void solveValue(int startVal)
	{
		for(int i = startVal * 2; i < primes.length; i+=startVal)
		{
			primes[i] = false;
		}
	}
	
	//Print out a list of primes (in readable format)
	public void printPrimes()
	{
		System.out.println("List of Primes below " + primes.length);
		for(int i = 0; i < primes.length; i++)
		{
			if(primes[i])
			{
				System.out.print(i + ", ");
			}
		}
	}
	
}