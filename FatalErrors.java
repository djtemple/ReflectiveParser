package parser;

public class FatalErrors {
	/**
	 * Error for unrecognized long command-line qualifier
	 * @param input - command-line argument that caused the error
	 * prints synopsis
	 */
	public static void unrecognizedLongQualifier(String input){
		System.err.println("Unrecognized qualifier '" + input + "'");
		Interpreter.synopsis();
		System.exit(-1);
	}
	
	/**
	 * Error for unrecognized short command-line qualifier
	 * @param input - command-line argument that caused the error
	 * prints synopsis
	 */
	public static void unrecognizedQualifier(String input){
		char c;
		if ((input.charAt(1) == 'h') || (input.charAt(1) == 'v') || (input.charAt(1) == '?'))
			c = input.charAt(2);
		else
			c = input.charAt(1);
		System.err.println("Unrecognized qualifier '" + c + "' in '" + input + "'");
		Interpreter.synopsis();
		System.exit(-1);
	}
	/** 
	 * Error when help qualifier is used along with other command-line arguments
	 * prints synopsis
	 */
	public static void invalidHelpQualifier(){
		System.err.println("Qualifier '--help' (-h, -?) should not appear with any command-line arguments");
		Interpreter.synopsis();
		System.exit(-4);		
	}
	/**
	 * Error when too many command-line arguments are used
	 * prints synopsis
	 */
	public static void invalidNumberOfArgs(){
		System.err.println("This program takes at most two command-line arguments");
		Interpreter.synopsis();
		System.exit(-2);
	}
	/**
	 * Error when arguments are in an invalid order (where a valid order is {qualifiers}* jarfile class
	 * prints synopsis
	 */
	public static void invalidArgOrder(){
		System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
		Interpreter.synopsis();
		System.exit(3);
	}
}


