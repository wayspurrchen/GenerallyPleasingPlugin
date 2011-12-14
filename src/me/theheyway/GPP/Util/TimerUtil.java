package me.theheyway.GPP.Util;

import java.util.Timer;
import java.util.TimerTask;

import me.theheyway.GPP.GPP;
import me.theheyway.GPP.AreYouExperienced.AYE;

public class TimerUtil extends TimerTask {
	
	/*
	 * Get yer time management here! Grabs the time of day every tick (3000ms).
	 * 
	 * Notes:
	 * 
	 * Noon = 6000
	 * Sunset = 13000
	 * Midnight = 18000
	 * Sunrise = 23000
	 * 
	 */
	
	private final static long tick = 1000;
	private static long oldTime = 0;
	private static long newTime = 0;
	private static boolean givenAliveBonus = false;
	
	public TimerUtil() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(this, tick, tick);
	}

	@Override
	public void run() {
		oldTime = newTime;
		newTime = GPP.server.getWorld("world").getTime();
		//GPP.logger.info("Timer ticking at " + tick + "ms. In game time: " + GPP.server.getWorld("world").getTime() + " oldTime: " + oldTime
		//		+ " | newTime: " + newTime);
		//TODO: Make this toggleable, as well as multiworld, and implemented with permissions and AYE module status
		//TODO: Make this not totally stupid broken
		/*if (oldTime < 23000 && newTime > 23000) {
			if (!givenAliveBonus) {
				givenAliveBonus = true;
				AYE.stillAliveReward();
			}
		} else {
			givenAliveBonus = false;
		}*/
		
	}
	

}
