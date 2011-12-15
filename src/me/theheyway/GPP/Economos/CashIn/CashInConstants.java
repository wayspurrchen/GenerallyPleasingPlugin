package me.theheyway.GPP.Economos.CashIn;

public class CashInConstants {
	public static Double CASH_PER_LEVEL;
	
	public static final String CASHIN_DIR = "plugins/GPP/Economos/CashIn";
	public static final String CASHIN_CONFIG_PATH = "plugins/GPP/Economos/CashIn/config.yml";
	public static final String CASHIN_INTERNAL_CONFIG = "me/theheyway/GPP/Economos/CashIn/config.yml";
	
	public CashInConstants() {
		CASH_PER_LEVEL = CashIn.cashin_config.getDouble("General.Cash per Level",1);
	}
}
