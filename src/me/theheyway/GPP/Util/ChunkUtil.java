package me.theheyway.GPP.Util;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class ChunkUtil {
	
	public static void loadChunkAtLocation(Location loc, boolean generate) {
		Chunk chunk = loc.getChunk();
		if (!chunk.isLoaded()) {
			if (generate) chunk.load(true);
			else chunk.load(false);
		}
	}

}
