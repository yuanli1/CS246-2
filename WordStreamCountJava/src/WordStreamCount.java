import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WordStreamCount {

	private static BufferedReader br;
	private static String paramsFilePath = "data\\hash_params.txt";
	private static String wordsStreamFilePathTiny = "data\\words_stream_tiny.txt";
	private static String countsFilePathTiny = "data\\counts_tiny.txt";
	private static String wordsStreamFilePath = "C:\\Users\\siamaks\\OneDrive\\Stanford\\CS246\\ProblemSets\\HW4\\Q4\\HW4-q4\\words_stream.txt";
	private static String countsFilePath = "C:\\Users\\siamaks\\OneDrive\\Stanford\\CS246\\ProblemSets\\HW4\\Q4\\HW4-q4\\counts.txt";
	private static String errorsFilePath = "data\\errors.txt";
	private static double epsilon = Math.pow(10, -4) * Math.E;
	private static double delta = Math.pow((Math.E), -5);
	
	public static void main(String[] args) throws IOException {

		int num_hash_function = (int) Math.ceil(Math.log(1 / delta));
		int num_slots_per_hash = (int) Math.ceil(Math.E / epsilon);
		int[][] params = new int[][]{{3,1561},{17,277},{38,394},{61,13},{78,246}};
		int[][] hash_matrix = new int[num_hash_function][num_slots_per_hash];
		br = new BufferedReader(new FileReader(wordsStreamFilePathTiny));
		 
		String currentLine;
		int numLines = 0;
		while ((currentLine = br.readLine()) != null) {
			numLines ++;
			int currentId = Integer.parseInt(currentLine);
			for (int i=0;i < 5; i++)
			{
				int y = currentId % 123457;
			    int hash_val = ((params[i][0]*y+params[i][1]) % 123457) % 10000;
				hash_matrix[i][hash_val] ++;
			}
			//System.out.println(sCurrentLine);
			if (numLines % 10000 == 0)
				System.out.println(numLines);
		}
		
		
		// Now create the error Array:
		Map<String,Double> errors = new HashMap<String,Double>();
		
		br = new BufferedReader(new FileReader(countsFilePathTiny));

		while ((currentLine = br.readLine()) != null) {
			String[] tokens = currentLine.split("\\W+");
			int currentId = Integer.parseInt(tokens[0]);
			int currentCount = Integer.parseInt(tokens[1]);
			
			//Find the estimate error for the currentId:
			int currentMinCount = Integer.MAX_VALUE;
			for (int i=0;i < 5; i++)
			{
				int y = currentId % 123457;
			    int hash_val = ((params[i][0]*y+params[i][1]) % 123457) % 10000;
			    if (hash_matrix[i][hash_val] < currentMinCount)
			    {
			    	currentMinCount = hash_matrix[i][hash_val];
			    }
			}
			
			errors.put(tokens[0],((double)(currentMinCount - currentCount) / currentCount));			
		}
		
		File errorsOutputFile=new File(errorsFilePath);
	    FileOutputStream fos=new FileOutputStream(errorsOutputFile);
	    PrintWriter pw=new PrintWriter(fos);
	    
		for(Entry<String, Double> entry : errors.entrySet())
		{
			pw.println(entry.getKey() + "\t" + entry.getValue().toString());
		}

		pw.flush();
		pw.close();
		fos.close();
	        
		System.out.println();
	}
}
