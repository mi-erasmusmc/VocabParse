package vocabParse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.univocity.parsers.conversions.FormattedConversion;
import com.univocity.parsers.conversions.ObjectConversion;

public class myDateConversion extends ObjectConversion<String> implements FormattedConversion<SimpleDateFormat> {

	private final String  toFormat;
	private final String  fromFormat;
	
	/**
	 * Defines a conversion from String to {@link java.util.Date} using a sequence of acceptable date patterns.
	 * This constructor assumes the output of a conversion should be null when input is null
	 *
	 * @param locale              the {@link Locale} that determines how the date mask should be formatted.
	 * @param valueIfStringIsNull default Date value to be returned when the input String is null. Used when {@link ObjectConversion#execute(String)} is invoked.
	 * @param valueIfObjectIsNull default String value to be returned when a Date input is null. Used when {@link DateConversion#revert(Date)} is invoked.
	 * @param dateFormats         list of acceptable date patterns The first pattern in this sequence will be used to convert a Date into a String in {@link DateConversion#revert(Date)}.
	 */
	public myDateConversion(Locale locale, String fromFormat, String toFormat) {
		this.toFormat = toFormat;
		this.fromFormat = fromFormat;
	}
	
	/**
	 * Returns a new instance of {@link DateConversion}
	 *
	 * @param dateFormats list of acceptable date patterns. The first pattern in this sequence will be used to convert a Date into a String in {@link DateConversion#revert(Date)}.
	 *
	 * @return the new instance of {@link DateConversion} created with the given parameters.
	 */
	public static myDateConversion toMyDate(String fromFormat, String toFormat) {
		return new myDateConversion(Locale.getDefault(), fromFormat, toFormat);
	}
	
	public String execute(String arg0) {
		String result = "";
		try {
			Date date = new SimpleDateFormat(this.fromFormat).parse(arg0);
			SimpleDateFormat formatter = new SimpleDateFormat(toFormat);
			result = formatter.format(date);			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	protected String fromString(String arg0) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public SimpleDateFormat[] getFormatterObjects() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
