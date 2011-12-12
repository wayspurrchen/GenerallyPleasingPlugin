package me.theheyway.GPP.Util;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class CommandUtil {

    public static boolean commandAliasMatch(String[] array, Command command) {
    	for (int i=0;i<array.length;i++) {
			if (array[i].equalsIgnoreCase(command.getName())) return true;
		}
		return false;
    }
    
    public static boolean cmdEquals(Command command, String string) {
		if (command.getName().equalsIgnoreCase(string)) return true;
		return false;
    }
    
}
