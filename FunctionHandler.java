package parser;

import java.lang.reflect.*;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Stack;

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
	 *  it does error checking
	 * 
	 * ex. (add 1 (mul 100 5)) would become...
	 * "(", "add", "1", "(", "mul", "10", "5", ")", ")"
	 */
	public void interpretExpression(String function) throws ParseException{
			
		LinkedList<String> stringList = new LinkedList<String>();
		String temp; 
		
		// split the string into its individual strings to begin parsing
		char[] functionStringArray = function.toCharArray();

		int i = 0; 
		while(i < functionStringArray.length){
			
			temp = "";
			// if its a bracket ( or )...
			if(functionStringArray[i] == '(' || functionStringArray[i] == ')'){
				stringList.add(Character.toString(functionStringArray[i]));
				i++; // increment
			}
			// if its a digit 0 - 9...
			else if(Character.isDigit(functionStringArray[i])){
				while(true){
					temp += functionStringArray[i++]; 
			
					if(functionStringArray[i] == '.'){	// if its supposed to be a float
						temp += functionStringArray[i++];
						if(!Character.isDigit(functionStringArray[i]))
							throw new ParseException("Invalid float: ", i);
						while(true){
							temp += functionStringArray[i];
							i++;
							if(functionStringArray[i] == ' ' || functionStringArray[i] == ')')
								break;
							else if(!Character.isDigit(functionStringArray[i]))
								throw new ParseException("Invalid float: ", i);
						}
					}
					if(functionStringArray[i] == ' ' || functionStringArray[i] == ')')
						break;
				}
				stringList.add(temp);
			}
			// if its a string 
			else if(functionStringArray[i] == '"'){
				temp += functionStringArray[i++]; // start building the string 
				
				while(true){
					temp += functionStringArray[i]; // start building the string 
					i++;
					if(functionStringArray[i] == '"'){
						temp += functionStringArray[i];
						i++;
						if(functionStringArray[i] != ' ' && functionStringArray[i] != ')')
							throw new ParseException("Error parsing: ", i);
						else
							break;
					}
				}
				stringList.add(temp);
			}
			// if its an identifier
			else if(Character.isAlphabetic(functionStringArray[i])){
				while(true){
					temp += functionStringArray[i++]; // start building the string 
					if(functionStringArray[i] == ' ')
						break;
				}
				if(isToken(temp))
					stringList.add(temp);
				else
					throw new ParseException("Parse Exception - Identifier error: ", i);
			}
			// if its a space...
			else if(functionStringArray[i] == ' '){
				i++;
			}
			else{
				throw new ParseException("Parse Exception - Encountered illegal input", i);
			}
		}
		System.out.println("Split the expression: " + stringList); 
		
		// make the tree NOW
		preorder(createTree(stringList));
		
		
		System.out.println(" ");
		
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
	
	
	void preorder(Node t) {
        if (t != null) {
        	System.out.print(t.value + " ");
        	preorder(t.left);
        	preorder(t.right);
        }
    }
	
	boolean oneParameter(String Function) {
		if (Function.equals("len") || Function.equals("inc") || Function.equals("dec")) {
			return true;
		}
		else
			return false;
	}
	
	boolean zeroParameter(String Function) {
		if (Function.equals("rand")) {
			return true;
		}
		else
			return false;
	}
	
	private Node createTree(LinkedList<String> parsedList) {
		System.out.println(parsedList);
		Stack<Node> stack = new Stack<Node>();
		Node t, t1, t2, t3;
		
		for (int i = 0; i < parsedList.size(); i++) {
					
			if(parsedList.get(i).equals(")")) {
					t1 = stack.pop();
					Node peek = stack.peek();
					String peekString = peek.value; 
					
					if (oneParameter(peekString)) {
						t2 = stack.pop();
						stack.pop();	
						
						t2.left = t1;
						stack.push(t2);	
					}
//					else if (zeroParameter(peekString)) {
//						stack.pop();
//						stack.push(t1);
//					}

					else {
						t2 = stack.pop();
						t3 = stack.pop();
						stack.pop();			
	
						t3.left = t2;
						t3.right = t1;
			
						stack.push(t3);	
					}
			}
			else {
				t = new Node(parsedList.get(i));
				stack.push(t);
			}
		}
		
		t = stack.peek();
		stack.pop();
		
		return t;
		
	}
}
