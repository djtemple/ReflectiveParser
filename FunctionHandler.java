package parser;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.TreeMap;
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
			else if(Character.isDigit(functionStringArray[i])){
			
				// read the whole digit in
				while(Character.isDigit(functionStringArray[i])){
					temp += functionStringArray[i]; // start building the string 
					i++;
					/* TODO handle decimals here
					if(functionStringArray[i] == '.'){
						
					}
					*/
				}
				stringList.add(temp);
			}
			// if its in the alphabet...
			else if(Character.isAlphabetic(functionStringArray[i])){
				
				// read the whole word in
				while(Character.isAlphabetic(functionStringArray[i])){
					temp += functionStringArray[i]; // start constructing the token
					i++;
				}
				// check if its a valid expression
				if(isToken(temp))
					stringList.add(temp);
				else
					throw new ParseException("Invalid expression command at: ", i);
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
		System.out.println("Split the expression into tokens: " + stringList); 
		//TODO: put it into a tree now...
		
		
		
	}
	
	/*
	 * Function called to check if a string is a valid token
	 */
	private boolean isToken(String tkn){
	
		if(tokens.contains(tkn)){
			return true;
		}
		return true;
	}
	
	
	
	
	
	
	
	

}
