import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class dbquery {
	public static final String HEAP_FNAME = "heap.";
	public static void main(String[] args) 
	{
		if (args.length == 2)
		{
			if (isInteger(args[1]))
			{
				String heapOutputName = HEAP_FNAME+args[1];
				File file = new File(heapOutputName);
//				System.out.println(file);
				long start = System.currentTimeMillis();
				read(file,args[0], Integer.parseInt(args[1]));
				long time = System.currentTimeMillis() - start;
				System.out.println("Time (ms):"+time);
			}
		}
		else
		{
			System.out.println("Error: only pass in two arguments");
		}
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
			e.printStackTrace();
		}
		return isValidInt;
	}
	
	//print out result of whether query was found or not
	public static void printCheck(boolean check, ArrayList<String> str)
	{
		if(check && !str.isEmpty())
		{
			for(String s: str)
			{
	    		System.out.println("Has Found: "+str);
			}
		}
		else
		{
			System.out.println("Not found in file.");
		}
	}
	
	//read in file and check query
	public static void read(File file, String query, int size) 
	{
		String queryLower = query.toUpperCase();
		byte[] heapPage = new byte[size];
		String heapStr = null;
		ArrayList<String> endStr = new ArrayList<String>();
		boolean check = false;
		// use RandomAccessFile to open file
		try (RandomAccessFile fileContent = new RandomAccessFile(file, "r")) 
		{
			long len = fileContent.length() / size;
		    for (int i = 0; i < len; i++) 
		    {
		    	fileContent.readFully(heapPage);
		    	heapStr = new String(heapPage);
//		        System.out.println(heapStr);
		        String[] heapArr = heapStr.split(":");
		        for(String str : heapArr) 
		        {
		        	String[] strSplit = str.split("\t");
		        	for(String word : strSplit)
		        	{
		        		String newWord = word.toUpperCase();
			        	if(newWord.contains(queryLower)) 
			        	{
			        		check = true;
			        		endStr.add(str);
			        	}
		        	}
		        }
		    }  
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		printCheck(check, endStr);
	}
}
