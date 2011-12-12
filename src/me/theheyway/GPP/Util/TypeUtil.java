package me.theheyway.GPP.Util;

public class TypeUtil {
    
    public static boolean isInteger(String input)  
    {
       try  { Integer.parseInt(input); return true; }
       catch( NumberFormatException e ) { return false; }
    }
    
    public static boolean isDouble(String input)  
    {
       try  { Double.parseDouble(input); return true; }
       catch( NumberFormatException e ) { return false; }
    }

}
