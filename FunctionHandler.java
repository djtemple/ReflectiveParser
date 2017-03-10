package parser;

import java.lang.reflect.*;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.TreeMap;

class Node{
	
	String value;
	Node left, right;
	
	Node(String s){
		value = s;
		left = right = null;
	}
	
}



/*
 *	Class built in to take in the functions and evaluate
 *	via a TreeMap
 * 
 * 
 * 
 * 
 */
public class FunctionHandler {

	// linked list to contain all the possible tokens
	private LinkedList<String> tokens;
	
	/*
	 * Basic constructor
	 * Handles setting all of the possible tokens
	 */
	public FunctionHandler(Class<?> subject){
		this.tokens = new LinkedList<String>();
		Method[] methods = subject.getMethods();
		
		for(int i = 0; i < methods.length; i++){
			tokens.add(methods[i].getName().toString()); // retrieve every method name
		}
	}
	
	/*
	 * Function called to take the string and put it into a parse tree
	 * 
	 *  First, it splits the string into tokens of type string
	 *  consisting of brackets, numbers, and words
	 * 
	 * ex. (add 1 (mul 100 5)) would become...
	 * "(", "add", "1", "(", "mul", "10", "5", ")", ")"
	 */
	public void interpretExpression(String function) throws ParseException{
		
		TreeMap<String, String> tm = new TreeMap();
		
		LinkedList<String> stringList = new LinkedList<String>();
		String temp; 
		
		// split the string into its individual strings to begin parsing
		char[] functionStringArray = function.toCharArray();

		int i = 0; 
		while(i < functionStringArray.length){
			
			temp = ""; // reset string first
			
			// if its a bracket ( or )...
			if(functionStringArray[i] == '(' || functionStringArray[i] == ')'){
				stringList.add(Character.toString(functionStringArray[i]));
				i++; // increment
			}
			// if its a digit 0 - 9...
			else if(Character.isDigit(functionStringArray[i]) || Character.isAlphabetic(functionStringArray[i])){
				while(Character.isDigit(functionStringArray[i]) || Character.isAlphabetic(functionStringArray[i])){
					temp += functionStringArray[i]; // start building the string 
					i++;
				}
				stringList.add(temp);
			}
			// if its a space...
			else if(functionStringArray[i] == ' '){
				// do nothing...we don't want no spaces!
				i++;
			}
			else{
				throw new ParseException("Encountered illegal character", i);
			}
		}
		System.out.println("Split the expression: " + stringList); 
		//TODO: put it into a tree now...
		
		Node root = new Node("");	// instantiate the root node
		
		for(int j = 0;j < stringList.size(); j++){
			
			
		}
	}
	
	/*
	 * Function called to check if a string is a valid token
	 */
	private boolean isToken(String tkn){
	
		if(tokens.contains(tkn)){
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	

}
