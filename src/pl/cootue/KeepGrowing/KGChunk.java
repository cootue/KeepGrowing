package pl.cootue.KeepGrowing;

import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.block.Block;

public class KGChunk implements Serializable {

  private static final long serialVersionUID = 3423753453998563697L;
  private HashMap<String, KGLoc> list = new HashMap<String, KGLoc>();

  public KGChunk(KGLoc loc) {
    addToList(loc);
  }

  public HashMap<String, KGLoc> getList() {
    return list;
  }

  public void addToList(KGLoc loc) {
    list.put(getLocId(loc), loc);
  }

  public void removeFromList(Block block) {
    list.remove(getLocId(block));
  }

  public boolean onList(Block block) {
    if (list.containsKey(getLocId(block))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean isEmpty() {
    if (list.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }

  static String getLocId(Block block) {
    return block.getX() + "." + block.getY() + "." + block.getZ();
  }

  public String getLocId(KGLoc loc) {
    return loc.getX() + "." + loc.getY() + "." + loc.getZ();
  }

  static String getChunkId(Chunk chunk) {
    return chunk.getX() + "." + chunk.getZ();
  }

  static byte growSize(int sec, byte currState, byte lightLevel, byte waterLevel) {
    int result = currState;
    int shift = 515; // ~60min
    // int shift = 170; // ~20min
    // int shift = 2; // ~14sec

    if (lightLevel >= 9) {
      if (waterLevel == 0) {
        shift *= 4;
      }

      if (sec >= 7 * shift) {
        result = currState + 7;
      } else if (sec >= 6 * shift) {
        result = currState + 6;
      } else if (sec >= 5 * shift) {
        result = currState + 5;
      } else if (sec >= 4 * shift) {
        result = currState + 4;
      } else if (sec >= 3 * shift) {
        result = currState + 3;
      } else if (sec >= 2 * shift) {
        result = currState + 2;
      } else if (sec >= shift) {
        result = currState + 1;
      } else {
        result = currState;
      }

      if (result >= 7) {
        result = 7;
      }
    }

    return (byte) result;
  }

}
