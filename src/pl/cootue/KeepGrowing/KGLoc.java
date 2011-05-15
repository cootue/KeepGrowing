package pl.cootue.KeepGrowing;

import java.io.Serializable;

import org.bukkit.block.Block;

public class KGLoc implements Serializable {

	private static final long serialVersionUID = 3167738358584931958L;
	private int x;
	private int y;
	private int z;
	private long time;

	public KGLoc(Block block, long time) {
		this.x = block.getX();
		this.y = block.getY();
		this.z = block.getZ();
		this.time = time;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getTime() {
		return time;
	}

}
