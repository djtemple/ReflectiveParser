package parser;

import java.lang.reflect.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

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

		System.out.println("Split the expression: " + checkMethods(function.toCharArray(), 0));


		System.out.println(treeEvaluate(createTree(checkMethods(function.toCharArray(), 0))));

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
					i++;
					while(true){
						temp += str[i++]; // start building the string
						if(str[i] == '"'){
							i++;
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

	public class Node{
	Node left, right;
	String value;
	Boolean method;
	Integer numOfParam;
	String type;
	Float floatVal;
	String stringVal;
	Integer intVal;
	int i = 0;

	void NodeInt(String s) {
		intVal = Integer.parseInt(s);

	}
	void NodeFloat(String s){
		floatVal = java.lang.Float.valueOf(s);

	}

	Node (String s){
		value = s;
		char[] valueArray = s.toCharArray();
		left = right = null;

				if (tokens.contains(value)) {
					method = true;
				} else {
					method = false;
				}
				if (!method) {

					while(i < valueArray.length){
						if(Character.isAlphabetic(valueArray[i])) {
							type = "string";
							stringVal = s;
							break;
						}
						else if (s.contains(".")) {
							type = "float";
							NodeFloat(s);
							break;
						}
						else if (!s.equals("(")){
							type = "int";
							NodeInt(s);
						}
						i++;
					}
				}
	}
}

private String treeEvaluate(Node t) {

	Integer intVal = 0;
	float floatVal = 0;
	String stringVal = " ";
	int methodNum = 0;

			if (t != null) {

				if (t.left != null && t.right != null) {
					treeEvaluate(t.left);
					treeEvaluate(t.right);
				}

				if (t.method) {

					if (t.numOfParam == 2 && (t.left.type == t.right.type)) {
						if (t.left.type.equals("int")) {
							for (int i = 0; i < tokens.size(); i++) {
								if (methods[i].getReturnType().equals(int.class)&& t.value.equals(methods[i].getName())) {
									methodNum = i;
									try {
										intVal = (int) (methods[methodNum].invoke(null, t.left.intVal ,t.right.intVal));
										}
										catch (IllegalAccessException | IllegalArgumentException
												| InvocationTargetException e) {
												e.printStackTrace();
												}
										t.intVal = intVal;
										t.type = "int";
										t.method = false;
										t.value = Integer.toString(intVal);
										break;
									}
								}
							}
						else if (t.left.type.equals("float")) {
							for (int i = 0; i < tokens.size(); i++) {
								if (methods[i].getReturnType().equals(float.class)&& t.value.equals(methods[i].getName())) {
									methodNum = i;
									try {
										floatVal = (float) (methods[methodNum].invoke(null, t.left.floatVal ,t.right.floatVal));
										}
										catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
										}
										t.floatVal = floatVal;
										t.type = "float";
										t.method = false;
										t.value = java.lang.String.valueOf(floatVal);
									}
								}
							}
						else if (t.left.type.equals("string")) {
							for (int i = 0; i < tokens.size(); i++) {
								if (methods[i].getReturnType().equals(String.class)&& t.value.equals(methods[i].getName())) {
									methodNum = i;
									try {
										stringVal = (String) (methods[methodNum].invoke(null, t.left.stringVal ,t.right.stringVal));
										}
										catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
										}
										t.stringVal = stringVal;
										t.type = "string";
										t.method = false;
										t.value = stringVal;
									}
								}
						}
					}
					else if (t.numOfParam ==2 && (t.left.type != t.right.type)) {
						return ("Matching function for '(" + t.value + " " + t.left.type + " " + t.right.type + ")' not found at offset 10");
					}

					if (t.numOfParam == 1) {

						if (t.left.type.equals("int")) {
							for (int i = 0; i < tokens.size(); i++) {
								if (t.value.equals(methods[i].getName()) && (t.left.intVal.getClass() == (methods[i].getReturnType()))) {
									methodNum = i;
									try {
										intVal = (int) (methods[methodNum].invoke(null, t.left.intVal));
										}
										catch (IllegalAccessException | IllegalArgumentException
												| InvocationTargetException e) {
												e.printStackTrace();
												}
										t.intVal = intVal;
										t.type = "int";
										t.method = false;
										t.value = Integer.toString(intVal);
									}
								}
							}

						else if (t.left.type.equals("float")) {
							for (int i = 0; i < tokens.size(); i++) {
								if (t.value.equals(methods[i].getName()) && (t.left.floatVal.getClass() == (methods[i].getReturnType()))) {
									methodNum = i;
									try {
										floatVal = (float) (methods[methodNum].invoke(null, t.left.floatVal));
										}
										catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
										}
										t.floatVal = floatVal;
										t.type = "float";
										t.method = false;
										t.value = java.lang.String.valueOf(floatVal);
									}
								}
							}

						else if (t.left.type.equals("string")) {
							for (int i = 0; i < tokens.size(); i++) {
								if (methods[i].getReturnType().equals(int.class)&& t.value.equals(methods[i].getName())) {
									methodNum = i;
									try {
										intVal = (Integer) (methods[methodNum].invoke(null, t.left.stringVal));
										}
										catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										e.printStackTrace();
										}
									t.intVal = intVal;
									t.type = "int";
									t.method = false;
									t.value = Integer.toString(intVal);
									}
								}
						}


					}
					else {
						for (int i = 0; i < tokens.size(); i++) {
							if (t.value.equals(methods[i].getName()) && methods[i].getParameterCount() ==0) {
								methodNum = i;
								if (t.value.equals("rand")) {
								try {
									intVal = (int) methods[methodNum].invoke(null);
									} catch (IllegalAccessException | IllegalArgumentException
										| InvocationTargetException e) {
									// TODO Auto-generated catch block
										e.printStackTrace();
									}
								t.intVal = intVal;
								t.type = "int";
								t.method = false;
								t.value = Integer.toString(intVal);
								}
								else if (t.value.equals("randFloat")) {
									try {
										floatVal = (float) methods[methodNum].invoke(null);
										} catch (IllegalAccessException | IllegalArgumentException
											| InvocationTargetException e) {
										// TODO Auto-generated catch block
											e.printStackTrace();
										}
									t.floatVal = floatVal;
									t.type = "float";
									t.method = false;
									t.value = String.valueOf(floatVal);
								}

								}
							}
						}
					}


			}
			return t.value;
	}

public Node createTree(LinkedList<String> parsedList) {
	System.out.println(parsedList);
	Stack<Node> stack = new Stack<Node>();
	Stack<Node> stackTemp = new Stack<Node>();
	Node t, t1, t2, t3;
	int numOfParameters = -1;

	for (int i = 0; i < parsedList.size(); i++) {

		if(parsedList.get(i).equals(")")) {

			while(!(stack.peek().value).equals("(")) {
				stackTemp.push(stack.pop());
				numOfParameters++;
			}

			while(!stackTemp.isEmpty()) {
				stack.push(stackTemp.pop());
			}

			if (numOfParameters == 2) {
				t1 = stack.pop();
				t2 = stack.pop();
				t3 = stack.pop();
				stack.pop();

				t3.left = t2;
				t3.right = t1;

				t3.numOfParam = numOfParameters;
				stack.push(t3);

			}
			else if (numOfParameters == 1) {
				t1 = stack.pop();
				t2 = stack.pop();
				stack.pop();

				t2.left = t1;
				t2.numOfParam = numOfParameters;
				stack.push(t2);
			}

			else {
				t1 = stack.pop();
				stack.pop();

				t1.numOfParam = numOfParameters;
				stack.push(t1);
			}

		}

		else {
			t = new Node(parsedList.get(i));
			stack.push(t);
		}

		numOfParameters = -1;
	}

	t = stack.peek();
	stack.pop();

	return t;

}

}
