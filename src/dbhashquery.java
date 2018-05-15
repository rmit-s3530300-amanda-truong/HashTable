import java.io.File;

public class dbhashquery {
	public static final String HEAP_FNAME = "heap.";
	public static final String HASH_FNAME = "hash.";
	public static final int FINAL_SIZE = 2500000;
	public static final Integer INITIAL = 0;
	public static final int BUSINESS_NAME_FIELD = 2;
	public static void main(String[] args) {
		if (args.length == 2)
		{
			if (isInteger(args[1]))
			{
				String heapOutputName = HEAP_FNAME+args[1];
				File file = new File(heapOutputName);
//				System.out.println(file);
				long start = System.currentTimeMillis();
//				read(file,args[0], Integer.parseInt(args[1]));
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

}
