package me.theheyway.GPP.Listeners;

import me.theheyway.GPP.Constants;
import me.theheyway.GPP.GPP;
import me.theheyway.GPP.AreYouExperienced.AYE;
import me.theheyway.GPP.AreYouExperienced.AYEConstants;
import me.theheyway.GPP.Economos.AccountUtil;
import me.theheyway.GPP.Economos.Economos;
import me.theheyway.GPP.Economos.EconomosConstants;
import me.theheyway.GPP.Overlord.Cruelty;
import me.theheyway.GPP.Util.MiscUtil;

import org.bukkit.Location;
import org.bukkit.entity.Player;
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
	
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		if(!AccountUtil.hasAccountant(playerName)) {
			AccountUtil.createAccountant(playerName);
			if (Constants.VERBOSE) GPP.consoleInfo("[Economos] Accountant created for " + player.getName() + ".");
		} else if (Constants.VERBOSE) GPP.consoleInfo("[Economos] Accountant already exists for " + player.getName() + ".");
	}
	
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (Cruelty.isFrozen(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

}
