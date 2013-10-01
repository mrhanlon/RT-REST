package de.boksa.rt.rest.response.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;

import de.boksa.rt.rest.RTRESTResponse;

public class RTParser {
	private static RTParser SINGLETON = new RTParser();

	private RTParser() {}

	public static RTParser getInstance() {
		return SINGLETON;
	}

	private static final String DELIMITER = "\n\n--\n\n";
	private static final String DELIMITER_LINES = "\n";
	private static final Pattern PATTERN_START_FIELD = Pattern.compile("^(\\w+?): ?(.*)");
	//private static final Pattern CUSTOM_FIELD = Pattern.compile("^CF.\\{(.*)\\}: ?(.*)"); TODO this should be implemented at some point
	private static final Pattern COMMENT_LINE = Pattern.compile("^#.*");

	public List<Map<String, String>> parseResponse(RTRESTResponse response) {
		if (response.getBody().split(DELIMITER).length > 1) {
			return processMultiLine(response.getBody());
		} else {
			return processSingleLine(response.getBody());
		}
	}
	
	private List<Map<String, String>> processMultiLine(String responseBody) {
		List<Map<String,String>> resultData = new LinkedList<Map<String,String>>();
		for (String responseString : responseBody.split(DELIMITER)) {	
			resultData.addAll(processSingleLine(responseString));
		}
		
		return resultData;
	}
	
	private List<Map<String, String>> processSingleLine(String responseBody) {

			Map<String,String> responseData = new HashMap<String,String>();
			List<Map<String,String>> resultData = new LinkedList<Map<String,String>>();

			String fieldName = null;
			StringBuffer tmp = new StringBuffer();
			for (String responseLine : responseBody.split(DELIMITER_LINES)) {
				Matcher m = PATTERN_START_FIELD.matcher(responseLine);
				if (m.matches()) {
					if (fieldName != null) {
						if (COMMENT_LINE.matcher(tmp.toString()).matches()) {
							String id = tmp.toString().replaceAll("#.*\\(.*\\)", "");
							tmp.setLength(0);
							tmp.append(id);
						}
						responseData.put(WordUtils.uncapitalize(fieldName), tmp.toString().replaceFirst("<br />$", ""));
						tmp.setLength(0);
					}
					
					fieldName = m.group(1);
					tmp.append(m.group(2));
				} else {
					tmp.append(responseLine + "<br />");
				}
			}	
			responseData.put(WordUtils.uncapitalize(fieldName), tmp.toString());
			resultData.add(responseData);
			return resultData;
	}
}

