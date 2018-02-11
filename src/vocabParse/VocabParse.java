package vocabParse;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

public class VocabParse {

	private static Options options = new Options();

	public static void main(String[] args) throws Exception {
		CommandLine commandLine;
		options.addOption("h", "help", false, "Show help.");
		
		Option folderOption = Option.builder("f")
	            .longOpt("folder")
	            .required(false)
	            .desc("The folder containing the vocabulary files.")
	            .hasArg() 
	            .build();
		options.addOption(folderOption);
		
		CommandLineParser parser = new DefaultParser();

		try {

			commandLine = parser.parse(options, args,true);

			if (commandLine.hasOption("h")) {
				help();
			}

			if (commandLine.hasOption("f")) {
				Parser txtParser = new  Parser();
				txtParser.concepts(commandLine.getOptionValue("f")+"/concept.csv");
				txtParser.concepts(commandLine.getOptionValue("f")+"/drug_strength.csv");
				txtParser.concepts(commandLine.getOptionValue("f")+"/concept_relationship.csv");	    	
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			help();
		}

	} 

	private static void help() {

		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("VocabParser", options);
		System.exit(0);

	}
}
