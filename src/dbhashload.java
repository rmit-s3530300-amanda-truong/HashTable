import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class dbhashload {
	public static final String HEAP_FNAME = "heap.";
	private static final int FINAL_SIZE = 2500000;
	private static HashMap<String, ArrayList<String>> hashIndexMap;
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
				//function
				long time = System.currentTimeMillis() - start;
				System.out.println("Time (ms):"+time);
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

}
