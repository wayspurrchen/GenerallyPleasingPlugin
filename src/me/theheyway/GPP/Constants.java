package me.theheyway.GPP;

public class Constants {
	
	public static final String SQLITE_DB_PATH = "plugins/GPP/db.sqlite";
	public static final String PLUGIN_DIR = "plugins/GPP";
	public static final String MASTER_CONFIG = "plugins/GPP/config.yml";
	
	public static boolean VERBOSE;
	
	public static String MYSQL_HOSTNAME;
	public static int MYSQL_PORT;
	public static String MYSQL_USERNAME;
	public static String MYSQL_PASSWORD;
	public static String MYSQL_DBNAME;
	
	public static boolean AREYOUEXPERIENCED_ENABLED;
	public static boolean ECONOMOS_ENABLED;
	
	
	public GPP plugin;
	
	public Constants(GPP plugin) {
		this.plugin = plugin;
		
		VERBOSE = plugin.getConfig().getBoolean("General.Verbose", false);
		
		AREYOUEXPERIENCED_ENABLED = plugin.getConfig().getBoolean("Modules.AreYouExperienced", true);
		ECONOMOS_ENABLED = plugin.getConfig().getBoolean("Modules.Economos", false);
		
		MYSQL_HOSTNAME = plugin.getConfig().getString("MySQL.hostname");
		MYSQL_PORT = plugin.getConfig().getInt("MySQL.port");
		MYSQL_USERNAME = plugin.getConfig().getString("MySQL.username");
		MYSQL_PASSWORD = plugin.getConfig().getString("MySQL.password");
		MYSQL_DBNAME = plugin.getConfig().getString("MySQL.db_name");
		GPP.logger.info("MYSQL_HOSTNAME: " + MYSQL_HOSTNAME);
		GPP.logger.info("MYSQL_PORT: " + MYSQL_PORT);
		GPP.logger.info("MYSQL_USERNAME: " + MYSQL_USERNAME);
		GPP.logger.info("MYSQL_PASSWORD: " + MYSQL_PASSWORD);
		GPP.logger.info("MYSQL_DBNAME: " + MYSQL_DBNAME);
		
	}

}
