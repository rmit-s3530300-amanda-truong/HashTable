import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class dbhashload {
	public static final String HEAP_FNAME = "heap.";
	public static final int FINAL_SIZE = 2500000;
	public static final Integer INITIAL = 0;
	public static final int BUSINESS_NAME_FIELD = 2;
	
	private static HashMap<Integer, ArrayList<String>> hashIndexMap;
	public static void main(String[] args) {
		hashIndexMap = new HashMap<>();
		if (args.length == 1)
		{
			if (isInteger(args[0]))
			{
				String heapOutputName = HEAP_FNAME+args[0];
				System.out.println(heapOutputName);
				File file = new File(heapOutputName);
				long start = System.currentTimeMillis();
				read(file, Integer.parseInt(args[0]));
				long time = System.currentTimeMillis() - start;
				System.out.println("Time (ms):"+time);
				for(Map.Entry<Integer,ArrayList<String>> entry: hashIndexMap.entrySet())
				{
					System.out.println(entry.getKey() + " : " + entry.getValue());
				}
			}
		}
		else
		{
			System.out.println("Error: only pass in two arguments");
		}

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
	
	public static void read(File file, int size)
	{
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
            	        	System.out.println("fits");
            	        	System.out.println(keyHash);
            	        }
            	        else
            	        {
            	        	keyHash = keyHash+FINAL_SIZE;	
            	        	System.out.println("doesnt fits");
            	        	System.out.println(keyHash);
            	        }
            	        if(hashIndexMap.get(keyHash) == null)
            	        {
            	        	ArrayList<String> valueList = new ArrayList<String>();
                			String value = Integer.toString(i);
                			valueList.add(value);
                			hashIndexMap.put(keyHash, valueList);
            	        }
            	        else
            	        {
                	        ArrayList<String> valueList = hashIndexMap.get(keyHash);
                			String value = Integer.toString(i);
                			valueList.add(value);
                			hashIndexMap.put(keyHash, valueList);
            	        }
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
