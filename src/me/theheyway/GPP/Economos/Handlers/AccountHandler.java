package me.theheyway.GPP.Economos.Handlers;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.theheyway.GPP.Stage;
import me.theheyway.GPP.Economos.Account;
import me.theheyway.GPP.Economos.AccountUtil;
import me.theheyway.GPP.Economos.Accountant;
import me.theheyway.GPP.Economos.Executors.AccountExecutor;
import me.theheyway.GPP.Exceptions.GPPException;
import me.theheyway.GPP.Exceptions.InvalidArgumentException;
import me.theheyway.GPP.Exceptions.NoPermissionException;
import me.theheyway.GPP.Exceptions.NoPlayerException;
import me.theheyway.GPP.Exceptions.NotAlphanumericException;
import me.theheyway.GPP.Exceptions.NotDoubleException;
import me.theheyway.GPP.Exceptions.NotIntegerException;
import me.theheyway.GPP.Exceptions.Accounts.AccountAlreadyExistsException;
import me.theheyway.GPP.Exceptions.Accounts.AccountDoesNotExistException;
import me.theheyway.GPP.Exceptions.Accounts.AccountIntegerCountException;
import me.theheyway.GPP.Exceptions.Accounts.AccountNoAccessException;
import me.theheyway.GPP.Exceptions.Accounts.AccountNotEmptyException;
import me.theheyway.GPP.Exceptions.Accounts.AccountNotEnoughFundsException;
import me.theheyway.GPP.Exceptions.Accounts.AccountNotEnoughFundsTransferException;
import me.theheyway.GPP.Exceptions.Accounts.AccountNotOwnerException;
import me.theheyway.GPP.Exceptions.Accounts.AccountRequiredException;
import me.theheyway.GPP.Exceptions.Accounts.WalletNotEnoughFundsException;
import me.theheyway.GPP.Util.Arguments;
import me.theheyway.GPP.Util.StringUtil;
import me.theheyway.GPP.Util.TypeUtil;

public class AccountHandler {

	//FLATTEN EVERYTHING!
	public static boolean direct(Player executor, Arguments args) throws NumberFormatException, SQLException, GPPException {
		String executorName = executor.getName();
		
		Accountant accountant = AccountUtil.guaranteeAccountant(executorName);
		
		if (args.size() == 0 ||
				((args.getString(0).equalsIgnoreCase("bal") || args.getString(0).equalsIgnoreCase("balance") && args.size()==1))) {
			if (!accountant.guaranteeActiveAccount()) throw new AccountRequiredException();
			
			AccountExecutor.displayAccountBalance(accountant.getActiveAccountNumber(), executor);
		
		} else if (args.size() >= 1) {
			
			//Make account
			if (args.getString(0).equalsIgnoreCase("create") || args.getString(0).equalsIgnoreCase("make")) {
				
				/*if (createAccount()) return true;
				else return false;*/
				if (args.size()==2) { // Two args: create account for self
					
					if (!args.isInt(1)) throw new NotIntegerException();
					
					String accountno = args.getString(1);
					if ((accountno.length() > 7)) throw new AccountIntegerCountException();
						
					if (AccountUtil.accountNoExists(accountno)) throw new AccountAlreadyExistsException(accountno);
						
					AccountExecutor.createAccount(executor, executorName, accountno);
					
					return true;
				
				} else if (args.size()==3) { // Three args: create account for another player
					if (!executor.hasPermission("gpp.economos.account.remotecreate")) throw new NoPermissionException();
						
					String accountno = args.getString(1);
					if (TypeUtil.isInteger(accountno)) throw new NotIntegerException();
					if (!(accountno.length() > 7)) throw new AccountIntegerCountException();
					
					String accountName = args.getString(2);
					if (!StringUtil.isCleanString(accountName)) throw new NotAlphanumericException();
					
					if (accountName.equals("noowner") || accountName.equals("none") || accountName.equals("null")) {
						AccountExecutor.createAccount(executor, null, accountno);
						return true;
					} else {
						AccountExecutor.createAccount(executor, accountName, accountno);
						return true;
					}
				} else {
					executor.sendMessage(ChatColor.RED + "Proper format:" + ChatColor.WHITE + " /acc create <accountnumber>");
					return true;
				}
			
			//Cancel account
			} else if (args.getString(0).equalsIgnoreCase("close") || args.getString(0).equalsIgnoreCase("delete")
					|| args.getString(0).equalsIgnoreCase("cancel")) {
				if (args.size()==2) {
					String accountno = args.getString(1);
					if (!AccountUtil.accountNoExists(accountno)) throw new AccountDoesNotExistException(accountno);
					
					if (!accountant.hasAccountNo(accountno)) throw new AccountNotOwnerException(accountno);
					
					if (AccountUtil.isOwner(executorName, accountno)) {
						if (AccountUtil.getAccountBalance(accountno)>0)	throw new AccountNotEmptyException(accountno);
						AccountExecutor.closeAccount(executor, accountno);
					} else {
						if (!executor.hasPermission("gpp.economos.account.remoteclose")) throw new AccountNoAccessException(accountno);
						AccountExecutor.closeAccount(executor, accountno);
					}
					
				} else {
					executor.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc close <accountno>");
					return true;
				}
				
			//Deposit wallet cash into select or default account
			//TODO: Checks for active account
			} else if (args.getString(0).equalsIgnoreCase("deposit") || args.getString(0).equalsIgnoreCase("dep")
					|| args.getString(0).equalsIgnoreCase("put")) {
				if (args.size()==2) {
					if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
					double value = args.getDouble(1);
					
					if (accountant.getWalletBalance()<value) throw new WalletNotEnoughFundsException(value);
					
					if (!accountant.guaranteeActiveAccount()) throw new AccountRequiredException();
					
					AccountExecutor.depositFunds(executor, accountant.getActiveAccount().getAccountNumber(), value);
					return true;
					
				} else if (args.size()>=3) {
					if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
					
					double value = args.getDouble(1);
					
					if (accountant.getWalletBalance()<value) throw new WalletNotEnoughFundsException(value);
					
					if (!TypeUtil.isInteger(args.getString(2))) throw new NotAlphanumericException();
							
					String accountno = args.getString(2);
					
					if (!AccountUtil.accountNoExists(accountno)) throw new AccountDoesNotExistException(accountno);
					
					if (!AccountUtil.isUser(executorName, accountno)) throw new AccountNoAccessException(accountno);
					
					AccountExecutor.depositFunds(executor, accountno, value);
					return true;
					
				} else {
					executor.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
					return true;
				}
				
			} else if (args.getString(0).equalsIgnoreCase("withdraw") || args.getString(0).equalsIgnoreCase("take")
					|| args.getString(0).equalsIgnoreCase("wd")) {
				if (args.size()==2) {
					if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
					double value = args.getDouble(1);
					
					if (!accountant.guaranteeActiveAccount()) throw new AccountRequiredException();
					
					String accountno = accountant.getActiveAccountNumber();
					if (accountant.getAccountBalance(accountno)<value) throw new AccountNotEnoughFundsException(value, accountno);
					
					AccountExecutor.withdrawFunds(executor, accountant.getActiveAccount().getAccountNumber(), value);
					return true;
				} else if (args.size()>=3) {
					if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
					double value = args.getDouble(1);
					if (!TypeUtil.isInteger(args.getString(2))) throw new NotAlphanumericException();
					String accountno = args.getString(2);
					if (!AccountUtil.accountNoExists(accountno)) throw new AccountDoesNotExistException(accountno);
					//TODO: Check for withdraw limits
					if (!AccountUtil.isUser(executorName, accountno)) throw new AccountNoAccessException(accountno);
					//TODO: Get withdraw limit system working
					/*
					double withdrawlimit = AccountUtil.getAccountUserWithdrawLimit(accountno, executorName);
					if (!(withdrawlimit==0 || withdrawlimit>=value)) throw new 
					*/
					AccountExecutor.withdrawFunds(executor, accountno, value);
					return true;
				} else {
					executor.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc deposit <value> [accountno]");
					return true;
				}
				
			//Show account information
			} else if (args.getString(0).equalsIgnoreCase("info") || args.getString(0).equalsIgnoreCase("i")) {
				if (args.size()==2) {
					if (!TypeUtil.isInteger(args.getString(1))) throw new NotAlphanumericException();
					String accountno = args.getString(1);
					if (!AccountUtil.accountNoExists(accountno)) throw new AccountDoesNotExistException(accountno);
					if (accountant.hasAccountNo(accountno)) {
						AccountExecutor.displayAccountInfo(args.getString(1), executor);
						return true;
					} else {
						if (!executor.hasPermission("gpp.economos.account.peek")) throw new AccountNoAccessException(accountno);
						AccountExecutor.displayAccountInfo(args.getString(1), executor);
						return true;
					}
				} else {
					if (accountant.guaranteeActiveAccount()) throw new AccountRequiredException();
					AccountExecutor.displayAccountInfo(accountant.getActiveAccount().getAccountNumber(), executor);
					return true;
				}
			
			//Show the player their accounts
			} else if (args.getString(0).equalsIgnoreCase("list")) {
				if (args.size()>=2) {
					if (!executor.hasPermission("gpp.economos.account.peek")) throw new NoPermissionException();
					String targetPlayer = args.getString(1);
					String matchPlayer = args.getPlayerNameMatch(1);
					
					if (AccountUtil.getAnyAccount(targetPlayer)!=null) {
						AccountExecutor.displayAccountsList(executor, targetPlayer);
						return true;
					} else {
						if (matchPlayer!=null) {
							AccountExecutor.displayAccountsList(executor, matchPlayer);
							return true;
						} else throw new NoPlayerException();
					}
				} else {
					if (!(accountant.accountCount()>0)) throw new AccountRequiredException();
				
					AccountExecutor.displayAccountsList(executor, executor.getName());
					return true;
				}
				
			//Set active account
			} else if (args.getString(0).equalsIgnoreCase("setactive") || args.getString(0).equalsIgnoreCase("active") ||
					args.getString(0).equalsIgnoreCase("use") || args.getString(0).equalsIgnoreCase("focus")) {
				if (args.size()==2) {
					if (!TypeUtil.isInteger(args.getString(1))) throw new NotIntegerException();
					
					String accountno = args.getString(1);
					if (!AccountUtil.accountNoExists(accountno)) throw new AccountDoesNotExistException(accountno);
					if (!accountant.hasAccountNo(accountno)) throw new AccountNoAccessException(accountno);
					//TODO: Run under AccountExecutor
					AccountUtil.getAccountant(executorName).setActiveAccount(accountno);
					executor.sendMessage(ChatColor.YELLOW + "Account " + ChatColor.WHITE +
							accountno + ChatColor.YELLOW + " set to active.");
					return true;
				} else {
					throw new GPPException(ChatColor.DARK_RED + "You must supply an account number to make active.");
				}
				
			//Set account balance
			} else if (args.getString(0).equalsIgnoreCase("setbal") || args.getString(0).equalsIgnoreCase("set")) {
				//Permissions check
				if (!executor.hasPermission("gpp.economos.account.setbalance")) throw new NoPermissionException();
				
				if (args.size()>2) { //Check for three args: setbal keyword, the account number, and the value to set it to
					if (!TypeUtil.isInteger(args.getString(1))) throw new NotIntegerException();
					String targetAccount = args.getString(1);
					if (!AccountUtil.accountNoExists(targetAccount)) throw new AccountDoesNotExistException(targetAccount);
					if (!TypeUtil.isDouble(args.getString(2))) throw new NotDoubleException();
					double value = Double.parseDouble(args.getString(2));
					AccountExecutor.setBalance(executor, targetAccount, value);
					return true;
				} else {
					if (args.size()>1) {
						if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
						double value = args.getDouble(1);
						if (!accountant.guaranteeActiveAccount()) throw new AccountRequiredException();
						AccountExecutor.setBalance(executor, accountant.getActiveAccount().getAccountNumber(), value);
						return true;
					} else {
						executor.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc setbal [account] <amount>");
						return true;
					}
				}
				
			//Transfer funds - this part sucks.
			} else if (args.getString(0).equalsIgnoreCase("transfer") || args.getString(0).equalsIgnoreCase("pay")
					|| args.getString(0).equalsIgnoreCase("send")) {
				if (args.size()>2) {
					if (args.size()>3) {
						//If transfer is 4 args: transfer keyword, transfer amount, sending account, and destination account
						if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
						double value = Double.parseDouble(args.getString(1));
						String fromAccount = args.getString(2);
						if (!AccountUtil.accountNoExists(fromAccount)) throw new AccountDoesNotExistException(fromAccount);
						
						if (accountant.hasAccountNo(fromAccount)) {
							String toAccount = args.getString(3);
							if (!AccountUtil.accountNoExists(toAccount)) throw new AccountDoesNotExistException(toAccount);
							if (value<=0) throw new InvalidArgumentException();
							if (AccountUtil.getAccountBalance(fromAccount)<value)
								throw new AccountNotEnoughFundsTransferException(value, fromAccount, toAccount);
							AccountExecutor.transferFunds(executor, fromAccount, toAccount, value);
							return true;
						} else {
							//Check whether player can transfer from accounts they don't own
							if (!executor.hasPermission("gpp.economos.account.remotetransfer")) throw new AccountNoAccessException(fromAccount);
							String toAccount = args.getString(3);
							if (!AccountUtil.accountNoExists(fromAccount)) throw new AccountDoesNotExistException(fromAccount);
							if (value<=0) throw new InvalidArgumentException();
							//GPP.consoleInfo(String.valueOf(AccountUtil.getAccountBalance(fromAccount)));
							if (AccountUtil.getAccountBalance(fromAccount)<value)
								throw new AccountNotEnoughFundsTransferException(value, fromAccount, toAccount);
							AccountExecutor.transferFunds(executor, fromAccount, toAccount, value);
							return true;
						}
					} else {
						// If three args: transfer keyword, transfer amount, and the destination
						if (!TypeUtil.isDouble(args.getString(1))) throw new NotDoubleException();
						double value = Double.parseDouble(args.getString(1));
						String toAccount = args.getString(2);
						if (!AccountUtil.accountNoExists(toAccount)) throw new AccountDoesNotExistException(toAccount);
						if (!accountant.guaranteeActiveAccount()) throw new AccountRequiredException();
						if (value<=0) throw new InvalidArgumentException();
						Account playerAccount = accountant.getActiveAccount();
						if (playerAccount.getBalance()<value)
							throw new AccountNotEnoughFundsTransferException(value, playerAccount.getAccountNumber(), toAccount);
						AccountExecutor.transferFunds(executor, playerAccount.getAccountNumber(), toAccount, value);
						return true;
					}
				} else {
					executor.sendMessage(ChatColor.DARK_RED + "Proper format:" + ChatColor.WHITE + " /acc transfer <amount> [from-account] <to-account>");
					return true;
				}
				
			} if (args.getPlayerNameMatch(0)!=null) {
				String targetName = args.getPlayerNameMatch(0);
				
				if (!AccountUtil.hasAccountant(targetName)) throw new GPPException("Player does not have any accounts.");
				
				AccountExecutor.displayAccountBalance(AccountUtil.getAccountant(targetName).getActiveAccountNumber(), executor);
				return true;
			// /acc <arg> - check if <arg> matches an account number
			} else if (AccountUtil.accountNoExists(args.getString(0))) {
				
			} else {
				throw new InvalidArgumentException();
			}
		}
		return false;
	}
	
	
}
