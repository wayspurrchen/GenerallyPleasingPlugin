package me.theheyway.GPP.Listeners;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.AreYouExperienced.AYE;
import me.theheyway.GPP.AreYouExperienced.AYEConstants;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Util.MiscUtil;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GPPPlayerListener extends PlayerListener {
	
    public GPP plugin;
    
    public GPPPlayerListener(GPP plugin) {
        this.plugin = plugin;
    }
	
	public void onPlayerMove(PlayerMoveEvent event) {
		//TODO: Implement properly
		AYE.addAccumulatedExp(event.getPlayer(), AYEConstants.TRAVELING_EXP);
		
		if (Cruelty.isFrozen(event.getPlayer())) {
			Location loc = event.getFrom();
			Location to = event.getTo();
			loc.setYaw(to.getYaw());
			loc.setPitch(to.getPitch());
			event.getPlayer().teleport(loc);
		}
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		
	}
	
	//MUST REMOVE THIS LATER!!
	public void onPlayerJoin(PlayerJoinEvent event) {
		//event.getPlayer().setLevel(0);
		//event.getPlayer().setExp(0);
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (Cruelty.isFrozen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

}
