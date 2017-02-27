package parser;

public class FatalErrors {
	
	public static void unrecognizedQualifier(String input){
		System.err.println("Unrecognized qualifier " + input);
		Interpreter.synopsis();
		System.exit(-1);
	}

	public static void unrecognizedLongQualifier(String input){
		System.err.println("Unrecognized qualifier '" + input.charAt(1) + "' in '" + input + "'");
		Interpreter.synopsis();
		System.exit(-1);
	}
	
	public static void invalidHelpQualifier(){
		System.err.println("Qualifier '--help' (-h, -?) should not appear with any command-line arguments");
		Interpreter.synopsis();
		System.exit(-4);		
	}

	public static void invalidNumberOfArgs(){
		System.err.println("This program takes at most two command-line arguments");
		Interpreter.synopsis();
		System.exit(-2);
	}
	public static void invalidArgOrder(){
		System.err.println("This program requires a jar file as the first command line argument (after any qualifiers).");
		Interpreter.synopsis();
		System.exit(3);
	}
}
