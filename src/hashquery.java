import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class hashquery {
	public static final String HEAP_FNAME = "heap.";
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
			if(isInteger(args[i]))
			{
				size = args[i];
				sizeInt = Integer.parseInt(size);
			}
			else
			{
				//if its the first query word no need for space
				if(count == 0)
				{
					queryStr = queryStr + args[i];
				}
				//add space if its more than one word 
				else
				{
					queryStr = queryStr + " " + args[i];
				}
				count++;
			}
		}
        String hashOutputName = HASH_FNAME+sizeInt;
        File file = new File(hashOutputName);
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
        String heapOutput = HEAP_FNAME + size;
        File heapFile = new File(heapOutput);
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

		//attempt to find the hash file and open
		try 
		{
			//open hash file
			FileReader fReader = new FileReader(file);
	        BufferedReader bReader = new BufferedReader(fReader);
	        //read the line with the keyHash
	        for(int x=0; x<keyHash; x++)
	        {
	        	bReader.readLine();
	        }
	        String line = bReader.readLine();
	        bReader.close();
	        //if line is not null
	        if(line.length() > 1)
	        {
	            String pNo = null; 
	            String foundKey = null;
	        	String[] keySplit = line.split(" ");
	            for(int i=0; i<keySplit.length; i++) 
	            {
	            	//get the page number
	            	pNo = keySplit[i];
	                //see if query matches key
	                foundKey = searchKey(heapFile, pNo, size, queryStr, foundKey);
	            }
	            if(foundKey == null) 
	            {
	                System.out.println("Not found");
	            } 
	            else 
	            {
	                System.out.println("Found" + foundKey);    
	            }
	        }
	        else
	        {
	        	System.out.println("Not found");
	        }
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}     
	}
	
	public static String searchKey(File heapFile, String pNo, int size, String queryStr, String foundKey)
	{
		boolean continueCheck = true;
		String queryLower = queryStr.toUpperCase();
        byte[] heapPage = new byte[size];
        try (RandomAccessFile busfile = new RandomAccessFile(heapFile, "r")) 
        {
        	int pNoInt = Integer.parseInt(pNo);
        	int bytePNo = pNoInt * size;
            busfile.skipBytes(bytePNo);
            long busSize = busfile.length() / size;
            for (int x=pNoInt; x<busSize; x++) 
            {            		
            	String heapStr = null;
            	//read page
            	busfile.readFully(heapPage);
            	//create string from read page
            	heapStr = new String(heapPage);
                //record delim
                String[] heapArr = heapStr.split(":");
                for(String str : heapArr) 
                {
                	if(continueCheck)
                	{
                		//word delim
                    	String[] strSplit = str.split("\t");
                        for(String word : strSplit)
                        {
                        	if(continueCheck)
                        	{
                            	//check if query matches
                            	String newWord = word.toUpperCase();
                                if(newWord.contains(queryLower)) 
                                {
                                    foundKey = str;
                                    continueCheck = false;
                                }	
                        	}
                        }	
                	}
                }
            }  
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return foundKey;
	}
}
