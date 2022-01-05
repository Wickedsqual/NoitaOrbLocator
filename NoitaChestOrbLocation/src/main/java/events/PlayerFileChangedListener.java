package events;

import java.io.File;
import java.util.EventListener;

public interface PlayerFileChangedListener extends EventListener {

	public void onPlayerFileChanged(File playerFilePath);
}
