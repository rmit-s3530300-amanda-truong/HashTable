import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class hashquery {
	public static final String HASH_FNAME = "hash.";
	public static final int FINAL_SIZE = 2500000;
	public static final Integer INITIAL = 0;
	public static void main(String[] args) {
		int validArg = args.length-1;
		String queryStr = "";
		String size = null;
		int sizeInt = 0;
		int count = 0;
		for(int i=0; i<=validArg; i++)
		{
			System.out.println(args[i]);
			System.out.println(isInteger(args[i]));
			if(isInteger(args[i]))
			{
				size = args[i];
				sizeInt = Integer.parseInt(size);
			}
			else
			{
				if(count == 0)
				{
					queryStr = queryStr + args[i];
				}
				else
				{
					queryStr = queryStr + " " + args[i];
				}
				count++;
			}
		}
		System.out.println(queryStr);
        String hashOutputName = HASH_FNAME+sizeInt;
        File file = new File(hashOutputName);
//		System.out.println(file);
        System.out.println(args[0]);
		long start = System.currentTimeMillis();
		read(file,queryStr.toUpperCase(), sizeInt);
		long time = System.currentTimeMillis() - start;
		System.out.println("Time (ms):"+time);
	}
	
	//from sample assignment 1
	public static boolean isInteger(String s)
	{
		boolean isValidInt = false;
		try
		{
			Integer.parseInt(s);
			isValidInt = true;
		}
		catch (NumberFormatException e)
		{
//			e.printStackTrace();
		}
		return isValidInt;
	}
	
	public static void read(File file, String queryStr, int size)
	{
		String foundKey = null;
		
		//hash query to find match
		int keyHash = queryStr.hashCode();
        keyHash = keyHash%FINAL_SIZE;
        if(keyHash>INITIAL)
        {
//        	System.out.println("fits");
//        	System.out.println(keyHash);
        }
        else
        {
        	keyHash = keyHash+FINAL_SIZE;	
//        	System.out.println("doesnt fits");
//        	System.out.println(keyHash);
        }
        System.out.println(keyHash);
		//attempt to find the hash file and open
        FileReader fReader;
		try 
		{
			fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);        
	        for(int i=0; i<keyHash; i++)
	        {
	        	bReader.readLine();
	        }
	        foundKey = bReader.readLine();
	        System.out.println(foundKey);
	        searchMatch(foundKey);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}     
	}
	
	public static void searchMatch(String foundKey)
	{
		
	}
}
