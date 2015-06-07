package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;

public class SplitLine {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	public static String extractURL(String line) {
		String[] tokens = line.split(" ");
		for(int i=0; i<tokens.length; i++){
	        log.debug(tokens[i]);
		}
		return tokens[1];
	}
	
}
