package main;

import java.awt.Component;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.event.EventListenerList;

import events.PlayerFileChangedListener;

public class PlayerFileWatcher implements Closeable {

	protected EventListenerList listenerList = new EventListenerList();

	private boolean stopFileWatching = false;
	private Thread saveFileWatcherThread;
	private WatchKey watchKey;
	WatchService watchService;

	public void init(Component parent, PlayerFileChangedListener playerFileChangedListener) throws IOException {

		addPlayerFileChangedListener(playerFileChangedListener);

		if (!FileHelper.getInstance().getPlayerFile().exists()) {

			JFileChooser fileChooser = new JFileChooser();

			fileChooser.setCurrentDirectory(FileHelper.getInstance().getUserHomeFile());
			int result = fileChooser.showOpenDialog(parent);

			if (result == JFileChooser.APPROVE_OPTION) {
				FileHelper.getInstance().setPlayerFile(fileChooser.getSelectedFile());
			} else {
				System.exit(0);
			}
		}

		watchService = FileSystems.getDefault().newWatchService();
		watchKey = FileHelper.getInstance().getFirstSaveFolderFile().toPath().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

		saveFileWatcherThread = new Thread() {

			@Override
			public void run() {
				while (!stopFileWatching) {
					try {
						watchKey = watchService.poll(1, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						stopFileWatching = true;
					}
					if (watchKey != null) {
						watchKey.pollEvents().stream().forEach(event -> processFolderEvents(event));
						watchKey.reset();
					}
				}
			}

		};
		saveFileWatcherThread.start();
	}

	public void addPlayerFileChangedListener(PlayerFileChangedListener playerFileChangedListener) {
		listenerList.add(PlayerFileChangedListener.class, playerFileChangedListener);
	}

	public void removePlayerFileChangedListener(PlayerFileChangedListener playerFileChangedListener) {
		listenerList.remove(PlayerFileChangedListener.class, playerFileChangedListener);
	}

	private void processFolderEvents(WatchEvent<?> event) {
		final Path changed = (Path) event.context();
		if (changed.endsWith(FileHelper.getInstance().getPlayerFile().getName())) {
			firePlayerFileChangedEvent(FileHelper.getInstance().getPlayerFile());
		}
	}

	private void firePlayerFileChangedEvent(File playerFile) {
		Object[] listeners = listenerList.getListenerList();

		for (Object listener : listeners) {
			if (listener instanceof PlayerFileChangedListener) {
				((PlayerFileChangedListener) listener).onPlayerFileChanged(playerFile);
			}
		}
	}

	@Override
	public void close() throws IOException {
		stopFileWatching = true;

		if (saveFileWatcherThread != null) {
			// TODO Check? --> add stop?
		}

	}
}
