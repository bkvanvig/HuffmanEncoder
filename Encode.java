import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;



public class Encode {
	public static int[] probabilities = new int[26]; 
	public static int sum = 0; 
	
	private static void setUpProbabilities(String file) {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file),
							Charset.forName("UTF-8")));
			String aLine;
			int idx = 0; 
		
			while ((aLine = reader.readLine()) != null){
				int lineValue = Integer.parseInt(aLine);
				probabilities[idx] = lineValue;
				sum += lineValue;
				idx++;
			}
			entropy(); 
			
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void entropy(){
		int current = 97;
		
		System.out.println("Result \t\tProbability \t\tEncoding");
		String entropy = "H = -("; 
		
		for (int a : probabilities) {
			if (a != 0) {
				System.out.println("  " + (char) current + "\t\t  " + a + "/" + sum); 
				entropy = entropy + a + "/" + sum + " log(" + a + "/" + sum + ") + ";
			}
			current++;
		}
		
		System.out.println("\n" + entropy.substring(0, entropy.length()-3) + ")");
	}
	
	public static void main(String[] args) {

		CountFrequencies.countLetters(args[0]);
		//setUpProbabilities(args[0]); 
	}
}
