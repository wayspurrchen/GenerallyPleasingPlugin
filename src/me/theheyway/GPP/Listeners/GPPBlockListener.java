package me.theheyway.GPP.Listeners;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.AreYouExperienced.AYE;
import me.theheyway.GPP.AreYouExperienced.AYEConstants;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;

public class GPPBlockListener extends BlockListener {
	
    public GPP plugin;
    
    public GPPBlockListener(GPP plugin) {
        this.plugin = plugin;
    }
	
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("gpp.aye.allowed.miningexp")) {
			Block block = event.getBlock();
			Material blockType = block.getType();
			boolean match = false;
			
			int index = -1; //Set to -1 so if it doesn't find a match, the switch statement will fail
			for (int i=0; i < AYEConstants.blockList.size(); i++) {
				if (blockType.equals(AYEConstants.blockList.get(i))) {
					match = true;
					index = i;
					//GPP.logger.info(AYEConstants.blockList.get(i) + " found index "+i);
					break;
				}
			}
			if (match) {
				//GPP.consoleInfo(player.getName() + " broke an ore block");
				double expBonus = 0;
				
				//Fugly switch statement for setting the exp bonus
				switch (index) { //EFFICACY!!!
					case 0:	expBonus = AYEConstants.BREAK_EXP_COALORE; break;
					case 1:	expBonus = AYEConstants.BREAK_EXP_DIAMONDORE; break;
					case 2:	expBonus = AYEConstants.BREAK_EXP_GOLDORE; break;
					case 3:	expBonus = AYEConstants.BREAK_EXP_IRONORE; break;
					case 4:	expBonus = AYEConstants.BREAK_EXP_LAPISORE;	break;
					case 5: expBonus = AYEConstants.BREAK_EXP_OBSIDIAN; break;
					case 6: expBonus = AYEConstants.BREAK_EXP_REDSTONEORE; break;
					case 7: expBonus = AYEConstants.BREAK_EXP_REDSTONEORE; break; // This accounts for GLOWING_REDSTONE_ORE
				}
				//GPP.consoleInfo("exPBonus s after multiplier: " +expBonus);
				AYE.addAccumulatedExp(player, expBonus);
			}
		}
	}

}
