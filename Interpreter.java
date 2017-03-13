package parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Scanner;
import java.net.URLClassLoader;
import java.net.URL;

public class Interpreter {
	static boolean verbose;
	static boolean running = true;
	static String jarname;
	static String classname = "Commands";
	static Class<?> subject;
	static String[] allowables = {"int", "float", "java.lang.String", "java.lang.Float", "java.lang.Integer"};
	
	static FunctionHandler funHandler; // handle all the fun!
	
	public static void main(String[] args){
		checkCommandLineArgs(args);
		
	}
	private static void checkCommandLineArgs(String[] args){
		boolean h = false;
		int i;
		for (i = 0; i < args.length; i++){
			if (args[i].charAt(0) == '-'){
				if (args[i].equals("-v") || args[i].equals("--verbose"))
					verbose = true;
				else if ((args[i].equals("-?") || args[i].equals("-h")) || args[i].equals("--help"))
					h = true;
				else if (args[i].charAt(1) == '-'){
					FatalErrors.unrecognizedQualifier(args[i]);
				}
				else {
					FatalErrors.unrecognizedLongQualifier(args[i]);
				}
			}
			else
				break;
		}
		for (int j = 0; i < args.length; i++, j++){
			if (args[i].charAt(0) == '-')
				FatalErrors.invalidArgOrder();
			else if (j == 0)
				jarname = args[i];
			else if (j == 1)
				classname = args[i];
			else
				FatalErrors.invalidNumberOfArgs();
		}
		if (args.length == 0)
			synopsis();
		else if (h && (jarname != null))
			FatalErrors.invalidHelpQualifier();
		else if (h){
			synopsis();
			System.out.println("\nThis program interprets commands of the format '(<method> {arg}*)' on the command line, finds corresponding");
			System.out.println("methods in <class-name>, and executes them, printing the result to sysout.");
		}
		else {
			try {
				File f = new File(jarname);
				if (!f.exists())
					throw new IOException();
				URL fURL = f.toURI().toURL();
				URL[] jfURL = {new URL("jar", "", fURL + "!/")};
				URLClassLoader loader = new URLClassLoader(jfURL);
				subject = Class.forName(classname, true, loader);
				funHandler = new FunctionHandler(subject);
				loader.close();
				console();
			} catch (ClassNotFoundException e) {
				System.err.println("Could not find class: " + classname);
				System.exit(-6);
			} catch (IOException e2){
				System.err.println("Could not find jar file: " + jarname);
				System.exit(-5);
			}
		}
		
	}
	
	static void synopsis(){
		System.out.println("Synopsis:");
		System.out.println("  methods");
		System.out.println("  methods { -h | -? | --help }+");
		System.out.println("  methods {-v --verbose}* <jar-file> [<class-name>]");
		System.out.println("Arguments:");
		System.out.println("  <jar-file>:   The .jar file that contains the class to load (see next line).");
		System.out.println("  <class-name>: The fully qualified class name containing public static command methods to call. [Default=\"Commands\"]");
		System.out.println("Qualifiers:");
		System.out.println("  -v --verbose: Print out detailed errors, warning, and tracking.");
		System.out.println("  -h -? --help: Print out a detailed help message.");
		System.out.println("Single-char qualifiers may be grouped; long qualifiers may be truncated to unique prefixes and are not case sensitive.");
	}
	
	private static void console(){
		Scanner s = new Scanner(System.in);
		help();
		String line;
		while (running) {
			System.out.print("> ");
			line = s.nextLine();
			if (line.length() == 0)
				continue;
			char c = line.charAt(0);
			if (line.length() == 1){
				switch(c){
				case 'q':
					running = false;
					break;
				case '?':
					help();
					break;
				case 'v':
					toggleVerbose();
					break;
				case 'f':
					listFunctions(subject);
					break;	
				default: 
					valueHandler(line);
					break;
				}
			}
			else if (c == '(')
				functionHandler(line);
			else{
				valueHandler(line);
			}
		}
		s.close();
		System.out.println("Bye");
	}
	
	private static void toggleVerbose(){
		verbose = !verbose;
		String message = verbose ? "Verbose on" : "Verbose off";
		System.out.println(message);
	}
	
		private static void valueHandler(String input){
		try {
			if (input.charAt(0) == '"'){
				int close = input.indexOf('"', 1);
				if (close == -1)
					throw new ParseException("Reached end of string while parsing", input.length());
				else if (close < (input.length() - 1))
					throw new ParseException("Unexpected character encountered at offset " + (close + 1), (close + 1) );
				else
					System.out.println(input);
			}
			else {
				int point = 0;
				int j = 0;
				if (input.charAt(j) == '+' || input.charAt(j) == '-')
					j++;
				for (int i = j; i < input.length(); i++){
					if (Character.isDigit(input.charAt(i)))
						continue;
					else if (input.charAt(i) == '.')
						point++;
					else
						throw new ParseException("Unexpected character encountered at offset " + i, i);					
				}
				if (point > 1){
					int offset = input.indexOf('.', (input.indexOf('.')+1));
					throw new ParseException("Unexpected character encountered at offset " + offset, offset);
				}
				else if (point == 1)
					System.out.println(Float.parseFloat(input));
				else
					System.out.println(Integer.parseInt(input));
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			System.out.println(input);
			for (int i = 0; i < e.getErrorOffset(); i++)
				System.out.print("-");
			System.out.println("^");
			if (verbose)
				e.printStackTrace(); 
		} catch (NumberFormatException e2) {
			System.out.println("Integer out of range");
			System.out.println(input);
			System.out.println("^");
			if (verbose)
				e2.printStackTrace();
		}
	}
	
	private static void functionHandler(String input){
		try{
		funHandler.interpretExpression(input);
		}catch(ParseException e){
			System.out.println(e);
		}
	}

	private static void listFunctions(Class<?> c){
		String name;
		String rType;
		Method[] methods = c.getMethods();
		boolean ignore;
		for (int i = 0; i < methods.length; i++){
			ignore = false;
			name = methods[i].getName();
			rType = methods[i].getReturnType().getName();
			if (Arrays.asList(allowables).contains(rType) == false)
				continue;
			Parameter[] parameters = methods[i].getParameters();
			for (int j = 0; j < parameters.length; j++){
				if (Arrays.asList(allowables).contains(parameters[j].getType().getName()) == false)
					ignore = true;
			}
			if (ignore)
				continue;
			System.out.print("(" + name);
			for (int j = 0; j < parameters.length; j++){
				if (parameters[j].getType().getName().equals("java.lang.String"))
					System.out.print(" String");
				else if (parameters[j].getType().getName().equals("java.lang.Integer"))
					System.out.print(" int");
				else if (parameters[j].getType().getName().equals("java.lang.Float"))
					System.out.print(" float");
				else
					System.out.print(" " + parameters[j].getType().getName());
			}

			if (rType.equals("java.lang.String"))
				System.out.println(") : String");
			else if (rType.equals("java.lang.Integer"))
				System.out.println(") : int");
			else if (rType.equals("java.lang.Float"))
				System.out.println(") : float");
			else
				System.out.println(") : " + rType);
			
		}
	}
	private static void help(){
		System.out.println("q           : Quit the program.");
		System.out.println("v           : Toggle verbose mode (stack traces).");
		System.out.println("f           : List all known functions.");
		System.out.println("?           : Print this helpful text.");
		System.out.println("<expression>: Evaluate the expression.");
		System.out.println("Expressions can be integers, floats, strings (surrounded in double quotes) or function \n calls of the form '(identifier {expression}*)'.);");
	}
}
