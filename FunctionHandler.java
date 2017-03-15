package parser;

import java.lang.reflect.*;
import java.text.ParseException;
import java.util.Arrays;
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
	private Method[] methods;
	
	/*
	 * Basic constructor
	 * Handles setting all of the possible tokens
	 */
	public FunctionHandler(Class<?> subject){
		this.tokens = new LinkedList<String>();
		methods = subject.getMethods();
		
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
		char[] str = function.toCharArray();
		String temp = "";
		int i = 0;
		/*
		while(i < str.length){

			temp = "";
			
			if(str[i] == '(' || str[i] == ')'){
				stringList.add(Character.toString(str[i++]));
			}
			else if(Character.isDigit(str[i])){
				while(true){
					temp += str[i++];

					if(str[i] == '.'){	// if its supposed to be a float
						temp += str[i++];
						if(!Character.isDigit(str[i]))
							throw new ParseException("Invalid float: ", i);
						while(true){
							temp += str[i];
							i++;
							if(str[i] == ' ' || str[i] == ')')
								break;
							else if(!Character.isDigit(str[i]))
								throw new ParseException("Invalid float: ", i);
						}
					}
					if(str[i] == ' ' || str[i] == ')')
						break;
				}
				stringList.add(temp);
			}
			else if(str[i] == '"'){
				while(true){
					temp += str[i++]; // start building the string 
					if(str[i] == '"'){
						temp += str[i++];
						break;
					}
					if(i >= str.length)
						throw new ParseException("Invalid string: ", i);
				}
				stringList.add(temp);
			}
			else if(Character.isAlphabetic(str[i])){
				while(true){
					temp += str[i++]; // start building the string
					if(str[i] == ' ')
						break;
				}
				if(isToken(temp))
					stringList.add(temp);
				else
					throw new ParseException("Parse Exception - Identifier error: ", i);
			}
			else if(str[i] == ' '){
				i++;
			}
			else{
				throw new ParseException("Parse Exception - Encountered illegal input", i);
			}
		}
		
		System.out.println("Split the expression: " + stringList);
		*/
		System.out.println("Split the expression: " + checkMethods(function.toCharArray(), 0));
		

		System.out.println(" ");
		
	}
	
	private LinkedList<String> checkMethods(char[] str, int i) throws ParseException{
		
		LinkedList<String> list = new LinkedList<String>(); 
		String temp = "";
		
		if(str[i] == '('){
			// add the first opening bracket
			list.add(Character.toString(str[i++]));
			
			// read in the identifier
			while(true){
				temp += str[i++];
				if(i >= (str.length)){
					throw new ParseException("Invalid identifier: ", i);
				}else if(str[i] == ' '){ // read until a space is encountered
					if(!isToken(temp)) // check if its a proper method name
						throw new ParseException("Invalid identifier: ", i);
					list.add(temp);	
					temp = "";
					i++;	// skip over the space
					break;	
				}else if(str[i] == ')'){
					if(!isToken(temp)) // check if its a proper method name
						throw new ParseException("Invalid identifier: ", i);
					list.add(temp);	
					list.add(Character.toString(str[i++]));
					return list;
				}
			}
			
			
			// continue to read all the arguments
			while(true){
				temp = "";
				// if its a space, ignore it
				if(str[i] == ' ')
					i++;
				else if(Character.isDigit(str[i])){
					while(true){
						temp += str[i++];

						if(str[i] == '.'){	// if its supposed to be a float
							temp += str[i++];
							if(!Character.isDigit(str[i]))
								throw new ParseException("Invalid float: ", i);
							while(true){
								temp += str[i];
								i++;
								if(str[i] == ' ' || str[i] == ')')
									break;
								else if(!Character.isDigit(str[i]))
									throw new ParseException("Invalid float: ", i);
							}
						}
						if(str[i] == ' ' || str[i] == ')')
							break;
					}
					list.add(temp);
				}else if(str[i] == '"'){
					while(true){
						temp += str[i++]; // start building the string 
						if(str[i] == '"'){
							temp += str[i++];
							break;
						}
						if(i >= str.length)
							throw new ParseException("Invalid string: ", i);
					}
					list.add(temp);
				}else if(str[i] == '('){
					list.addAll(checkMethods(str, i)); // recursive call
					int j = 1;
					i++;
					while(j > 0 && i < str.length){
						
						if(str[i] == '(')
							j++;
						else if(str[i] == ')')
							j--;
						
						i++;
					}
					

				}else if(str[i] == ')'){ // ')' signifies all arguments have been read
					list.add(Character.toString(str[i++]));
					return list;
				}else{
					throw new ParseException("Incorrect input: ", i);
				}
				
				if(i >= str.length)
					break;
			}
			
		}else
			throw new ParseException("Bracket error: ", i); // first character MUST be a bracket
		return list;
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
