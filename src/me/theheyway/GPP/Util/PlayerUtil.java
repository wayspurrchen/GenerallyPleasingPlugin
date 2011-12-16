package me.theheyway.GPP.Util;

import org.bukkit.entity.Player;

public class PlayerUtil {
    
    public static void heal(Player player, Integer amount) {
    	int healAmount = amount;
		int health = player.getHealth() + healAmount;
		
		//validations
		if (health > player.getMaxHealth())
			health = player.getMaxHealth();
		else if (health < 0)
			health = 0;
		
		//apply changes
		player.setHealth(health); //YES! Totally mean you can /heal -3;
    }

}
