package team.three.msgconsumer.manager.status;

import java.util.concurrent.atomic.AtomicBoolean;

import team.three.msgconsumer.manager.data.DataManager;

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
	private int priority;
	
	public void setMaster() {
		master.set(true);
	}
	
	public boolean isMaster() {
		return master.get();
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
}
