package vocabParse;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;

public class Parser {

	/**
	 * Creates a reader for a resource in the relative path
	 *
	 * @param relativePath relative path of the resource to be read
	 *
	 * @return a reader of the resource
	 * @throws FileNotFoundException 
	 */
	public Reader getReader(String filename) throws FileNotFoundException {
		try {
			InputStream inputStream       = new FileInputStream(filename);
			return new InputStreamReader(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Unable to read input", e);
		}
	}

	/**
	 * Modifies the values of an array of Strings to make line separator characters `visible` by replacing
	 * them with their character escapes
	 *
	 * @param input the input array whose values will have line separators replaced by escape sequences.
	 *
	 * @return the modified, input array
	 */
	public String[] displayLineSeparators(String[] input) {
		for (int i = 0; i < input.length; i++) {
			if (input[i] != null) {
				input[i] = input[i].replaceAll("\\n", "\\\\n");
				input[i] = input[i].replaceAll("\\r", "\\\\r");
			}
		}
		return input;

	}

	/**
	 * Prints and validates rows in a map consisting of entity names and their respective rows.
	 *
	 * @param allRows a map of entity names and their rows.
	 */
	public final void printAndValidate(Map<String, List<String[]>> allRows) {
		for (Map.Entry<String, List<String[]>> e : allRows.entrySet()) {
			println("Rows of '" + e.getKey() + "'\n-----------------------");
			for (String[] row : e.getValue()) {
				println(Arrays.toString(row));
			}
		}
		//printAndValidate();
	}

	public static int countLines(String filename) throws IOException {
	    InputStream is = new BufferedInputStream(new FileInputStream(filename));
	    try {
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        boolean empty = true;
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            for (int i = 0; i < readChars; ++i) {
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }
	        return (count == 0 && !empty) ? 1 : count;
	    } finally {
	        is.close();
	    }
	}

	public void concepts(String filename) throws Exception {
		
		Path p = FileSystems.getDefault().getPath(filename);
		
		ProgressBar bar = new ProgressBar();
		int nrRows = countLines(filename);

		TsvWriterSettings settings = new TsvWriterSettings();
		// Sets the character sequence to write for the values that are null.
		settings.setNullValue("");

		//Changes the comment character to -
		settings.getFormat().setComment('-');

		// Sets the character sequence to write for the values that are empty.
		settings.setEmptyValue("!");

		// writes empty lines as well.
		settings.setSkipEmptyLines(false);
		
		String outputFilename = p.getParent()+"/parsed/"+p.getFileName();
		println("Parsing: "+p.getFileName());
		
		Files.createDirectories(Paths.get(p.getParent()+"/parsed"));
		OutputStream outputStream       = new FileOutputStream(outputFilename);
		Writer outputWriter = new OutputStreamWriter(outputStream);

		// Creates a writer with the above settings;
		TsvWriter writer = new TsvWriter(outputWriter, settings);

		// ObjectRowProcessor converts the parsed values and gives you the resulting row.
		ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
			@Override
			public void rowProcessed(Object[] row, ParsingContext context) {
				//here is the row. Let's just print it.
				//println(Arrays.toString(row).toString());
				bar.update((int) context.currentRecord(),nrRows);
				if (context.currentRecord()==1) {
					writer.writeRow(context.headers());
				}
				if (!row.toString().isEmpty()) {					
					writer.writeRow(row);
				}
			}

		};

		rowProcessor.convertFields(myDateConversion.toMyDate("yyyyMMdd","yyyy-MM-dd")).set("valid_start_date");
		rowProcessor.convertFields(myDateConversion.toMyDate("yyyyMMdd","yyyy-MM-dd")).set("valid_end_date");

		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.getFormat().setLineSeparator("\n");
		parserSettings.setDelimiterDetectionEnabled(true);
		parserSettings.setProcessor(rowProcessor);
		parserSettings.setHeaderExtractionEnabled(true);

		CsvParser parser = new CsvParser(parserSettings);
		parser.parse(getReader(filename));

		writer.close();
	}

	public void print(String text) {
		System.out.print(text);
	}

	public void println(String text) {
		System.out.print(text+'\n');
	}

}
