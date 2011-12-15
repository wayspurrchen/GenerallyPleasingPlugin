package me.theheyway.GPP.Economos;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.TypeUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EconomosCommands implements CommandExecutor {
	
	static GPP plugin;
	static Economos economos;
	
	EconomosCommands(GPP plugin, Economos economos) {
		this.plugin = plugin;
		this.economos = economos;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias,
			String[] args) {
		Player player = (Player) sender;
		String playerName = player.getName();
		
		if (EconomosConstants.ECONOMY_ENABLED) {
			
			if (CommandUtil.cmdEquals(command, "balance")) {
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("top")) {
						if (player.hasPermission("economos.economy.top")) {
							if (args.length==2) {
								if (TypeUtil.isInteger(args[1])) ; //displayWealthiest(player, Integer.parseInt(args[1]));
								else return false;
							} else {
								//displayWealthiest(player, 5);
								return true;
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don't have permission to view the wealthiest players.");
							return true;
						}
					} else if (AccountManager.hasAccountObject((args[0]))) {
						player.sendMessage(ChatColor.YELLOW + "--------------");
						player.sendMessage(ChatColor.WHITE + args[0] + ChatColor.GOLD + "'s "
								+ EconomosConstants.WALLET_NAME + ": "
								+ ChatColor.WHITE + economos.accMan.getWalletBalance(args[0]));
						player.sendMessage(ChatColor.YELLOW + "--------------");
						return true;
					} else {
						player.sendMessage(ChatColor.RED + "Could not find a player by that name.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.YELLOW + "--------------");
					player.sendMessage(ChatColor.GOLD + EconomosConstants.WALLET_NAME + ": "
						+ ChatColor.WHITE + AccountManager.getAccount(playerName).getWalletBalance());
					player.sendMessage(ChatColor.YELLOW + "--------------");
					return true;
				}
				
			} else if (CommandUtil.cmdEquals(command, "account")) {
				Account playerAccount = AccountManager.getAccount(playerName);
				if (args.length == 0) {
					if (playerAccount.hasAccount()) {
						String accountNumber = playerAccount.getActiveAccount();
						double bal = economos.accMan.getAccountBalance(accountNumber);
						player.sendMessage(ChatColor.YELLOW + "--------------");
						player.sendMessage(ChatColor.GOLD + "Account " + accountNumber + ": "
							+ ChatColor.WHITE + bal);
						player.sendMessage(ChatColor.YELLOW + "--------------");
						return true;
					} else if (AccountManager.hasAccountObject(playerName)) {
						playerAccount.setActiveAccount(playerAccount.getAnyOwnedAccount());
						String accountNumber = playerAccount.getActiveAccount();
						double bal = economos.accMan.getAccountBalance(accountNumber);
						player.sendMessage(ChatColor.YELLOW + "--------------");
						player.sendMessage(ChatColor.GOLD + "Account " + accountNumber + ": "
							+ ChatColor.WHITE + bal);
						player.sendMessage(ChatColor.YELLOW + "--------------");
						return true;
					} else {
						player.sendMessage(ChatColor.YELLOW + "You do not have any accounts yet. Use /acc create <accountnumber> to make one.");
					}
				} if (args.length >= 1) {
					
					if (args[0].equals("create") || args[0].equals("make")) {
						if (args.length==2) {
							if (TypeUtil.isInteger(args[1])) {
								String accountNumber = args[1];
								if (!(accountNumber.length() > 7)) {
									AccountManager.getAccount(playerName).createAccount(accountNumber);
									player.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE +
											accountNumber + ChatColor.YELLOW + " created.");
									AccountManager.getAccount(playerName).setActiveAccount(accountNumber);
									return true;
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Account number must be seven digits or less.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "That is not a valid integer account number.");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.WHITE + "/acc create <accountnumber>");
							return true;
						}
						
					} else if (args[0].equals("setactive") || args[0].equals("use") || args[0].equals("focus")) {
						if (args.length==2) {
							if (TypeUtil.isInteger(args[1])) {
								String accountNumber = args[1];
								AccountManager.getAccount(playerName).setActiveAccount(accountNumber);
								player.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE +
										accountNumber + ChatColor.YELLOW + " set to active.");
								return true;
							} else {
								player.sendMessage(ChatColor.DARK_RED + "That is not a valid integer account number.");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "You must supply an account number to make active.");
							return true;
						}
					}
				}
				
			} else if(CommandUtil.cmdEquals(command, "pay")) {
				
				if (args.length == 2) {
					
					Player recip = player.getServer().getPlayer(args[0]);
					String recipName = recip.getName();
					//GPP.consoleInfo("Arg 1: " + args[0] + " Arg 2: " + args[1]);
					
					if (AccountManager.getAccount(recipName).hasWallet() && recip != null) {
						
						if (TypeUtil.isDouble(args[1])) {
							
							if (recip.hasPermission("economos.economy.payable")) {
								
								double val = Double.valueOf(args[1]);
								if (economos.accMan.getWalletBalance(args[0]) >= val) {
									
									economos.accMan.incrementIndividualBalance(args[0], val);
									recip.sendMessage(ChatColor.YELLOW + "You received " + ChatColor.WHITE + val + " " + ChatColor.YELLOW
											 + (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
											 + " from " + ChatColor.WHITE + player.getDisplayName() + ChatColor.YELLOW + ".");
									economos.accMan.incrementIndividualBalance(player.getName(), -val);
									
									player.sendMessage(ChatColor.YELLOW + "You gave " + ChatColor.WHITE + val + " " + ChatColor.YELLOW
											 + (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
											 + " to " + ChatColor.WHITE + recip.getDisplayName() + ChatColor.YELLOW + ".");
									
								} else player.sendMessage(ChatColor.RED + "You don't have " + ChatColor.YELLOW + val + " "
								 + (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
								 + ChatColor.RED + " to send.");
								
							} else player.sendMessage(ChatColor.RED + recip.getDisplayName() + " does not have permission to be paid. It's a cruel world out there.");
							
						} else player.sendMessage(ChatColor.RED + "Must enter a numeric value.");
						
					} else player.sendMessage(ChatColor.RED + "Could not find a player by that name.");
					
				} else return false;
				
			} else if (CommandUtil.cmdEquals(command, "setbalance")) {
				
				if (args.length == 2) {
					
					String recipName = args[0];
					
					if (GenUtil.getPlayerMatchString(recipName) != null) {
							
						if (TypeUtil.isDouble(args[1])) {
							
								double val = Double.valueOf(args[1]);
								AccountManager.getAccount(recipName).setWalletBalance(val);
								player.sendMessage(ChatColor.YELLOW + "You set " + ChatColor.WHITE + args[0] + ChatColor.YELLOW
										+ "'s balance to " + ChatColor.WHITE + val + " "
										+ (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
										+ ChatColor.YELLOW + ".");
								
						} else player.sendMessage(ChatColor.RED + "Must enter a numeric value.");
					
					} else player.sendMessage(ChatColor.RED + "Could not find a player by that name.");
					
				} else return false;
				
			}
			
		} else player.sendMessage(ChatColor.GRAY + "Economos Economy module is not enabled on this server. " +
					"Don't ask me why they even have this plugin installed.");
		
		return true;
	}
	

}
