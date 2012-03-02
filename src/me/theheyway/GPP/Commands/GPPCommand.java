package me.theheyway.GPP.Commands;

import java.util.LinkedList;
import java.util.List;

public class GPPCommand {
	
	private String name = ""; //The command's name
	private String rootPerm = ""; //Root permission String for this command
	private List<ArgumentSet> argumentSets = new LinkedList<ArgumentSet>();
	
	public GPPCommand(String name, String rootPermission) {
		this.name = name;
	}
	
	/**
	 * Add an ArgumentSet object to the GPPCommand's ArgumentSet[] array.
	 * 
	 * @param set
	 */
	public void addArgumentSet(ArgumentSet set) {
		argumentSets.add(set);
	}
	
	/**
	 * Creates an ArgumentSet object with the passed comma-tokenized type String and then adds it
	 * to the GPPCommand's ArgumentSet[] array.
	 * 
	 * @param set
	 */
	public void addArgumentSet(String string) {
		ArgumentSet argSet = new ArgumentSet(string);
		argumentSets.add(argSet);
	}
	
	public String getName() {
		return name;
	}

}
