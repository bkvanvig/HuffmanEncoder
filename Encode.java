import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.PriorityQueue;



public class Encode {

	public static class Node {
		Node left;			//children
		Node right;
		int probability;	//probability of occurring
		double scaleLow; 		//used in generating text
		double scaleHigh; 		//used in generating text
		String letter; 		//letter we are encoding/decoding
		String encode; 		//encode value
		boolean isLeaf;
		int childProb;		//child's smallest probability

		public Node(int prob, String let, boolean leaf) {
			probability = prob;
			letter = let; 
			isLeaf = leaf; 
			encode = "";
			childProb = 1000000;
			scaleLow = 0.0; 
			scaleHigh = 0.0;
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
		
		public Node deepCopy() {
			return new Node(probability, letter, true); 
		}
	}

	public static Node[] huffmanLeaves; 
	public static Node huffmanTree; 
	public static int sum = 0; 
	public static String entropy = "";
	public static int numOfSymbols = 0; 
	public static double amountAccounted = 0.0; 

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
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
							Charset.forName("UTF-8")));
			String aLine;
			int letterValue = 97; 

			//create a node for each line of data in file, place in min to max order
			while ((aLine = reader.readLine()) != null){
				int lineValue = Integer.parseInt(aLine);
				Node aNode = new Node(lineValue, String.valueOf((char)letterValue), true); 
				letters.add(aNode); 
				sum += lineValue;
				letterValue++;
			}
			
			int times = start;
			int orig = sum; 
			
			while (times > 0) {
				sum = sum * orig; 
				times--; 
			}
			 
			reader.close();
			letters = findPerm(letters, start--); 
			numOfSymbols = letters.size(); 
			setUpTree(letters);   //use priorityqueue to create Huffman tree
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static PriorityQueue<Node> findPerm(PriorityQueue<Node> root, int i) {
		//create priorityqueue to store in nodes to be put into Huffman tree
		PriorityQueue<Node> letters = new PriorityQueue<Node>(5, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				int retvalue = Integer.valueOf(n1.probability).compareTo(n2.probability);
				if (retvalue == 0)
					return Integer.valueOf(n1.childProb).compareTo(n2.childProb);
				return retvalue; 
			}});
		
		PriorityQueue<Node> adding  = new PriorityQueue<Node>(5, new Comparator<Node>() {
			public int compare(Node n1, Node n2) {
				int retvalue = Integer.valueOf(n1.probability).compareTo(n2.probability);
				if (retvalue == 0)
					return Integer.valueOf(n1.childProb).compareTo(n2.childProb);
				return retvalue; 
			}});
		
		for (Node aNode : root) 
			letters.add(aNode.deepCopy());
		
		int times = 0; 
		while (times < i) {
			for  (Node aNode : root){
				for (Node letterNode : letters){
					adding.add(new Node(letterNode.probability * aNode.probability, letterNode.letter + aNode.letter, true)); 
				}
			}
			
			letters = adding;
			adding = new PriorityQueue<Node>(5, new Comparator<Node>() {
				public int compare(Node n1, Node n2) {
					int retvalue = Integer.valueOf(n1.probability).compareTo(n2.probability);
					if (retvalue == 0)
						return Integer.valueOf(n1.childProb).compareTo(n2.childProb);
					return retvalue; 
				}});
			times++;

		}
		return letters; 
	}

	/*
	 * pre: accepts a priorityqueue of nodes and sets up a Huffman tree
	 */
	private static void setUpTree(PriorityQueue<Node> letters) {
		
		//sets up huffman tree
		while (letters.size() > 1){
			//removes smallest nodes and appends encoding all the way down the tree
			Node smallest = letters.remove();
			appendEncodingDown(smallest,0); 
			Node smaller  = letters.remove();
			appendEncodingDown(smaller, 1); 
			
			//creates a parent and sets two smallest nodes as children to parent node
			Node parent = new Node(smallest.probability + smaller.probability, "", false);
			parent.childProb = smallest.probability;
			parent.left=smallest;
			parent.right=smaller;
			letters.add(parent); 		//adds parent back to priority queue
		}
		
		Node root = letters.remove();
		setScales(root); 
		huffmanLeaves = new Node[numOfSymbols]; 
		setUpArray(root); 	//places leaves in an array to quickly iterate through
	}
	
	//Appends a scale value used to generate text
	public static void setScales(Node aNode) {
		if (aNode.isLeaf) {
			double prob = aNode.probability/(sum * 1.0);
			aNode.scaleLow = amountAccounted;  
			aNode.scaleHigh = amountAccounted + prob; 
			amountAccounted = amountAccounted + prob; 
		}
		else {
			setScales(aNode.right);
			setScales(aNode.left);  
		}
	}
	
	/*
	 * pre: huffman tree saved in node - takes leaves and adds to array
	 */
	private static void setUpArray(Node aNode){
		if (aNode.isLeaf)
			huffmanLeaves[--numOfSymbols] = aNode; 
		else{
			setUpArray(aNode.left);
			setUpArray(aNode.right); 
		}
	}

	/*
	 *  places a 1 or 0 infront of every node below node given
	 */
	private static void appendEncodingDown(Node smaller, int i) {
		if (smaller.isLeaf) { smaller.encode = i + smaller.encode; }
		else {
			smaller.encode = i + smaller.encode;
			if (smaller.hasLeft()) appendEncodingDown(smaller.right, i);
			if (smaller.hasRight()) appendEncodingDown(smaller.left, i); 
		}
	}
	
	//prints out tree for testing
	private static void printTree(Node aNode) {
		if (aNode != null) {
			if (aNode.hasLeft()) printTree(aNode.left);
			System.out.println(aNode);
			if (aNode.hasRight()) printTree(aNode.right); 
		}
	}
	
	//prints out entropy as a string
	private static void printEncodingTable() {
		for (int i=0; i<huffmanLeaves.length; i++)
		{
			System.out.println("  " + huffmanLeaves[i].letter + "\t\t  " + huffmanLeaves[i].probability + "/" + sum + "\t\t\t" + huffmanLeaves[i].encode); 
		}
	}

	public static void entropy(){
		System.out.println("Result \t\tProbability \t\tEncoding");
		printEncodingTable();
		double ent = 0;
		double x =0;
		System.out.print("H = -(");
		for (int i=0; i<huffmanLeaves.length; i++)
		{
			
			x = huffmanLeaves[i].probability/(sum*1.0);
			//System.out.println(x);
			if (x == 0)
				continue;
			if (i != huffmanLeaves.length-1)
			{
				System.out.print(huffmanLeaves[i].probability + "/" + sum + " log(" + huffmanLeaves[i].probability + "/" + sum + ") + ");
			}
			else
			{
				System.out.println(huffmanLeaves[i].probability + "/" + sum + " log(" + huffmanLeaves[i].probability + "/" + sum + "))");
			}
			
			ent += x*(Math.log(x)/Math.log(2));
		}
		System.out.println("H = "+(ent*-1));
	}

	
	public static void main(String[] args) throws IOException {
		int start = 0;
		int j = 2;
		if (args.length > 2)
		{
			j = Integer.parseInt(args[2]);
		}
			
		
		//Run permutations
		//able to accept > 2 if extra parameter is given
		while (start < j)
		{
			readInFile(args[0], start); 
			createTestText(Integer.parseInt(args[1])); 
			entropy(); 
			//System.out.println(args[1]);
			encode(start);
			decode(start);
			start++;
			amountAccounted = 0.0;
			sum = 0;
		}
	}
	
	public static void createTestText(int k){
		try {
			File file = new File("testText.txt");
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			while (k > 0)
			{
				bw.write(findLetter());
				k--;
			}
			bw.flush();
			bw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	public static String findLetter(){
		double randomInt = Math.random();
		
		for (Node aNode : huffmanLeaves) {
			//System.out.println(randomInt+"\t\t"+aNode.scaleLow +"\t\t"+aNode.scaleHigh);
			if (aNode.scaleLow < randomInt && aNode.scaleHigh > randomInt)
				return aNode.letter; 
		}
		
		System.out.println("THERE IS AN ERROR!!"); 
		return ""; 
	}

	public static void encode(int start) throws IOException{
		InputStream filein = new FileInputStream("testText.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(filein));
		String filename = "testText.enc"+(start+1);
		File file = new File(filename);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));

		String line;

		while((line = in.readLine()) != null){

			String[] word = line.split("");

			for (String letter : word) {
				for (Node aNode : huffmanLeaves){
					if (aNode.letter.equals(letter))
						bw.write(aNode.encode);
				}
			}
		}

		bw.flush();
		bw.close();
		in.close();
	}

	public static void decode(int start) {
		InputStream filein;
		try {
			String filename = "testText.enc"+(start+1);
			filein = new FileInputStream(filename);
			BufferedReader in = new BufferedReader(new InputStreamReader(filein));
			
			filename = "testText.dec"+(start+1);
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)));

			String line; 
			
			while((line = in.readLine()) != null){

				int lineIdx = 0;
				
				for (int idx = 0; idx < huffmanLeaves.length; idx++){
					
					Node aNode = huffmanLeaves[idx]; 
					int endpt = aNode.encode.length() + lineIdx; 
					if (line.length() >= endpt && aNode.encode.equals(line.substring(lineIdx, endpt))){
						bw.write(aNode.letter);
						idx = -1; 
						lineIdx = lineIdx + aNode.encode.length(); 
					}
				}
			}
			
			bw.flush();
			bw.close();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
