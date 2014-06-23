import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;


public class CountFrequencies {

	public static int[] freq= new int[26];
	
	public static void printArray() {
		int current = 97;
		for (int a : freq) {
			System.out.println("Character: " + (char) current + " has count: " +a);  
			current++;
		}
	}
	
	public static void countLetters(String file){

		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file),
							Charset.forName("UTF-8")));
			int c;

			while((c = reader.read()) != -1) {
				//make uppercase letters lowercase
				//add count to array
				if (c >= 65 && c <= 90) {
					c = c -65;  
					freq[c] += 1; 
				}
				
				//these letters are lowercase
				//add count to array
				if (c >= 97 && c <= 122) {
					c = c -97; 
					freq[c] += 1; 
				} 
			}
			
			printArray();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
