/*
 * Stephen Rice
 * P2P Map Generation
 * Created 4/10/2014
 * 
 * Main.java: Takes a player number, port number (self), IP Address, port number (other), map size
 * and seed contribution. Note that the other player's IP address and port number are not used if 
 * "1" is entered as the player number
 *
 */

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MapGenP2P 
{
	public static void main(String[] args)
	{
		//Network Connections
		ServerSocket server = null;
		Socket client = null;
		OutputStream out = null;
		DataOutputStream dos = null;
		InputStream in = null;
		DataInputStream dis = null;
		
		//Seed Variables
		MessageDigest md = null;
		long SMC_Seed = 0;	
		
		//Expected order of args: Player Number, My Port, IP, Port, Map Size, Seed Contribution
		if(args.length < 5)
		{
			System.err.println("Error: Expecting Six arguments: Player Number, Local Port,  IP Address, Port, MapSize, Seed Contribution");
			System.exit(-1);
		}
		
		//Save user values
		int player = Integer.parseInt(args[0]);
		int myPort = Integer.parseInt(args[1]);
		String ip = args[2];
		int port = Integer.parseInt(args[3]);
		int mapSize = Integer.parseInt(args[4]);
		long seed = Long.parseLong(args[5]);
		
		//Check validity of provided map size
		if((mapSize - 1) % 4 != 0)
		{
			System.out.println("Map Size must be of the form (2^n) + 1");
			System.exit(-1);
		}
		
		//If Player 1, create a server and wait
		if(player == 1)
		{
			//Initiate network handshake with other client
			try 
			{
				server = new ServerSocket(myPort);
				
				//Wait for a client to connect
				while(true)
				{
					client = server.accept();
					System.out.println("Connected to other player");
					break;
				}
			} 
			catch (IOException e) 
			{
				System.err.println("Exception: Couldn't bind to port");
				System.exit(-1);
			}
		}
		else
		{
			//Connect to the server
			 try 
			 {
				client = new Socket(ip, port);
				System.out.println("Connected to player 1");
			 } 
			 catch (UnknownHostException e)
			 {
				 System.err.println("Exception: Unknown Host");
				 System.exit(-1);
			 } 
			 catch (IOException e) 
			 {
				System.err.println("Exception: Couldn't create connection to server");
				System.exit(-1);
			 }
		}
		
		//Bind inputs/outputs
		try
		{
			out = client.getOutputStream();
			dos = new DataOutputStream(out);
			
			in = client.getInputStream();
			dis = new DataInputStream(in);
			
		}
		catch(IOException e)
		{
			System.err.println("Exception: Couldn't capture Input/Output Streams");
		}
		
		//Create Hash of seed
		byte[] myHash = new byte[Long.SIZE];
		byte[] theirHash = new byte[Long.SIZE];
		long theirSeed = 0;
		try 
		{
			//Create a message digest with SHA-512
			md = MessageDigest.getInstance("SHA-512");
			
			//Get the bytes of the Long, then encode
			md.update(Long.toString(seed).getBytes());
			myHash = md.digest();
			
			//Send the hash to the other player
			dos.write(myHash);

			//Wait for reply from other user
			while(true)
			{
				if(dis.available() > 0)
				{
					dis.readFully(theirHash);
					break;
				}
			}
			
			//Send the unhashed integer
			dos.writeLong(seed);
			
			//Receive their unhashed integer
			while(true)
			{
				if(dis.available() > 0)
				{
					theirSeed = dis.readLong();
					break;
				}
			}
			
			//Check the seed
			md.update(Long.toString(theirSeed).getBytes());
			
			byte[] testHash = new byte[Long.SIZE];
			byte[] temp = md.digest();

			//Copy into a new array to ensure length parity
			for(int i = 0; i < temp.length; i++)
			{
				testHash[i] = temp[i];
			}
			
			//Check if the two hashes are identical
			if(!Arrays.equals(testHash, theirHash))
			{
				System.out.println("Their hashed seed does not match the sent seed");
				System.exit(-1);
			}
			
			//XOR the seed and save it
			SMC_Seed = seed ^ theirSeed;
			
		} 
		catch (NoSuchAlgorithmException e1) 
		{
			System.out.println("Exception: Cannot find specified hash");
		}
		catch (IOException e) 
		{
			System.out.println("Error reading message");
		}
		
		//Agree on seed, set seed
		RandomWrapper.setSeed(SMC_Seed);
		SimplexNoise.seedP();
		
		//Generate the Map and save it in a new variable
		MapGenerator mapGen = new MapGenerator(mapSize);
		char[][] map = mapGen.getMap(true);
		
		//Convert bytes to map
		byte[] bMap = toBytes(map);
		md.update(bMap);
		byte[] myHashMap = md.digest();
		byte[] theirHashMap = new byte[Long.SIZE];
		
		try 
		{
			//Send hashed map to client
			dos.write(myHashMap);
			
			//Wait for reply
			while(true)
			{
				if(dis.available() > 0)
				{
					dis.readFully(theirHashMap);
					break;
				}
			}
			
			//Copy my hash into a max size array
			byte[] testHash = new byte[Long.SIZE];

			for(int i = 0; i < myHashMap.length; i++)
			{
				testHash[i] = myHashMap[i];
			}
			
			//Compare
			if(!Arrays.equals(testHash, theirHashMap))
			{
				System.err.println("The maps do not match.");
				System.exit(-1);
			}
			
		}
		catch (IOException e) 
		{
			System.out.println("Exception: Comparing hashes");
		}
		
		//Write the map to a file
		writeMapToFile(map);
		
		System.out.println("A map was succesfully generated with the other player");
		
		System.exit(1);
	}
	
	public static void writeMapToFile(char[][] map)
	{
		//Write to file
		try 
		{
			System.out.println("Writing map to file");
			BufferedWriter writer = new BufferedWriter(new FileWriter("map.txt"));
			for(int i = 0; i < map.length; i++)
			{
				for(int j = 0; j < map[i].length; j++)
				{
					writer.write(map[j][i] + " ");
				}
				writer.write("\n");
			}
					
			writer.close();
			System.out.println("File written successfully");
		} 
		catch (IOException e) 
		{
			System.err.println("Problem writing to file");
		}
	}
	
	//Convert a character map into a byte array
	public static byte[] toBytes(char[][] map)
	{
		byte[] bytes = new byte[map.length*map.length];
		
		for(int i = 0; i < map.length; i++)
		{
			for(int j = 0; j < map[i].length; j++)
			{
				bytes[i + j * map.length] = (byte) map[j][i];
			}
		}
		
		return bytes;
		
	}
}


