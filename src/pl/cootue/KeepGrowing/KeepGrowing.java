package pl.cootue.KeepGrowing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KeepGrowing extends JavaPlugin {

	public HashMap<String, KGChunk> kgData = new HashMap<String, KGChunk>();
	public Logger log = Logger.getLogger("Minecraft");

	private final BlockListener blockListener = new KeepGrowingBlockListener(this);
	private final WorldListener worldListener = new KeepGrowingWorldListener(this);
	private PluginManager pm;

	private String dirName = "plugins" + File.separator + "KeepGrowing" + File.separator;
	private String fName = "KeepGrowingData.ser";
	private String logPrefix = "[KeepGrowing]: ";
	private File file;

	@Override
	public void onDisable() {

		if (!kgData.isEmpty()) {
			try {
				FileOutputStream fileOut = new FileOutputStream(dirName + fName);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(kgData);
				out.flush();
				out.close();
				fileOut.close();
				log.info(logPrefix + "Saved data file");
			} catch (Exception e) {
				// e.printStackTrace();
				log.info(logPrefix + "Error while saving file");
			}
		} else {
			log.info(logPrefix + "No data to save");
		}
		log.info(logPrefix + "Plugin disabled");
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onEnable() {
		pm = this.getServer().getPluginManager();
		pm.registerEvent(Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.BLOCK_FROMTO , blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.CHUNK_UNLOAD, worldListener, Event.Priority.Normal, this);
		pm.registerEvent(Type.CHUNK_LOAD, worldListener, Event.Priority.Normal, this);
		
		file = new File(dirName);
		if (!file.exists()) {
			file.mkdir();
		}

		file = new File(dirName + fName);
		if (file.exists()) {
			try {
				FileInputStream fileIn = new FileInputStream(dirName + fName);
				ObjectInputStream in = new ObjectInputStream(fileIn);

				kgData = (HashMap<String, KGChunk>) in.readObject();

				in.close();
				fileIn.close();
				log.info(logPrefix + "Loaded data file");
				file.delete();
			} catch (Exception e) {
				// e.printStackTrace();
				log.info(logPrefix + "Error while loading file");
			}
		} else {
			log.info(logPrefix + "No dava file to load");
		}
		log.info(logPrefix + "Plugin enabled");
	}
}