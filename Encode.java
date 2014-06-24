import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

		//CountFrequencies.countLetters(args[0]);
		setUpProbabilities(args[0]); 
		createTestText(Integer.parseInt(args[1]));
		
	}
	public static void createTestText(int k){
		try {
			File file = new File("testText.txt");
			if (!file.exists())
			{
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			while (k>0)
			{
				bw.write(findLetter());
				k--;
			}
			bw.close();
			System.out.println("Done");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public static String findLetter(){
		int min = 1;
		int max = sum;
		int target = min + (int)(Math.random() * ((max - min) + 1));
		int runningsum = 0;
		//System.out.println(target);
		
		if (target >= min && target<(runningsum += probabilities[0]))
			return "a";
		if (target >= min && target<(runningsum += probabilities[1]))
			return "b";
		if (target >= min && target<(runningsum += probabilities[2]))
			return "c";
		if (target >= min && target<(runningsum += probabilities[3]))
			return "d";
		if (target >= min && target<(runningsum += probabilities[4]))
			return "e";
		if (target >= min && target<(runningsum += probabilities[5]))
			return "f";
		if (target >= min && target<(runningsum += probabilities[6]))
			return "g";
		if (target >= min && target<(runningsum += probabilities[7]))
			return "h";
		if (target >= min && target<(runningsum += probabilities[8]))
			return "i";
		if (target >= min && target<(runningsum += probabilities[9]))
			return "j";
		if (target >= min && target<(runningsum += probabilities[10]))
			return "k";
		if (target >= min && target<(runningsum += probabilities[11]))
			return "l";
		if (target >= min && target<(runningsum += probabilities[12]))
			return "m";
		if (target >= min && target<(runningsum += probabilities[13]))
			return "n";
		if (target >= min && target<(runningsum += probabilities[14]))
			return "o";
		if (target >= min && target<(runningsum += probabilities[15]))
			return "p";
		if (target >= min && target<(runningsum += probabilities[16]))
			return "q";
		if (target >= min && target<(runningsum += probabilities[17]))
			return "r";
		if (target >= min && target<(runningsum += probabilities[18]))
			return "s";
		if (target >= min && target<(runningsum += probabilities[19]))
			return "t";
		if (target >= min && target<(runningsum += probabilities[20]))
			return "u";
		if (target >= min && target<(runningsum += probabilities[21]))
			return "v";
		if (target >= min && target<(runningsum += probabilities[22]))
			return "w";
		if (target >= min && target<(runningsum += probabilities[23]))
			return "x";
		if (target >= min && target<(runningsum += probabilities[24]))
			return "y";
		if (target >= min && target<=(runningsum += probabilities[25]))
			return "z";
		else
			return "ERROR";
	}
}
