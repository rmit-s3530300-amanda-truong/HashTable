import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class dbload {
	public static final String HEAP_FNAME = "heap.";
	public static final Integer INITIAL = 0;
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		if (args.length == 3)
  		{
			if (args[0].equals("-p") && isInteger(args[1]))
     		{
				read(args[2],Integer.parseInt(args[1]));
     		}
  		}
  		else
  		{
	  		System.out.println("Error: only pass in three arguments");
  		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("Load time: " + (endTime - startTime) + "ms");
	}
	
	//fReaderom sample assignment 1 HEAP IMPLEMENTATION
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
//				e.printStackTrace();
		}
		return isValidInt;
	}
	
	public static byte[] convertIntToBytes (int num)
	{
	  byte[] convertedNum = new byte[4];

	  convertedNum[0] = (byte) (num >> 24);
	  convertedNum[1] = (byte) (num >> 16);
	  convertedNum[2] = (byte) (num >> 8);
	  convertedNum[3] = (byte) (num);

	  return convertedNum;
	}
	
	public static int getSpaceUsed(ArrayList<Integer> spaceUsed)
	{
		return spaceUsed.size() - 1;
	}
	
	public static int getSpaceUsedByte(ArrayList<byte[]> pages)
	{
		return pages.size() - 1;
	}
	
	public static void read(String busfile, int size){
		String heapOutput = HEAP_FNAME+Integer.toString(size);
		//create page list
		ArrayList<byte[]> heapPages = new ArrayList<byte[]>(); 
		// list of space used in page
		ArrayList<Integer> spaceUsed = new ArrayList<Integer>();
		int recCount = 0;
		DataOutputStream os = null;
		spaceUsed.add(INITIAL);
		try 
		{
			os = new DataOutputStream(new FileOutputStream(heapOutput));
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		try
		{
			byte[] heapPage = new byte[size];
			//open file
			File file = new File(busfile);
			FileReader fReader = new FileReader(file);
			//initiliase bufferedreader
			BufferedReader bReader = new BufferedReader(fReader);
			String line = bReader.readLine();
			heapPages.add(heapPage);
			while(line!=null)
			{
				int spaceCount=0;
				recCount++; 
				//split by tab delim
				String[] lineSplit = line.split("\t");
				ArrayList<Byte> recordByte = new ArrayList<Byte>();
				byte[] recOffset = ":".getBytes();
				spaceCount = spaceUsed.get(getSpaceUsed(spaceUsed));
				for(byte offsetByte : recOffset)
				{
					recordByte.add(offsetByte);
					spaceCount++;
//					System.out.println(offsetByte);
				}
//				System.out.println(spaceCount);
				spaceUsed.remove(getSpaceUsed(spaceUsed));
				spaceUsed.add(spaceCount);
				for(String lineStr : lineSplit)
				{
					spaceCount = spaceUsed.get(getSpaceUsed(spaceUsed));
					byte[] sep = "\t".getBytes();
					for(byte wordByte : sep)
					{
//						System.out.println(wordByte);
						recordByte.add(wordByte);
						spaceCount++;
//						System.out.println(spaceCount);
					}
					spaceUsed.remove(getSpaceUsed(spaceUsed));
					spaceUsed.add(spaceCount);
					if(!lineStr.isEmpty())
					{
						boolean intCheck = isInteger(lineStr);
						if(intCheck)
						{
							//line is an integer
							int lineInt = Integer.parseInt(lineStr);
							//convert line to byte
							byte[] intArray = convertIntToBytes(lineInt);
							for(byte intBytes : intArray)
							{
								recordByte.add(intBytes);
							}
						}
						else
						{
							byte[] lineSArray = lineStr.getBytes();
							//line isn't an integer
							for(byte strArray : lineSArray) 
							{
								recordByte.add(strArray);
							}
						}
					}
				}

				byte[] recSep = ":".getBytes();
				spaceCount = spaceUsed.get(getSpaceUsed(spaceUsed));
				
				for(byte offsetByte : recSep)
				{
//					System.out.println(offsetByte);
					recordByte.add(offsetByte);
					spaceCount++;
				}
				spaceUsed.remove(getSpaceUsed(spaceUsed));
				spaceUsed.add(spaceCount);
				//need more space, add new page
				if(spaceUsed.get(getSpaceUsed(spaceUsed)) + recordByte.size() > size) 
				{
					int newPageCount = -1;
					os.write(heapPages.get(getSpaceUsedByte(heapPages)));
					byte[] page2 = new byte[size];
					heapPages.add(page2);
					for(byte recByte : recordByte)
					{
						++newPageCount;
						page2[newPageCount] = recByte;
//						System.out.println(newPageCount);
					}
					//System.out.println(newPageCount);
					spaceUsed.add(newPageCount);	
				}
				//still has space, add to page
				else 
				{
					byte[] existing = null;
					int pageCount = 0;
					pageCount = spaceUsed.get(getSpaceUsed(spaceUsed));
					for(byte recByte : recordByte)
					{
						existing = heapPages.get(getSpaceUsed(spaceUsed));
//						System.out.println(recByte);
						++pageCount;
						existing[pageCount] = recByte;
//						System.out.println(pageCount);
					}
					heapPages.remove(getSpaceUsedByte(heapPages));
					heapPages.add(existing);
//					System.out.println(pageCount);
					spaceUsed.remove(getSpaceUsed(spaceUsed));
					spaceUsed.add(pageCount);
//					System.out.println(pageCount);
				}
				//read next line
				line = bReader.readLine();
			}
			System.out.println("Amount of Pages:" +heapPages.size());
			System.out.println("Record Count: "+recCount);
			os.write(heapPages.get(getSpaceUsedByte(heapPages)));
			fReader.close();			
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		

	}
}