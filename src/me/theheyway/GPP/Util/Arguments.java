package me.theheyway.GPP.Util;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.theheyway.GPP.GPP;

public class Arguments {
	
	ArrayList<String> args;
	
	public Arguments(String[] inArgs) {
		args = new ArrayList<String>(); //initialized to 10; doubt there will ever be more than 10 arguments
		for (int i=0; i < inArgs.length; i++) {
			args.add(inArgs[i]);
		}
	}
	
	public int size() {
		return args.size();
	}
	
	public String getString(int index) {
		//if (index<args.size()) { //Checks that index is less than args.size(), which will meet the proper bounds given that the index call would be size - 1 anyway
			return args.get(index);
		//} else return null; //This may actually be better for debugging, without these two lines
	}
	
	public boolean isDouble(int index) {
		return TypeUtil.isDouble(args.get(index));
	}
	
	public boolean isInt(int index) {
		return TypeUtil.isInteger(args.get(index));
	}
	
	public double getDouble(int index) {
		return Double.parseDouble(args.get(index));
	}
	
	public int getInt(int index) {
		return Integer.parseInt(args.get(index));
	}
	
	public Player getPlayerExact(int index) {
		return GPP.server.getPlayerExact(args.get(index));
	}
	
	public Player getPlayerMatch(int index) {
		return GenUtil.getPlayerMatch((args.get(index)));
	}
	
	public String getPlayerNameExact(int index) {
		return GPP.server.getPlayerExact(args.get(index)).getName();
	}
	
	public String getPlayerNameMatch(int index) {
		return GenUtil.getPlayerMatchString((args.get(index)));
	}
	

}
