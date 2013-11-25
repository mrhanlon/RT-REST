package de.boksa.rt.rest.response.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;

import de.boksa.rt.rest.RTRESTResponse;

public class RTParser {
    
    private static final Logger logger = Logger.getLogger(RTParser.class);
    
	private static RTParser SINGLETON = new RTParser();

	private RTParser() {}

	public static RTParser getInstance() {
		return SINGLETON;
	}

	private static final String DELIMITER = "\n\n--\n\n";
	
	private static final String DELIMITER_LINES = "\n";
	
	private static final Pattern PATTERN_START_FIELD = Pattern.compile("^(\\w+?): ?(.*)");
	
	// TODO this should be implemented at some point
	// private static final Pattern CUSTOM_FIELD = Pattern.compile("^CF.\\{(.*)\\}: ?(.*)");
	
	private static final Pattern COMMENT_LINE = Pattern.compile("^#.*");

	public List<Map<String, String>> parseResponse(RTRESTResponse response) {
      List<Map<String, String>> resultData = new LinkedList<Map<String, String>>();
      for (String responseString : response.getBody().split(DELIMITER)) {
          resultData.addAll(processResponseLine(responseString));
      }

      return resultData;
	}

	private List<Map<String, String>> processResponseLine(String responseBody) {
	    if (logger.isDebugEnabled()) {
	        logger.debug("Response body:\n" + responseBody);
	    }
	    
		Map<String, String> responseData = new HashMap<String, String>();
		List<Map<String, String>> resultData = new LinkedList<Map<String, String>>();

		String fieldName = null;
		StringBuilder tmp = new StringBuilder();
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
