package me.theheyway.GPP.Economos.Executors;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import me.theheyway.GPP.Executor;
import me.theheyway.GPP.Secretary;
import me.theheyway.GPP.Strings;
import me.theheyway.GPP.Economos.AccountUtil;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.NoMorePagesException;
import me.theheyway.GPP.Exceptions.NotIntegerException;
import me.theheyway.GPP.Util.Arguments;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WalletExecutor extends Executor {
	
	public static void displayWealthiest(Player executor, int count) throws GPPException, SQLException {
		HashMap<String,Double> pairings = AccountUtil.getTopWalletPairings(count);

		Secretary secretary = new Secretary(executor);
		secretary.addLine(ChatColor.GOLD + "Top " + count + " Richest Wallets");
		
		int i = 1;
		for (Map.Entry<String, Double> entry : pairings.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    secretary.addLine(ChatColor.GOLD + " " + i + ". " + ChatColor.YELLOW + key + ChatColor.GOLD
		    		+ " [" + ChatColor.WHITE + value + ChatColor.GOLD + "]");
		    i++;
		}
		secretary.addLine(ChatColor.YELLOW + Strings.DIVIDER);
		
		secretary.sendPage();
	}
	
	public static void displayWalletBalance(Player executor, String targetName) throws NumberFormatException, SQLException {
		double bal = AccountUtil.getWalletBalance(targetName);
		String name = executor.getName().equals(targetName) ? "Your" : targetName + "'s"; //Prettiness Inc., 2011
		
		Secretary secretary = new Secretary(executor);
		secretary.addLine(ChatColor.GOLD + name + " " + EconomosConstants.WALLET_NAME);
		secretary.addLine(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
		secretary.addLine(ChatColor.YELLOW + Strings.DIVIDER);
		secretary.simplePrint();
	}
	
}
