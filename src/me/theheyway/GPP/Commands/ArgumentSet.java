package me.theheyway.GPP.Commands;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class ArgumentSet {
	
	List<Argument> args = new LinkedList<Argument>();
	
	public ArgumentSet() {
		
	}
	
	/**
	 * Pass a string of comma-separated types to auto-load this argument set. Example: new ArgumentSet("double,string,double");
	 * 
	 * @param types
	 */
	public ArgumentSet(String types) {
		StringTokenizer strTokenizer = new StringTokenizer(types, ",");
		while (strTokenizer.hasMoreTokens()) {
			addArgument(strTokenizer.nextToken());
		}
	}
	
	/**
	 * Manually add an argument type to this ArgumentSet.
	 * 
	 * @param type
	 */
	void addArgument(String type) {
		Argument argument = new Argument(type);
		args.add(argument);
	}
	
	String argumentTypeAtIndex(int index) {
		return args.get(index).getType();
	}
	
	private class Argument {
		
		String type;
		
		Argument(String type) {
			this.type = type;
		}
		
		private String getType() {
			return type;
		}
		
	}

}
