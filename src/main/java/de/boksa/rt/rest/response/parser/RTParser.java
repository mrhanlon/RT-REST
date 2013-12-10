package de.boksa.rt.rest.response.parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
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
	
	private static final Pattern PATTERN_START_FIELD = Pattern.compile("^(\\w+?):\\s*(.*)");
	
	private static final Pattern WHITESPACE_PREFIX = Pattern.compile("^(\\s*)(.*)");
	
	// TODO this should be implemented at some point
	// private static final Pattern CUSTOM_FIELD = Pattern.compile("^CF.\\{(.*)\\}: ?(.*)");
	
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
		String linePad = null;
		StringBuilder fieldContent = new StringBuilder();
		for (String responseLine : responseBody.split(DELIMITER_LINES)) {
		    
		    if (responseLine.length() == 0) {
		        continue;
		    }
		    
			Matcher m = PATTERN_START_FIELD.matcher(responseLine);
			if (m.matches()) {
			    // found a field
				if (fieldName != null) {
				    // had matched a previous field, clear the queue
				    String content = fieldContent.toString();
				    
					// remove leading/trailing newlines
					if (content.startsWith("\n") || content.endsWith("\n")) {
					    content = content.replaceFirst("^\n+", "").replaceFirst("\n+$", "");
					}
					
					responseData.put(WordUtils.uncapitalize(fieldName), content);
					
				}
				fieldContent.setLength(0);
				
				fieldName = m.group(1);
				fieldContent.append(m.group(2));
				linePad = null;
			} else if (fieldName != null) {
			    // Multiline field content; RT left-pads multiline fields with varying whitespace, but the amount
			    // is the same for each line in the field
			    if (linePad == null) {
			        Matcher prefix = WHITESPACE_PREFIX.matcher(responseLine);
			        if (prefix.matches()) {
			            linePad = prefix.group(1);
			        } else {
			            linePad = "";
			        }
			    }
			    if (responseLine.startsWith(linePad)) {
			        responseLine = responseLine.replaceFirst(linePad, "");
			    }
				fieldContent.append("\n" + StringUtils.stripEnd(responseLine, null));
			}
		}
		responseData.put(WordUtils.uncapitalize(fieldName), fieldContent.toString());
		resultData.add(responseData);
		return resultData;
	}
}
