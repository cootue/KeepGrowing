package pl.cootue.KeepGrowing;

import java.util.Date;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class KeepGrowingBlockListener extends BlockListener {
	private KeepGrowing plugin;
	private String hash;
	private Block block,blockTo;
	private Chunk chunk;
	private KGChunk kChunk;
	private Date date;

	public KeepGrowingBlockListener(KeepGrowing plugin) {
		this.plugin = plugin;
	}

	public void onBlockPlace(BlockPlaceEvent event) {
		block = event.getBlock();
		if (block.getType() == Material.CROPS) {
			chunk = block.getChunk();
			hash = KGChunk.getChunkId(chunk);
			date = new Date();

			if (!plugin.kgData.containsKey(hash)) {
				kChunk = new KGChunk(new KGLoc(block, date.getTime()));
				plugin.kgData.put(hash, kChunk);
			} else {
				kChunk = plugin.kgData.get(hash);
				kChunk.addToList(new KGLoc(block, date.getTime()));
				plugin.kgData.put(hash, kChunk);
			}
		}
	}

	public void onBlockBreak(BlockBreakEvent event) {
		block = event.getBlock();
		chunk = block.getChunk();
		hash = KGChunk.getChunkId(chunk);

		if (plugin.kgData.containsKey(hash)) {
			kChunk = plugin.kgData.get(hash);
			if (kChunk.onList(block)) {
				kChunk.removeFromList(block);
			}
			if (kChunk.onList(block)) {
				kChunk.removeFromList(block);
			}
			if (kChunk.isEmpty()) {
				plugin.kgData.remove(hash);
			}
		}
	}

	public void onBlockFromTo(BlockFromToEvent event) {
		blockTo = event.getToBlock();
		chunk = blockTo.getChunk();
		hash = KGChunk.getChunkId(chunk);

		if (plugin.kgData.containsKey(hash)) {
			kChunk = plugin.kgData.get(hash);
			if (kChunk.onList(blockTo)) {
				kChunk.removeFromList(blockTo);
			}
			
			if (kChunk.isEmpty()) {
				plugin.kgData.remove(hash);
			}
			
		}
	}
	
}
