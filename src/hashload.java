import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

class HashIndex
{
    private int size;
    private HashBucket[] hashIndex;
    
    public HashIndex(int tableSize)
    {
        size = tableSize;
        hashIndex = new HashBucket[size];
        //initiliase hash index table values to 0
        for (int i=0; i<size; i++) {
        	hashIndex[i] = null;
        }
    }
    
    public void outputHash(String hashOutput)
    {
    	PrintWriter os = null;
        try 
        {
         os = new PrintWriter(hashOutput, "UTF-8");
        }
        catch (FileNotFoundException | UnsupportedEncodingException e) 
        {
        	e.printStackTrace();
        } 
        for (int i = 0; i < size; i++)
        {
            HashBucket bucket = hashIndex[i];
            while (bucket != null)
            {
            	os.print(bucket.bucketVal + " ");
            	bucket = bucket.valNext;
            }            
            os.println();
        }
        os.close();
    }
    
    public void add(int hashedKey, String bucketVal) 
    {
        if (hashIndex[hashedKey] == null)
        {
        	hashIndex[hashedKey] = new HashBucket(String.valueOf(hashedKey), bucketVal);
        }
        else 
        {
            HashBucket existing = hashIndex[hashedKey];
            while (existing.valNext != null && !existing.hashIndex.equals(String.valueOf(hashedKey))) 
            {
            	existing = existing.valNext;
            }
            existing.valNext = new HashBucket(String.valueOf(hashedKey), bucketVal);
        }
    }
    
	private class HashBucket{
		String hashIndex;
		String bucketVal;
		HashBucket valNext;

	    HashBucket(String hashIndex, String bucketVal) 
	    {
	        this.valNext = null;
	        this.hashIndex = hashIndex;
	        this.bucketVal = bucketVal;
	    }
	}
}

public class hashload {
	public static final String HEAP_FNAME = "heap.";
	public static final String HASH_FNAME = "hash.";
	public static final int FINAL_SIZE = 2500000;
	public static final Integer INITIAL = 0;
	public static final int BUSINESS_NAME_FIELD = 2;
	
	private static HashIndex table;
	public static void main(String[] args) {
		if (args.length == 1)
		{
			if (isInteger(args[0]))
			{
				String heapOutputName = HEAP_FNAME+args[0];
				File file = new File(heapOutputName);
				long start = System.currentTimeMillis();
				read(file, Integer.parseInt(args[0]));
//				writeToOutput(args[0]);
				table.outputHash(HASH_FNAME+args[0]);
				long time = System.currentTimeMillis() - start;
				System.out.println("Time (ms):"+time);
			}
		}
		else
		{
			System.out.println("Error: only pass in one arguments");
		}

	}
	
	public static HashIndex createTable()
	{
		table = new HashIndex(FINAL_SIZE);
		return table;
	}
	
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
			e.printStackTrace();
		}
		return isValidInt;
	}
	
	public static void writeToOutput(String args)
	{
        PrintWriter os = null;
        String hashOutput = HASH_FNAME + args;
        try 
        {
         os = new PrintWriter(hashOutput, "UTF-8");
        }
        catch (FileNotFoundException | UnsupportedEncodingException e) 
        {
        	e.printStackTrace();
        }      
        os.close();
	}
	
	public static void read(File file, int size)
	{
		HashIndex table = createTable();
		String heapStr = null;
        byte[] heapPage = new byte[size];
        // use RandomAccessFile to open file
        try(RandomAccessFile fileContent = new RandomAccessFile(file, "r")) 
        {
        	//to read each page
        	long indPage = fileContent.length() / size;
            for (int i = 0; i < indPage; i++) 
            {
            	fileContent.readFully(heapPage);
            	heapStr = new String(heapPage);
//		        System.out.println(heapStr);
            	String[] heapArr= heapStr.split(":");
            	for(String str : heapArr) 
            	{
            		String[] strSplit = str.split("\t");
            		int len = strSplit.length;
            		if(len>BUSINESS_NAME_FIELD) 
            		{
                		String strUpper = strSplit[2].toUpperCase();
                		int keyHash = strUpper.hashCode();
            	        keyHash = keyHash%FINAL_SIZE;
            	        if(keyHash>INITIAL)
            	        {
//            	        	System.out.println("fits");
//            	        	System.out.println(keyHash);
            	        }
            	        else
            	        {
            	        	keyHash = keyHash+FINAL_SIZE;	
//            	        	System.out.println("doesnt fits");
//            	        	System.out.println(keyHash);
            	        }     
            	        table.add(keyHash, Integer.toString(i));
            		}  
            	}
            }  
        } 
	    catch (IOException e) 
        {
	      e.printStackTrace();
	    }
	}
}
