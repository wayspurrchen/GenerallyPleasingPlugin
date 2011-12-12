package me.theheyway.GPP.Listeners;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.AreYouExperienced.Constants;
import me.theheyway.GPP.Util.GenUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GPPBlockListener extends BlockListener {
	
    public GPP plugin;
    
    public GPPBlockListener(GPP plugin) {
        this.plugin = plugin;
    }
	
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("aye.allowed.miningexp")) {
			GPP.logger.info("permission for mining exp given");
			Block block = event.getBlock();
			Material blockType = block.getType();
			boolean match = false;
			int index = -1;
			for (int i=0; i < Constants.blockList.size(); i++) {
				GPP.logger.info("checking"+Constants.blockList.get(i));
				if (blockType.equals(Constants.blockList.get(i))) {
					match = true;
					index = i;
					GPP.logger.info("match found index "+i);
					break;
				}
			}
			if (match) {
				float curExp = player.getExp();
				GPP.logger.info("current player exp: " + curExp);
				float expBonus = 0;
				
				switch (index) {
					case 0:
						expBonus = (float) Constants.BREAK_EXP_COALORE;
						GPP.logger.info("Case 0 found: " + expBonus + " " + expBonus);
						break;
					case 1:
						expBonus = (float) Constants.BREAK_EXP_DIAMONDORE;
						GPP.logger.info("Case 1 found: " + expBonus + " " + expBonus);
						break;
					case 2:
						expBonus = (float) Constants.BREAK_EXP_GOLDORE;
						GPP.logger.info("Case 2 found: " + expBonus + " " + expBonus);
						break;
					case 3:
						expBonus = (float) Constants.BREAK_EXP_IRONORE;
						GPP.logger.info("Case 3 found: " + expBonus + " " + expBonus);
						break;
					case 4:
						expBonus = (float) Constants.BREAK_EXP_LAPISORE;
						GPP.logger.info("Case 4 found: " + expBonus + " " + expBonus);
						break;
					case 5:
						expBonus = (float) Constants.BREAK_EXP_OBSIDIAN;
						GPP.logger.info("Case 5 found: " + expBonus + " " + expBonus);
						break;
					case 6:
						expBonus = (float) Constants.BREAK_EXP_REDSTONEORE;
						GPP.logger.info("Case 6 found: " + expBonus + " " + expBonus);
						break;
				}
				
				GPP.logger.info("expBonus: "+expBonus);
				expBonus *= Constants.EXP_MULTIPLIER;
				GPP.logger.info("expBonus after multiplier: "+expBonus);
				player.setExp(curExp + expBonus);
				GPP.logger.info("new exp: "+player.getExp());
				GenUtil.updateExp(player);
			}
		}
	}

}
