package de.boksa.rt.rest.response.parser.customconverters;

import java.util.ArrayList;

import org.apache.commons.beanutils.Converter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

public class StringToDateTimeConverter implements Converter {
	
	private String DATE_FORMAT_PATTERNS[] = {
		// Possible date formats, see lib/RT/Date.pm in RT distribution
		"yyyy-MM-dd HH:mm:ss",				// ISO: YYYY-MM-DD hh:mm:ss
		"yyyy-MM-dd'T'HH:mm:ssz",			// W3CDTF:  YYYY-MM-DDThh:mm:ssTZD
		"EEE, dd MMM yyyy HH:mm:ss ZZZ", 	// RFC2822: Sun, 06 Nov 1994 08:49:37 +0000
		"EEE, dd MMM yyyy HH:mm:ss Z",	 	// RFC2616: Sun, 06 Nov 1994 08:49:37 GMT
		"yyyyMMdd'T'HHmmss",				// iCal: 19971024T120000
		"EEE MMM dd HH:mm:ss yyyy"			// A system default
	};

	@Override
	public Object convert(Class clazz, Object value) {
		DateTime dateTime = null;
		if (value instanceof String && !value.equals("Not set")) {
			DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();

			dateTime = builder.append(null, getParsers()).toFormatter().parseDateTime((String)value);
		} else {
			return null;
		}
		return dateTime;
	}
	
	private DateTimeParser[] getParsers() {
		ArrayList<DateTimeParser> parsers = new ArrayList<DateTimeParser>(); 
		for (String pattern : DATE_FORMAT_PATTERNS) {		
			parsers.add(DateTimeFormat.forPattern(pattern).getParser());
		}
		
		DateTimeParser[] parserArray = new DateTimeParser[parsers.size()];
		
		return parsers.toArray(parserArray);
	}

}
