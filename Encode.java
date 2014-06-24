import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.PriorityQueue;



public class Encode {

	public static class Node {
		Node left;			//children
		Node right;
		int probability;	//probability 
		char letter; 		//letter we are encoding/decoding
		String encode; 		//encode value
		boolean isLeaf;
		int childProb;		//child's smallest probability

		public Node(int prob, char let, boolean leaf) {
			probability = prob;
			letter = let; 
			isLeaf = leaf; 
			encode = "";
			childProb = 1000000;
		}

		@Override public String toString() {
			return "(" + letter + ", " + probability +", " + encode + ")"; 
		}
		
		public boolean hasRight(){
			return (right == null) ? false: true; 
		}
		
		public boolean hasLeft(){
			return (left == null) ? false: true; 
		}
	}

	public static int[] probabilities = new int[26];
	public static Node huffmanTree; 
	public static int sum = 0; 
	public static String entropy = "";

	/*
	 * pre: reads in a file of probabilities (one number per line) and assigns to letter of alphabet
	 * post: calls setUpTree to place probabilities in a Huffman Tree to encode letters
	 */
	private static void readInFile(String file, int start) {
		
		//create priorityqueue to store in nodes to be put into Huffman tree
		PriorityQueue<Node> letters = new PriorityQueue<Node>(5, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				int retvalue = Integer.valueOf(n1.probability).compareTo(n2.probability);
				if (retvalue == 0)
					return Integer.valueOf(n1.childProb).compareTo(n2.childProb);
				return retvalue; 
			}});

		//read in the file
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream

(file),
							Charset.forName("UTF-8")));
			String aLine;
			int letterValue = start +97; 

			//create a node for each line of data in file, place in min to max order
			while ((aLine = reader.readLine()) != null){
				int lineValue = Integer.parseInt(aLine);
				Node aNode = new Node(lineValue, (char) letterValue, true); 
				letters.add(aNode); 
				sum += lineValue;
				letterValue++;
			}
			
			reader.close();
			setUpTree(letters);   //use priorityqueue to create Huffman tree
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setUpTree(PriorityQueue<Node> letters) {
		
		while (letters.size() > 1){
			Node smallest = letters.remove();
			appendEncodingDown(smallest,0); 
			Node smaller  = letters.remove();
			appendEncodingDown(smaller, 1); 
			
			Node parent = new Node(smallest.probability + smaller.probability, ' ', false);
			parent.childProb = smallest.probability;
			parent.left=smallest;
			parent.right=smaller;
			letters.add(parent); 
		}
		
		huffmanTree = letters.remove();
	}

	private static void appendEncodingDown(Node smaller, int i) {
		if (smaller.isLeaf) { smaller.encode = i + smaller.encode; }
		else {
			smaller.encode = i + smaller.encode;
			if (smaller.hasLeft()) appendEncodingDown(smaller.right, i);
			if (smaller.hasRight()) appendEncodingDown(smaller.left, i); 
		}
	}
	
	private static void printTree(Node aNode) {
		if (aNode != null) {
			if (aNode.hasLeft()) printTree(aNode.left);
			System.out.println(aNode);
			if (aNode.hasRight()) printTree(aNode.right); 
		}
	}
	
	private static void printEntropy(Node aNode) {
		if (aNode.isLeaf){
			System.out.println("  " + aNode.letter + "\t\t  " + aNode.probability + "/" + sum + "\t\t\t" + aNode.encode); 
			entropy = entropy + aNode.probability + "/" + sum + " log(" + aNode.probability + "/" + 

sum + ") + ";
		}
		else {
			if (aNode.hasLeft()) printEntropy(aNode.left);
			if (aNode.hasRight()) printEntropy(aNode.right); 
		}
	}

	public static void entropy(){
		System.out.println("Result \t\tProbability \t\tEncoding");
		entropy = "H = -("; 
		printEntropy(huffmanTree); 
		entropy = entropy.substring(0, entropy.length()-3) + ")";
		System.out.println("\n" + entropy);
	}

	
	public static void main(String[] args) {
		int start = 0; 
		//CountFrequencies.countLetters(args[0]);
		//setUpProbabilities(args[0]); 
		readInFile(args[0], start); 
		entropy(); 
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
		
		//Is there a better way to do this?
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
