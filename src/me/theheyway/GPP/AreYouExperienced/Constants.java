package me.theheyway.GPP.AreYouExperienced;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class Constants {
	
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
	
	public AYE plugin;
	
	public Constants(AYE plugin) {
		
		this.plugin = plugin;
		
		blockList.add(Material.COAL_ORE);
		blockList.add(Material.DIAMOND_ORE);
		blockList.add(Material.IRON_ORE);
		blockList.add(Material.LAPIS_ORE);
		blockList.add(Material.OBSIDIAN);
		blockList.add(Material.REDSTONE_ORE);
		
		BREAK_EXP_COALORE = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.coal_ore");
		AYE.logger.info("Coal Ore EXP: "+BREAK_EXP_COALORE);
		BREAK_EXP_DIAMONDORE = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.diamond_ore");
		AYE.logger.info("Diamond Ore EXP: "+BREAK_EXP_DIAMONDORE);
		BREAK_EXP_GOLDORE = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.gold_ore");
		AYE.logger.info("Gold Ore EXP: "+BREAK_EXP_GOLDORE);
		BREAK_EXP_IRONORE = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.iron_ore");
		AYE.logger.info("Iron Ore EXP: "+BREAK_EXP_IRONORE);
		BREAK_EXP_LAPISORE = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.lapis_ore");
		AYE.logger.info("Lapis Ore EXP: "+BREAK_EXP_LAPISORE);
		BREAK_EXP_OBSIDIAN = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.obsidian");
		AYE.logger.info("Obsidian EXP: "+BREAK_EXP_OBSIDIAN);
		BREAK_EXP_REDSTONEORE = plugin.getConfig().getDouble("AreYouExperienced.ore_break_exp.redstone_ore");
		AYE.logger.info("Redstone Ore EXP: "+BREAK_EXP_REDSTONEORE);
		
		TRAVELING_EXP = plugin.getConfig().getDouble("AreYouExperienced.traveling_exp");
		EXPLORE_EXP = plugin.getConfig().getDouble("AreYouExperienced.chunk_discover_exp");
		STILL_ALIVE_EXP = plugin.getConfig().getDouble("AreYouExperienced.still_alive_exp");
		DAMAGE_TAKEN_EXP = plugin.getConfig().getDouble("AreYouExperienced.per_damage_exp");
		
		MOB_BONUS_BLAZE = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.blaze");
		MOB_BONUS_CAVESPIDER = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.cavespider");
		MOB_BONUS_CHICKEN = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.chicken");
		MOB_BONUS_CREEPER = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.creeper");
		MOB_BONUS_COW = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.cow");
		MOB_BONUS_ENDERMAN = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.enderman");
		MOB_BONUS_GHAST = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.ghast");
		MOB_BONUS_MAGMACUBE = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.magmacube");
		MOB_BONUS_MOOSHROOM = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.mooshroom");
		MOB_BONUS_PIG = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.pig");
		MOB_BONUS_SHEEP = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.sheep");
		MOB_BONUS_SILVERFISH = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.silverfish");
		MOB_BONUS_SKELETON = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.skeleton");
		MOB_BONUS_SLIME = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.slime");
		MOB_BONUS_SNOWGOLEM = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.snowgolem");
		MOB_BONUS_SPIDER = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.spider");
		MOB_BONUS_SPIDERJOCKEY = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.spiderjockey");
		MOB_BONUS_SQUID = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.squid");
		MOB_BONUS_VILLAGER = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.villager");
		MOB_BONUS_WOLF = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.wolf");
		MOB_BONUS_ZOMBIE = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.zombie");
		MOB_BONUS_ZOMBIEPIGMAN = plugin.getConfig().getDouble("AreYouExperienced.mob_bonus_exp.zombiepigman");
		
	}

}
