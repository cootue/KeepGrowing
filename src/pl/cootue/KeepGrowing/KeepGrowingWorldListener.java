package pl.cootue.KeepGrowing;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;

public class KeepGrowingWorldListener extends WorldListener {
  private KeepGrowing plugin;
  private HashMap<String, KGLoc> list;
  private HashMap<String, KGLoc> tmpList;
  private Block block, soil;
  private Chunk chunk;
  private String hash;
  private KGChunk kChunk;
  private Date date;
  private int timeDif;
  private byte maxState = 7;
  private byte lightLevel;
  private byte currState;
  private byte newState;

  public KeepGrowingWorldListener(KeepGrowing plugin) {
    this.plugin = plugin;
  }

  public void onChunkUnload(ChunkUnloadEvent event) {
    chunk = event.getChunk();
    hash = KGChunk.getChunkId(chunk);

    if (plugin.kgData.containsKey(hash)) {
      kChunk = plugin.kgData.get(hash);
      list = kChunk.getList();
      tmpList = new HashMap<String, KGLoc>(list);
      date = new Date();

      for (KGLoc loc : tmpList.values()) {
        block = event.getWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ());
        if (block.getType() == Material.CROPS) {
          loc.setTime(date.getTime());
        } else {
          kChunk.removeFromList(block);
        }
      }

      if (list.isEmpty()) {
        plugin.kgData.remove(hash);
      }
    }
  }

  public void onChunkLoad(ChunkLoadEvent event) {
    chunk = event.getChunk();
    hash = KGChunk.getChunkId(chunk);

    if (plugin.kgData.containsKey(hash)) {
      kChunk = plugin.kgData.get(hash);
      date = new Date();
      list = kChunk.getList();
      tmpList = new HashMap<String, KGLoc>(list);

      for (KGLoc loc : tmpList.values()) {
        block = event.getWorld().getBlockAt(loc.getX(), loc.getY(), loc.getZ());
        if (block.getType() == Material.CROPS) {
          currState = block.getData();
          if (currState != maxState) {
            lightLevel = block.getLightLevel();
            timeDif = (int) ((date.getTime() - loc.getTime()) / 1000);
            soil = chunk.getBlock(block.getX(), block.getY() - 1, block.getZ());
            newState = KGChunk.growSize(timeDif, currState, lightLevel, soil.getData());
            block.setData(newState);
            if (newState == maxState) {
              kChunk.removeFromList(block);
            }
          } else {
            kChunk.removeFromList(block);
          }
        } else {
          kChunk.removeFromList(block);
        }
      }

      if (list.isEmpty()) {
        plugin.kgData.remove(hash);
      }

    }
  }
}