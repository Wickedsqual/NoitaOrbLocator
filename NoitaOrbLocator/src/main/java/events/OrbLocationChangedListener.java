package events;

import java.util.EventListener;

import infoClasses.OrbLocation;

public interface OrbLocationChangedListener extends EventListener {

	public void onOrbLocationChanged(OrbLocation orbLocation);
}