package group_a7_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.gatech.SimDriver;

public class FileMain {
	private static final String INPUT_FILE_TOKEN="-input:";
	private static final String CONFIG_PATH_TOKEN="-config:";

	public static void main(String[] args) throws IOException {
		  System.out.println("Mass Transit Simulation System using file source Starting...");
		  String inputScriptFileName = null;
		  for(String arg:args) {
			  //System.out.printf("arg %s\n",arg);
			  if(arg.startsWith(INPUT_FILE_TOKEN)) {
				  inputScriptFileName = arg.substring(INPUT_FILE_TOKEN.length()).trim();
			  }
			  if(arg.startsWith(CONFIG_PATH_TOKEN)) {
				  FileProps.SetConfigPath(arg.substring(CONFIG_PATH_TOKEN.length()).trim());
			  }
		  }
		  	
		System.out.printf("url: %s\n",FileProps.get("connectionurl"));

		  
		  SimDriver commandInterpreter = new SimDriver();
		  

		  if(inputScriptFileName==null) {
			  printUsage();
			  System.exit(0);
		  }
		  File inFile = new File(inputScriptFileName);
		  if(!inFile.exists() || !inFile.isFile()) {
			  System.out.printf("ERROR: %s is an invalid input file\n",inputScriptFileName);
			  printUsage();
			  System.exit(0);
		  }

		  System.out.printf("\tinput:%s\n",inputScriptFileName);  

	      BufferedReader br = new BufferedReader(new FileReader(inFile));
	      String line = br.readLine();
	      while(line!=null) {
	    	  System.out.printf("%s\n",line);
	    	  commandInterpreter.processCommand(line);
	    	  line = br.readLine();
	      }	   
	      br.close();
	}

	public static  void printUsage() {
		String message = "Arguments:\n"
				+ "\t-input:<input file> - relative or full path of input file - REQUIRED\n"
				+ "\nExample:\n"
				+ "\t1)\tjava -cp:./mts.jar group_a7_8.FileMain -input:./simple_test.txt\n";
		System.out.printf("%s\n", message);
	}
}
