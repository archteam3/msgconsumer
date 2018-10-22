package team.three.msgconsumer.manager.status;

import java.util.concurrent.atomic.AtomicBoolean;

public class StatusManager {
	private static StatusManager instance = null;
	public static StatusManager get() {
		if( instance == null ) {
			synchronized (StatusManager.class) {
				if( instance == null ) {
					instance = new StatusManager();
				}
			}
		}
		return instance;
	}
	private StatusManager() { 
		master = new AtomicBoolean(false);
	}
	
	private AtomicBoolean master;
	
	public void setMaster() {
		master.set(true);
	}
	
	public boolean isMaster() {
		return master.get();
	}
}
