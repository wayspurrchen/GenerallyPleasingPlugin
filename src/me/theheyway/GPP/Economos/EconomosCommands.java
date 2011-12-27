package me.theheyway.GPP.Economos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.Strings;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Util.CommandUtil;
import me.theheyway.GPP.Util.GenUtil;
import me.theheyway.GPP.Util.SQLUtil;
import me.theheyway.GPP.Util.StringUtil;
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
	
	private void displayAccountsList(Player player, Accountant accountant) {
		player.sendMessage(ChatColor.GOLD + "Your Accounts");
		for (int i=0; i <accountant.accountCount(); i++) {
			Account account = accountant.getAccounts().get(i);
			
			double withdrawlimit = account.getWithdrawLimit();
			String withdrawString = "";
			if (withdrawlimit!=0) {
				withdrawString = "" + withdrawlimit + ChatColor.YELLOW + EconomosConstants.CURRENCY_NAME_PLURAL;
			} else {
				withdrawString = "Unlimited";
			}
			
			String string = ChatColor.GOLD + " - " + ChatColor.YELLOW + account.getAccountNumber() + ChatColor.GOLD + " ("
					+ ChatColor.WHITE + account.getRole() + ChatColor.GOLD + "): "
					+ ChatColor.WHITE + account.getBalance() + ChatColor.YELLOW +  ChatColor.GOLD + " [" + ChatColor.WHITE + withdrawString + ChatColor.GOLD + "]"
					+ " x " + ChatColor.WHITE + account.getInterest() + ChatColor.YELLOW + " per cycle";
			player.sendMessage(string);
		}
		player.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
	}
	
	/**
	 * Displays information about all of a player's accounts. This must deal with the account number string as opposed
	 * to an Account object because it needs to query additional information about the account's other users, as well
	 * as that an administrator may need to access the account of another player for information.
	 * 
	 * 
	 * @param accountNumber
	 * @param player
	 */
	private void displayAccountInfo(String accountno, Player player) {
		/*
		 * Schema returned:
		 * 
		 * 1 - id (integer)
		 * 2 - accountno (integer)
		 * 3 - user (string)
		 * 4 - role (string)
		 * 5 - withdrawlimit (double)
		 * 
		 */
		String query = "SELECT * FROM " + EconomosConstants.DB_ACCOUNTENTRIES_TABLENAME +
				" WHERE accountno=" + accountno;
		Connection conn = null;
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> roles = new ArrayList<String>();
		ArrayList<Double> withdrawlimits = new ArrayList<Double>();
		try {
			conn = SQLUtil.getConnection();
			Statement stat = conn.createStatement();
			conn.setAutoCommit(false);
			ResultSet rs = stat.executeQuery(query);
			while (rs.next()) {
				users.add(String.valueOf(rs.getString(3)));
				roles.add(String.valueOf(rs.getString(4)));
				withdrawlimits.add(rs.getDouble(5));
			}
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		double bal = AccountUtil.getAccountBalance(accountno);
		double interest = AccountUtil.getAccountInterest(accountno);
		player.sendMessage(ChatColor.GOLD + "Account #" + ChatColor.YELLOW + accountno);
		player.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
		player.sendMessage(ChatColor.GOLD + "Interest: " + ChatColor.WHITE + interest + ChatColor.YELLOW + " per cycle");
		int owner = -1;
		/*int owner = users.indexOf("owner");
		player.sendMessage(ChatColor.GOLD + " - " + ChatColor.WHITE + users.get(owner) + ChatColor.GOLD + " (" +
				ChatColor.YELLOW + roles.get(owner) + ChatColor.GOLD + ") " + ChatColor.YELLOW + " | " +
				ChatColor.GOLD + " Withdraw Limit: " + ChatColor.WHITE + " Unlimited");*/
		player.sendMessage(ChatColor.GOLD + "Account Holder(s):");
		if (users.size()>0) {
			for (int i=0; i < users.size(); i++) {
				if (i!=owner) {
					String string = ChatColor.GOLD + " - " + ChatColor.WHITE + users.get(i) + ChatColor.GOLD + " (" +
							ChatColor.YELLOW + roles.get(i) + ChatColor.GOLD + ")" + ChatColor.YELLOW + " || " +
							ChatColor.GOLD + "Withdraw Limit: " + ChatColor.WHITE;
					if (withdrawlimits.get(i)!=0) {
						string += withdrawlimits.get(i);
					} else {
						string += "Unlimited";
					}
					player.sendMessage(string);
				}
			}
		} else {
			player.sendMessage(ChatColor.GOLD + " - " + ChatColor.WHITE + "None");
		}
		player.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
	}
	
	private void displayAccountBalance(String accountNumber, Player player) {
		double bal = AccountUtil.getAccountBalance(accountNumber);
		player.sendMessage(ChatColor.GOLD + "Account: " + ChatColor.WHITE + accountNumber);
		player.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
		player.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
	}
	
	private void displayWalletBalance(Player requester, String playerName) {
		double bal = AccountUtil.getAccountant(playerName).getWalletBalance();
		requester.sendMessage(ChatColor.GOLD + playerName + "'s " + EconomosConstants.WALLET_NAME);
		requester.sendMessage(ChatColor.GOLD + "Balance: " + ChatColor.WHITE + bal + ChatColor.YELLOW + " " + EconomosConstants.CURRENCY_NAME_PLURAL);
		requester.sendMessage(ChatColor.YELLOW + Strings.DIVIDER);
	}
	
	/**
	 * Creates a new account, properly adding Account objects to any online players and creating an Account and AccountEntry pair
	 * for particular player. If playerName passed is "" or null, then a lone account is created with no owners.
	 * 
	 * @param creator
	 * @param playerName
	 * @param accountno
	 * @return
	 */
	private boolean createAccount(Player creator, String playerName, String accountno) {
		if (playerName.isEmpty() || playerName==null) {
			if (!AccountUtil.accountNoExists(accountno)) {
				if (AccountUtil.createAccountAndAccountEntry(playerName, accountno, 0, 0)) { // create the account
					Player player = GPP.server.getPlayerExact(playerName);
					if (player!=null) {
						Accountant accountant = AccountUtil.getAccountant(playerName);
						if (accountant!=null) {
							accountant.addAccount(accountno);
							accountant.setActiveAccount(accountno);
							creator.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
									+ ChatColor.YELLOW + " for " + ChatColor.WHITE + playerName + ChatColor.YELLOW + " created.");
						}
					}
					return true;
				} else {
					creator.sendMessage(ChatColor.DARK_RED + "Could not create account " + ChatColor.RED + accountno + ChatColor.DARK_RED + ".");
					return false;
				}
			} else {
				creator.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " already exists.");
				return false;
			}
		} else {
			AccountUtil.createAccount(accountno, 0.0);
			creator.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
					+ ChatColor.YELLOW + " without owner created.");
			return true;
		}
	}
	
	private boolean closeAccount(Player closer, String accountno) {
		/*
		 * This needs to check for whether any players are online who own the account and delete their Account objects
		 * from their ArrayList<Account> in their Accountant object.
		 */
		ArrayList<Player> players = AccountUtil.getAccountOnlineUsers(accountno);
		if (!players.isEmpty()) {
			for (int i=0; i < players.size(); i++) {
				Accountant accountant = AccountUtil.getAccountant(players.get(i));
				accountant.removeAccount(accountno);
			}
		}
		
		if (AccountUtil.deleteAccount(accountno)) {
			closer.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE + accountno 
					+ ChatColor.YELLOW + " closed.");
			return true;
		} else {
			closer.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno 
					+ ChatColor.DARK_RED + " could not be closed. Check logs for more information.");
			return false;
		}
	}
	
	private void transferFunds(Player transferrer, String fromAccount, String toAccount, double value) {
		AccountUtil.incrementAccountBalance(fromAccount, -value);
		AccountUtil.incrementAccountBalance(toAccount, value);
		transferrer.sendMessage(ChatColor.YELLOW + "You transferred " + ChatColor.WHITE + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL
				+ ChatColor.YELLOW + " from account " + ChatColor.WHITE + fromAccount + ChatColor.YELLOW + " to account "
				+ ChatColor.WHITE + toAccount + ChatColor.YELLOW + ".");
		ArrayList<String> receivers = AccountUtil.getAccountUsers(toAccount);
		for (int i=0; i < receivers.size(); i++) {
			Player receiverPlayer = GPP.server.getPlayer(receivers.get(i));
			if (GPP.server.matchPlayer(receivers.get(i))!=null) {
				receiverPlayer.sendMessage(ChatColor.WHITE + transferrer.getName() + ChatColor.YELLOW + " transferred "
						+ ChatColor.WHITE + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL + ChatColor.YELLOW
						+ " to account " + ChatColor.WHITE + toAccount + ChatColor.YELLOW + ".");
			}
		}
	}
	
	private void setBalance(Player setter, String accountno, double value) {
		AccountUtil.setAccountBalance(accountno, value);
		setter.sendMessage(ChatColor.YELLOW + "You have set account " + ChatColor.WHITE + accountno + ChatColor.YELLOW + "'s balance to "
				+ ChatColor.WHITE + value + ChatColor.YELLOW + ".");
	}
	
	private void depositFunds(Player depositer, String accountno, double value) {
		AccountUtil.incrementAccountBalance(accountno, value);
		AccountUtil.decrementWalletBalance(depositer.getName(), value);
		depositer.sendMessage(ChatColor.YELLOW + "You have deposited " + ChatColor.WHITE + value + ChatColor.YELLOW + " into account "
				+ ChatColor.WHITE + accountno + ChatColor.YELLOW + ".");
	}
	
	private void withdrawFunds(Player withdrawer, String accountno, double value) {
		AccountUtil.decrementAccountBalance(accountno, value);
		AccountUtil.incrementWalletBalance(withdrawer.getName(), value);
		withdrawer.sendMessage(ChatColor.YELLOW + "You have withdrawn " + ChatColor.WHITE + value + ChatColor.YELLOW + " from account "
				+ ChatColor.WHITE + accountno + ChatColor.YELLOW + ".");
	}

	
	//This method is so long. So, so, very long.
	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias,
			String[] args) {
		Player player = (Player) sender;
		String playerName = player.getName();
		
		if (EconomosConstants.ECONOMY_ENABLED) {
			
			//Balance command
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
					} else {
						String target = GenUtil.getPlayerMatchString(args[0]);
						if (target!=null) {
							if (AccountUtil.hasAccountant(target)) {
								displayWalletBalance(player, target);
								return true;
							} else {
								player.sendMessage(ChatColor.RED + "Player does not have a wallet yet (they need to rejoin).");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.RED + "Could not find a player by that name.");
							return true;
						}
					}
				} else {
					displayWalletBalance(player, playerName);
					return true;
				}
				
			//Account command
			} else if (CommandUtil.cmdEquals(command, "account")) {
				Accountant accountant = AccountUtil.guaranteeAccountant(playerName);
				if (args.length == 0) {
					if (accountant.guaranteeActiveAccount()) {
						displayAccountBalance(accountant.getActiveAccount().getAccountNumber(), player);
						return true;
					} else {
						player.sendMessage(ChatColor.YELLOW + "You do not have any accounts yet. Use " + ChatColor.WHITE + 
								"/acc create <#>" + ChatColor.YELLOW + " to make one.");
						return true;
					}
				} if (args.length >= 1) {
					
					//Make account
					if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("make")) {
						if (args.length==2) { // Two args: create account for self
							if (TypeUtil.isInteger(args[1])) {
								String accountno = args[1];
								if (!(accountno.length() > 7)) {
									if (createAccount(player, playerName, accountno)) return true;
									else return false;
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Account number must be seven digits or less.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "That is not a valid integer account number.");
								return true;
							}
						
						} else if (args.length==3) { // Three args: create account for another player
							if (player.hasPermission("gpp.economos.account.remotecreate")) {
								if (TypeUtil.isInteger(args[1])) {
									String accountNumber = args[1];
									if (!(accountNumber.length() > 7)) {
										if (StringUtil.isCleanString(args[2])) {
											String accountName = args[2];
											if (AccountUtil.createAccountAndAccountEntry(accountName, accountNumber, 0, 0)) { // create the account
												//TODO: Add autograb for accountant of players that are online
												accountant.addAccount(accountNumber);
												accountant.setActiveAccount(accountNumber);
												player.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE +
													accountNumber + ChatColor.YELLOW + " created.");
												return true;
											} else {
												player.sendMessage(ChatColor.RED + "An account by that number already exists.");
											}
										} else {
											player.sendMessage(ChatColor.DARK_RED + "Account name must be alphanumerical without spaces.");
											return true;
										}
									} else {
										player.sendMessage(ChatColor.DARK_RED + "Account number must be seven digits or less.");
										return true;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "That is not a valid integer account number.");
									return true;
								}
							}
						} else {
							player.sendMessage(ChatColor.RED + "Proper format:" + ChatColor.WHITE + " /acc create <accountnumber>");
							return true;
						}
					
					//Cancel account
					} else if (args[0].equalsIgnoreCase("close") || args[0].equalsIgnoreCase("delete")
							|| args[0].equalsIgnoreCase("cancel")) {
						if (args.length==2) {
							String accountno = args[1];
							if (AccountUtil.accountNoExists(accountno)) {
								if (accountant.hasAccountNo(accountno)) {
									if (AccountUtil.isOwner(playerName, accountno)) {
										if (AccountUtil.getAccountBalance(accountno)==0) {
											if (closeAccount(player, accountno)) {
												return true;
											} else {
												return false;
											}
										} else {
											player.sendMessage(ChatColor.DARK_RED + "You cannot close account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " while it still contains funds.");
											return true;
										}
									} else {
										player.sendMessage(Strings.ACCOUNT_NOTOWNER(accountno));
										return false;
									}
								} else {
									if (player.hasPermission("gpp.economos.account.remoteclose")) {
										if (closeAccount(player, accountno)) {
											return true;
										} else {
											return false;
										}
									} else {
										player.sendMessage(Strings.ACCOUNT_NOACCESS(accountno));
										return true;
									}
								}
							} else {
								player.sendMessage(Strings.ACCOUNT_NONEXISTENT(accountno));
								return true;
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc close <accountno>");
							return true;
						}
						
					//Deposit wallet cash into select or default account
					//TODO: Checks for active account
					} else if (args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("dep")
							|| args[0].equalsIgnoreCase("put")) {
						if (args.length==2) {
							if (TypeUtil.isDouble(args[1])) {
								double value = Double.parseDouble(args[1]);
								if (accountant.getWalletBalance()>=value) {
									if (accountant.guaranteeActiveAccount()) {
										depositFunds(player, accountant.getActiveAccount().getAccountNumber(), value);
										return true;
									} else {
										player.sendMessage(ChatColor.YELLOW + "You do not have any accounts yet. Use " + ChatColor.WHITE + 
												"/acc create <#>" + ChatColor.YELLOW + " to make one.");
										return true;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value
											+ EconomosConstants.CURRENCY_NAME_PLURAL + ChatColor.DARK_RED + " in your wallet.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
								return true;
							}
						} else if (args.length>=3) {
							if (TypeUtil.isDouble(args[1])) {
								double value = Double.parseDouble(args[1]);
								if (accountant.getWalletBalance()>=value) {
									if (TypeUtil.isInteger(args[2])) {
										String accountno = String.valueOf(args[2]);
										if (AccountUtil.accountNoExists(accountno)) {
											if (AccountUtil.isUser(playerName, accountno)) {
												depositFunds(player, accountno, value);
												return true;
											} else {
												player.sendMessage(Strings.ACCOUNT_NOACCESS(accountno));
												return true;
											}
										} else {
											player.sendMessage(Strings.ACCOUNT_NONEXISTENT(accountno));
											return true;
										}
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value
											+ EconomosConstants.CURRENCY_NAME_PLURAL + ChatColor.DARK_RED + " in your wallet.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
							return true;
						}
						
					} else if (args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("take")
							|| args[0].equalsIgnoreCase("wd")) {
						if (args.length==2) {
							if (TypeUtil.isDouble(args[1])) {
								double value = Double.parseDouble(args[1]);
								if (accountant.getAccountBalance(accountant.getActiveAccountNumber())>=value) {
									if (accountant.guaranteeActiveAccount()) {
										withdrawFunds(player, accountant.getActiveAccount().getAccountNumber(), value);
										return true;
									} else {
										player.sendMessage(ChatColor.YELLOW + "You do not have any accounts yet. Use " + ChatColor.WHITE + 
												"/acc create <#>" + ChatColor.YELLOW + " to make one.");
										return true;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value
											+ EconomosConstants.CURRENCY_NAME_PLURAL + ChatColor.DARK_RED + " in your account.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc withdraw <value> [accountno]");
								return true;
							}
						} else if (args.length>=3) {
							if (TypeUtil.isDouble(args[1])) {
								double value = Double.parseDouble(args[1]);
								if (TypeUtil.isInteger(args[2])) {
									String accountno = String.valueOf(args[2]);
									if (accountant.getAccountBalance(accountant.getActiveAccountNumber())>=value) {
										if (AccountUtil.accountNoExists(accountno)) {
											if (AccountUtil.isUser(playerName, accountno)) {
												withdrawFunds(player, accountno, value);
												return true;
											} else {
												player.sendMessage(Strings.ACCOUNT_NOACCESS(accountno));
												return true;
											}
										} else {
											player.sendMessage(Strings.ACCOUNT_NONEXISTENT(accountno));
											return true;
										}
									} else {
										player.sendMessage(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value
											+ EconomosConstants.CURRENCY_NAME_PLURAL + ChatColor.DARK_RED + " in account "
											+ ChatColor.RED + accountno);
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
							return true;
						}
						
					//Show account information
					} else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {
						if (args.length==2) {
							if (TypeUtil.isInteger(args[1])) {
								if (AccountUtil.accountNoExists(args[1])) {
									if (accountant.hasAccountNo(args[1])) {
										displayAccountInfo(args[1], player);
										return true;
									} else {
										//TODO: DECIDE ON PERMISSION
										if (player.hasPermission("")) {
											displayAccountInfo(args[1], player);
											return true;
										} else {
											player.sendMessage(ChatColor.DARK_RED + "You do not have access to that account.");
											return true;
										}
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + args[1] + ChatColor.DARK_RED + " does not exist.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "That is not a valid integer account number.");
								return true;
							}
						} else {
							if (accountant.guaranteeActiveAccount()) {
								displayAccountInfo(accountant.getActiveAccount().getAccountNumber(), player);
								return true;
							} else {
								player.sendMessage(ChatColor.YELLOW + "You do not have any accounts yet. Use " + ChatColor.WHITE + 
										"/acc create <#>" + ChatColor.YELLOW + " to make one.");
								return true;
							}
						}
					
					//Show a list of accounts
					} else if (args[0].equalsIgnoreCase("list")) {
						for (int i=0; i < accountant.accountCount(); i++) {
							displayAccountsList(player, accountant);
							return true;
						}
						
					//Set active account
					} else if (args[0].equalsIgnoreCase("setactive") || args[0].equalsIgnoreCase("active") ||
							args[0].equalsIgnoreCase("use") || args[0].equalsIgnoreCase("focus")) {
						if (args.length==2) {
							if (TypeUtil.isInteger(args[1])) {
								String accountNumber = args[1];
								if (AccountUtil.accountNoExists(accountNumber)) {
									if (accountant.hasAccountNo(accountNumber)) {
										AccountUtil.getAccountant(playerName).setActiveAccount(accountNumber);
										player.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE +
												accountNumber + ChatColor.YELLOW + " set to active.");
										return true;
									} else {
										player.sendMessage(ChatColor.DARK_RED + "You do not have access to that account.");
										return true;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountNumber + ChatColor.DARK_RED + " does not exist.");
									return true;
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "That is not a valid integer account number.");
								return true;
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "You must supply an account number to make active.");
							return true;
						}
						
					//Set account balance
					} else if (args[0].equalsIgnoreCase("setbal") || args[0].equalsIgnoreCase("set")) {
						if (player.hasPermission("gpp.economos.account.setbalance")) {
							if (args.length>2) { //Check for three args: setbal keyword, the account number, and the value to set it to
								if (TypeUtil.isInteger(args[1])) {
									String targetAccount = args[1];
									if (AccountUtil.accountNoExists(targetAccount)) {
										if (TypeUtil.isDouble(args[2])) {
											double value = Double.parseDouble(args[2]);
											setBalance(player, targetAccount, value);
											return true;
										} else {
											player.sendMessage(ChatColor.DARK_RED + "Invalid new balance number.");
											return true;
										}
									} else {
										player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED
												+ targetAccount + ChatColor.DARK_RED + " does not exist.");
										return true;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Invalid account number.");
									return true;
								}
							} else {
								if (args.length>1) {
									if (TypeUtil.isDouble(args[1])) {
										double value = Double.parseDouble(args[1]);
										if (accountant.guaranteeActiveAccount()) {
											setBalance(player, accountant.getActiveAccount().getAccountNumber(), value);
											return true;
										} else {
											player.sendMessage(ChatColor.YELLOW + "You do not have any accounts yet. Use " + ChatColor.WHITE + 
													"/acc create <#>" + ChatColor.YELLOW + " to make one.");
											return true;
										}
									} else {
										
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc setbal [account] <amount>");
									return true;
								}
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "You do not have access to that account.");
							return true;
						}
						
					//Transfer funds
					} else if (args[0].equalsIgnoreCase("transfer") || args[0].equalsIgnoreCase("pay")
							|| args[0].equalsIgnoreCase("send")) {
						if (args.length>2) {
							if (args.length>3) {
								//If transfer is 4 args: transfer keyword, transfer amount, sending account, and destination account
								if (TypeUtil.isDouble(args[1])) {
									double value = Double.parseDouble(args[1]);
									String fromAccount = args[2];
									if (AccountUtil.accountNoExists(fromAccount)) {
										if (accountant.hasAccountNo(fromAccount)) {
											String toAccount = args[3];
											if (AccountUtil.accountNoExists(fromAccount)) {
												if (value>0) {
													if (AccountUtil.getAccountBalance(fromAccount)>=value) {
														transferFunds(player, fromAccount, toAccount, value);
														return true;
													} else {
														player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + fromAccount + ChatColor.DARK_RED
																+ " does not have " + ChatColor.RED + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL
																+ ChatColor.DARK_RED + " to send to account " + ChatColor.RED + toAccount + ChatColor.DARK_RED + ".");
														return true;
													}
												} else {
													player.sendMessage(ChatColor.DARK_RED + "Transfer amount must be a valid number.");
													return true;
												}
											} else {
												player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + toAccount + ChatColor.DARK_RED + " does not exist.");
												return true;
											}
										} else {
											if (player.hasPermission("gpp.economos.account.remotetransfer")) {//Check whether player can transfer from accounts they don't own
												String toAccount = args[3];
												if (AccountUtil.accountNoExists(fromAccount)) {
													if (value>0) {
														GPP.consoleInfo(String.valueOf(AccountUtil.getAccountBalance(fromAccount)));
														if (AccountUtil.getAccountBalance(fromAccount)>=value) {
															transferFunds(player, fromAccount, toAccount, value);
															return true;
														} else {
															player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + fromAccount + ChatColor.DARK_RED
																	+ " does not have " + ChatColor.RED + value + " " + EconomosConstants.CURRENCY_NAME_PLURAL
																	+ ChatColor.DARK_RED + " to send to account " + ChatColor.RED + toAccount + ChatColor.DARK_RED + ".");
															return true;
														}
													} else {
														player.sendMessage(ChatColor.DARK_RED + "Transfer amount must be a valid number.");
														return true;
													}
												} else {
													player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + toAccount + ChatColor.DARK_RED + " does not exist.");
													return true;
												}
											} else {
												player.sendMessage(ChatColor.DARK_RED + "You do not own account " + ChatColor.RED + fromAccount + ChatColor.DARK_RED + ".");
												return true;
											}
										}
									} else {
										player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + fromAccount + ChatColor.DARK_RED + " does not exist.");
										return true;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Transfer amount must be a valid number.");
									return true;
								}
							} else {
								// If three args: transfer keyword, transfer amount, and the destination
								if (TypeUtil.isDouble(args[1])) {
									double value = Double.parseDouble(args[1]);
									String accountno = args[2];
									if (AccountUtil.accountNoExists(accountno)) {
										if (accountant.guaranteeActiveAccount()) {
											if (value>0) {
												Account playerAccount = accountant.getActiveAccount();
												if (playerAccount.getBalance()>=value) {
													transferFunds(player, playerAccount.getAccountNumber(), accountno, value);
													return true;
												} else {
													player.sendMessage(ChatColor.DARK_RED + "You do not have " + ChatColor.RED + value
															+ ChatColor.DARK_RED + " in account " + ChatColor.RED + accountant.getActiveAccount().getAccountNumber()
															+ ChatColor.DARK_RED + ".");
													return true;
												}
											} else {
												player.sendMessage(ChatColor.DARK_RED + "Transfer amount must be a valid number.");
												return true;
											}
										} else {
											player.sendMessage(ChatColor.YELLOW + "You have no account from which to transfer money! Create one with /acc create <#>.");
											return false;
										}
									} else {
										player.sendMessage(ChatColor.DARK_RED + "Account " + ChatColor.RED + accountno + ChatColor.DARK_RED + " does not exist.");
										return false;
									}
								} else {
									player.sendMessage(ChatColor.DARK_RED + "Transfer amount must be a valid number.");
									return true;
								}
							}
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc transfer <amount> [from-account] <to-account>");
							return true;
						}
						
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Invalid argument.");
						return true;
					}
				}
				
			//Pay command
			} else if(CommandUtil.cmdEquals(command, "pay")) {
				
				if (args.length == 2) {
					
					Player recip = player.getServer().getPlayer(args[0]);
					String recipName = recip.getName();
					//GPP.consoleInfo("Arg 1: " + args[0] + " Arg 2: " + args[1]);
					
					if (AccountUtil.getAccountant(recipName).hasWallet() && recip != null) {
						
						if (TypeUtil.isDouble(args[1])) {
							
							//Payable permissions check
							if (recip.hasPermission("gpp.economos.payable")) {
								
								double val = Double.valueOf(args[1]);
								if (economos.accMan.getWalletBalance(args[0]) >= val) {
									
									economos.accMan.incrementWalletBalance(args[0], val);
									recip.sendMessage(ChatColor.YELLOW + "You received " + ChatColor.WHITE + val + " " + ChatColor.YELLOW
											 + (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
											 + " from " + ChatColor.WHITE + player.getDisplayName() + ChatColor.YELLOW + ".");
									economos.accMan.incrementWalletBalance(player.getName(), -val);
									
									player.sendMessage(ChatColor.YELLOW + "You gave " + ChatColor.WHITE + val + " " + ChatColor.YELLOW
											 + (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
											 + " to " + ChatColor.WHITE + recip.getDisplayName() + ChatColor.YELLOW + ".");
									
								} else player.sendMessage(ChatColor.RED + "You don't have " + ChatColor.YELLOW + val + " "
								 + (val != 1 ? EconomosConstants.CURRENCY_NAME_PLURAL : EconomosConstants.CURRENCY_NAME_SINGULAR)
								 + ChatColor.RED + " to send.");
								
							} else {
								player.sendMessage(ChatColor.DARK_RED + recip.getDisplayName() + " does not have permission to be paid. It's a cruel world out there.");
								return true;
							}
							
						} else {
							player.sendMessage(ChatColor.DARK_RED + "Must enter a numeric value.");
							return true;
						}
						
					} else {
						player.sendMessage(ChatColor.DARK_RED + "Could not find a player by that name.");
						return true;
					}
					
				} else return false;
			
			//Admin set balance command
			} else if (CommandUtil.cmdEquals(command, "setbalance")) {
				
				if (args.length == 2) {
					String recipName = GenUtil.getPlayerMatchString(args[0]);
					if (recipName != null) {
						
						if (TypeUtil.isDouble(args[1])) {
							
								double val = Double.valueOf(args[1]);
								AccountUtil.getAccountant(recipName).setWalletBalance(val);
								player.sendMessage(ChatColor.YELLOW + "You set " + ChatColor.WHITE + recipName + ChatColor.YELLOW
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
