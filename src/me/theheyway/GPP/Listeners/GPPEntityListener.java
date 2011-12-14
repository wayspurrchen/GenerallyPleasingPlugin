package me.theheyway.GPP.Listeners;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.AreYouExperienced.AYE;
import me.theheyway.GPP.AreYouExperienced.AYEConstants;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class GPPEntityListener extends EntityListener {
	
    public GPP plugin;
    
    public GPPEntityListener(GPP plugin) {
        this.plugin = plugin;
    }
	
	public void onEntityDamage(EntityDamageEvent event) {
		Entity ent = event.getEntity();
		//TODO: Implement with permissions/AYE settings/etc.
		if (ent instanceof Player) {
			if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK
					|| event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION 
					|| event.getCause() == EntityDamageEvent.DamageCause.FIRE
					|| event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
					|| event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING //THAT'S RIGHT, YOU CAN GET EXPERIENCED BY BEING STRUCK BY LIGHTNING. HOW HARDCORE IS THAT?!
					|| event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE
					|| event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
				AYE.addAccumulatedExp((Player) ent, event.getDamage() * AYEConstants.DAMAGE_TAKEN_EXP);
			}
		} else if (ent instanceof Creature) {
			if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
				EntityDamageByEntityEvent entityDmgEvent = (EntityDamageByEntityEvent) event;
				if (entityDmgEvent.getDamager() instanceof Player) {
					Player player = (Player) entityDmgEvent.getDamager();
					if (ent instanceof Blaze) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_BLAZE);
					else if (ent instanceof CaveSpider) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_CAVESPIDER);
					else if (ent instanceof Chicken) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_CHICKEN);
					else if (ent instanceof Cow) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_COW);
					else if (ent instanceof Creeper) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_CREEPER);
					else if (ent instanceof Enderman) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_ENDERMAN);
					else if (ent instanceof Ghast) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_GHAST);
					else if (ent instanceof MagmaCube) {
						if (((MagmaCube) ent).getSize() == 0) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_MAGMACUBE_SMALL);
						else if (((MagmaCube) ent).getSize() == 1) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_MAGMACUBE_MEDIUM);
						else if (((MagmaCube) ent).getSize() == 2) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_MAGMACUBE_LARGE);
					} else if (ent instanceof MushroomCow) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_MOOSHROOM);
					else if (ent instanceof Pig) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_PIG);
					else if (ent instanceof Sheep) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SHEEP);
					else if (ent instanceof Silverfish) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SILVERFISH);
					else if (ent instanceof Skeleton) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SKELETON);
					else if (ent instanceof Slime) {
						if (((Slime) ent).getSize() == 0) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SLIME_SMALL);
						else if (((Slime) ent).getSize() == 1) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SLIME_MEDIUM);
						else if (((Slime) ent).getSize() == 2) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SLIME_LARGE);
						else if (((Slime) ent).getSize() == 3) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SLIME_HUGE);
					} else if (ent instanceof Snowman) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SNOWGOLEM);
					else if (ent instanceof Spider) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SPIDER);
					else if (ent instanceof Squid) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_SQUID);
					else if (ent instanceof Villager) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_VILLAGER);
					else if (ent instanceof Wolf) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_WOLF);
					else if (ent instanceof Zombie) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_ZOMBIE);
					else if (ent instanceof PigZombie) AYE.addAccumulatedExp(player, AYEConstants.MOB_BONUS_ZOMBIEPIGMAN);
				}
				
				
			}
		}
	}
	
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		
	}
	
	public void onEntityDeath(EntityDeathEvent event) {
		Entity ent = event.getEntity();
		if (ent instanceof Creature) {
			event.setDroppedExp(0);
		}
	}

}
