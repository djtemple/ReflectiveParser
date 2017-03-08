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
	public FunctionHandler(){
		this.tokens = new LinkedList<String>();
		
	}
	
	/*
	 * Function called to interpret the string into separate tokens
	 */
	public void interpretExpression(String function) throws ParseException{
		
		TreeMap<String, String> tm = new TreeMap();
		
		String tkn;
		
		// split the string into its individual parts to begin parsing
		char[] functionStringArray = function.toCharArray();
	
		// iterate through the entire array
		for(int i = 0; i < functionStringArray.length; i++){
			
		}	
	}

	private boolean ifToken(String tkn){
	
		if(tokens.contains(tkn)){
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	

}
