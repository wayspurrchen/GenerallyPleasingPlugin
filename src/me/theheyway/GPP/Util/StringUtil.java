package me.theheyway.GPP.Util;

public class StringUtil {
	
	public static String alphanumericize(String s) {
		
		String good = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		String result = "";
		
		for ( int i = 0; i < s.length(); i++ ) {
			if ( good.indexOf(s.charAt(i)) >= 0 ) result += s.charAt(i);
		}
		
		return result;
	}
	
	public static boolean isCleanString(String s) {
		
		String good = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		boolean clean = true;
		
		for ( int i = 0; i < s.length(); i++ ) {
			if ( good.indexOf(s.charAt(i)) >= 0 ) {
				continue;
			} else {
				clean = false;
				break;
			}
		}
		
		return clean;
	}
}
