package me.theheyway.GPP.AreYouExperienced;

import java.util.ArrayList;
import java.util.List;

import me.theheyway.GPP.GPP;

import org.bukkit.Material;

public class Constants {
	
	public static final String AYE_DIR = "plugins/GPP/AreYouExperienced";
	public static final String AYE_CONFIG_PATH = "plugins/GPP/AreYouExperienced/config.yml";
	public static final String AYE_INTERNAL_CONFIG = "me/theheyway/GPP/AreYouExperienced/config.yml";
	
	public static List<Material> blockList = new ArrayList<Material>();
	
	//This multiplies the values given in the config to make it match with the get/setExp functions, which take either 1 or 0.
	public static final double EXP_MULTIPLIER = 0.01;
	
	public static double BREAK_EXP_COALORE;
	public static double BREAK_EXP_DIAMONDORE;
	public static double BREAK_EXP_GOLDORE;
	public static double BREAK_EXP_IRONORE;
	public static double BREAK_EXP_LAPISORE;
	public static double BREAK_EXP_OBSIDIAN;
	public static double BREAK_EXP_REDSTONEORE;
	
	public static double TRAVELING_EXP;
	public static double EXPLORE_EXP;
	public static double STILL_ALIVE_EXP;
	public static double DAMAGE_TAKEN_EXP;
	
	public static double MOB_BONUS_BLAZE;
	public static double MOB_BONUS_CAVESPIDER;
	public static double MOB_BONUS_CHICKEN;
	public static double MOB_BONUS_CREEPER;
	public static double MOB_BONUS_COW;
	public static double MOB_BONUS_ENDERMAN;
	public static double MOB_BONUS_GHAST;
	public static double MOB_BONUS_MAGMACUBE;
	public static double MOB_BONUS_MOOSHROOM;
	public static double MOB_BONUS_PIG;
	public static double MOB_BONUS_SHEEP;
	public static double MOB_BONUS_SILVERFISH;
	public static double MOB_BONUS_SKELETON;
	public static double MOB_BONUS_SLIME;
	public static double MOB_BONUS_SNOWGOLEM;
	public static double MOB_BONUS_SPIDER;
	public static double MOB_BONUS_SPIDERJOCKEY;
	public static double MOB_BONUS_SQUID;
	public static double MOB_BONUS_VILLAGER;
	public static double MOB_BONUS_WOLF;
	public static double MOB_BONUS_ZOMBIE;
	public static double MOB_BONUS_ZOMBIEPIGMAN;
	
	public GPP plugin;
	public AYE aye;
	
	public Constants(GPP plugin, AYE aye) {
		this.aye = aye;
		this.plugin = plugin;
		
		blockList.add(Material.COAL_ORE);
		blockList.add(Material.DIAMOND_ORE);
		blockList.add(Material.IRON_ORE);
		blockList.add(Material.LAPIS_ORE);
		blockList.add(Material.OBSIDIAN);
		blockList.add(Material.REDSTONE_ORE);
		
		BREAK_EXP_COALORE = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.coal_ore");
		GPP.logger.info("Coal Ore EXP: "+BREAK_EXP_COALORE);
		BREAK_EXP_DIAMONDORE = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.diamond_ore");
		GPP.logger.info("Diamond Ore EXP: "+BREAK_EXP_DIAMONDORE);
		BREAK_EXP_GOLDORE = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.gold_ore");
		GPP.logger.info("Gold Ore EXP: "+BREAK_EXP_GOLDORE);
		BREAK_EXP_IRONORE = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.iron_ore");
		GPP.logger.info("Iron Ore EXP: "+BREAK_EXP_IRONORE);
		BREAK_EXP_LAPISORE = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.lapis_ore");
		GPP.logger.info("Lapis Ore EXP: "+BREAK_EXP_LAPISORE);
		BREAK_EXP_OBSIDIAN = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.obsidian");
		GPP.logger.info("Obsidian EXP: "+BREAK_EXP_OBSIDIAN);
		BREAK_EXP_REDSTONEORE = aye.getConfig().getDouble("AreYouExperienced.ore_break_exp.redstone_ore");
		GPP.logger.info("Redstone Ore EXP: "+BREAK_EXP_REDSTONEORE);
		
		TRAVELING_EXP = aye.getConfig().getDouble("AreYouExperienced.traveling_exp");
		EXPLORE_EXP = aye.getConfig().getDouble("AreYouExperienced.chunk_discover_exp");
		STILL_ALIVE_EXP = aye.getConfig().getDouble("AreYouExperienced.still_alive_exp");
		DAMAGE_TAKEN_EXP = aye.getConfig().getDouble("AreYouExperienced.per_damage_exp");
		
		MOB_BONUS_BLAZE = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.blaze");
		MOB_BONUS_CAVESPIDER = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.cavespider");
		MOB_BONUS_CHICKEN = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.chicken");
		MOB_BONUS_CREEPER = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.creeper");
		MOB_BONUS_COW = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.cow");
		MOB_BONUS_ENDERMAN = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.enderman");
		MOB_BONUS_GHAST = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.ghast");
		MOB_BONUS_MAGMACUBE = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.magmacube");
		MOB_BONUS_MOOSHROOM = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.mooshroom");
		MOB_BONUS_PIG = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.pig");
		MOB_BONUS_SHEEP = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.sheep");
		MOB_BONUS_SILVERFISH = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.silverfish");
		MOB_BONUS_SKELETON = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.skeleton");
		MOB_BONUS_SLIME = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.slime");
		MOB_BONUS_SNOWGOLEM = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.snowgolem");
		MOB_BONUS_SPIDER = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.spider");
		MOB_BONUS_SPIDERJOCKEY = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.spiderjockey");
		MOB_BONUS_SQUID = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.squid");
		MOB_BONUS_VILLAGER = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.villager");
		MOB_BONUS_WOLF = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.wolf");
		MOB_BONUS_ZOMBIE = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.zombie");
		MOB_BONUS_ZOMBIEPIGMAN = aye.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.zombiepigman");
		
	}

}
