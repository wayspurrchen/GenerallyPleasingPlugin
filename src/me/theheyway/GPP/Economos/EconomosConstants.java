package me.theheyway.GPP.Economos;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import me.theheyway.GPP.GPP;

public class EconomosConstants {
	
	public static final String ECONOMOS_DIR = "plugins/GPP/Economos";
	public static final String ECONOMOS_CONFIG_PATH = "plugins/GPP/Economos/config.yml";
	public static final String ECONOMOS_INTERNAL_CONFIG = "me/theheyway/GPP/Economos/config.yml";
	
	//Okay, so they're not the most constant-y constants in the world
	public static String CURRENCY_NAME_SINGULAR;
	public static String CURRENCY_NAME_PLURAL;
	
	public static double WALLET_INITIAL_BALANCE;

	public static boolean INTEREST_ONLINE;
	public static double INTEREST_AMOUNT;
	public static int INTEREST_TICK;

	public static String WALLET_NAME;
	public static boolean WALLET_LOSE_ON_DEATH;
	public static boolean WALLET_INTEREST_ENABLED;
	public static boolean WALLET_LOCAL_PAY;
	
	public static String ACCOUNTS_NAME;
	public static boolean ACCOUNTS_INTEREST_ENABLED;
	public static double ACCOUNTS_INTEREST_DEFAULT;
	
	public static final String DB_WALLET_TABLENAME = "Wallets";
	public static final String DB_ACCOUNTS_TABLENAME = "Accounts";
	public static final String DB_ACCOUNTENTRIES_TABLENAME = "AccountEntries";
	
	public static boolean ECONOMY_ENABLED;
	public static boolean CASHIN_ENABLED;
	public static boolean SHOPS_ENABLED;
	
	public static boolean VERBOSE;
	
	public static GPP plugin;
	public static Economos economos;
	
	public EconomosConstants(GPP plugin, Economos economos) {
		EconomosConstants.plugin = plugin;
		EconomosConstants.economos = economos;
		
		VERBOSE = plugin.getConfig().getBoolean("General.Verbose", false);
		
		ECONOMY_ENABLED = plugin.getConfig().getBoolean("Modules.Economy.Enabled", true);
		CASHIN_ENABLED = plugin.getConfig().getBoolean("Modules.CashIn.Enabled", true);
		SHOPS_ENABLED = plugin.getConfig().getBoolean("Modules.Shops.Enabled", true);
		
		CURRENCY_NAME_SINGULAR = Economos.economos_config.getString("General.Currency.Name.Singular");
		CURRENCY_NAME_PLURAL = Economos.economos_config.getString("General.Currency.Name.Plural");
		
		WALLET_NAME = Economos.economos_config.getString("Accounts.Client.Name", "Wallet");
		WALLET_INITIAL_BALANCE = Economos.economos_config.getDouble("Accounts.Client.Initial Balance",0.0);
		WALLET_LOCAL_PAY = Economos.economos_config.getBoolean("Accounts.Client.Local Payment", false);
		WALLET_LOSE_ON_DEATH = Economos.economos_config.getBoolean("Accounts.Client.Lose on Death", false);
		
		ACCOUNTS_NAME = Economos.economos_config.getString("Accounts.Name", "Account");
		ACCOUNTS_INTEREST_ENABLED = Economos.economos_config.getBoolean("Accounts.Interest.Enabled", true);
		
		INTEREST_AMOUNT = Economos.economos_config.getDouble("Accounts.Interest.Amount", 5.0E-4);
		INTEREST_ONLINE = Economos.economos_config.getBoolean("Accounts.Interest.Online", true);
		INTEREST_TICK = Economos.economos_config.getInt("Accounts.Interest.Frequency",60);
		
		GPP.consoleInfo("[Economos] Initial balance set to " + WALLET_INITIAL_BALANCE);
		
	}
}
