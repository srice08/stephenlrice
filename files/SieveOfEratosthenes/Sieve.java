/*
	Stephen Rice
	Sieve.java
	
	Written September 20th, 2013
	
	Utilize the algorithm laid out in the Sieve of Eratosthenes to find the primes under a given value
*/

import java.util.ArrayList;

public class Sieve
{
	private ArrayList<Boolean> primes;
	public long startTime, endTime;
	
	public Sieve(ArrayList<Boolean> newPrimes)
	{
		//Copy passeed array into a local array
		primes = new ArrayList<Boolean>();
		for(int i = 0; i < newPrimes.size(); i++)
		{
			primes.add(newPrimes.get(i));
		}

		primes.set(0, false);
		primes.set(1, false);
		
		startTime = System.currentTimeMillis();
		solve();
		endTime = System.currentTimeMillis();
	}
	
	//Loop through the array and mark all values that are not prime
	private void solve()
	{
		for(int i = 0; i < primes.size(); i++)
		{
			if(primes.get(i) == true)
			{
				solveValue(i);
			}
		}
	}
	
	//Given a value, mark all multiples as not prime
	private void solveValue(int startVal)
	{
		for(int i = startVal * 2; i < primes.size(); i+=startVal)
		{
			primes.set(i, false);
		}
	}
	
	//Print out a list of primes (in readable format)
	public void printPrimes()
	{
		System.out.println("List of Primes below " + primes.size());
		for(int i = 0; i < primes.size(); i++)
		{
			if(primes.get(i))
			{
				System.out.print(i + ", ");
			}
		}
	}
	
}