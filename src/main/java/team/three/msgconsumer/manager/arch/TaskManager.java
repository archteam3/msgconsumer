package team.three.msgconsumer.manager.arch;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.message.Msg;
import team.three.msgconsumer.props.IdMaker;

public class TaskManager {
	private static TaskManager instance;
	public static TaskManager get() {
		if( instance == null ) {
			synchronized (TaskManager.class) {
				if( instance == null )
					instance = new TaskManager();
			}
		}
		return instance;
	}
	
	private Map<String, TaskExecutor> thds;
	private AtomicLong al;
	
	public void init() {
		ConfigManager cm = ConfigManager.get();
		thds = new ConcurrentHashMap<String, TaskExecutor>();
		for( int i=cm.getEqpSIdx(); i<=cm.getEqpEIdx(); i++) {
			TaskExecutor te = new TaskExecutor();
			thds.put(IdMaker.makeEqpId(i), te);
			te.start();
		}
		al = new AtomicLong(0);
	}
	
	public void disconnect() {
		Iterator<String> keys = thds.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			thds.get(key).interrupt();
		}
	}
	
	public void msg(byte[] body) {
		Msg msg = new Msg(body);
		TaskExecutor te = thds.get(msg.hdr.eqpId);
		al.incrementAndGet();
		te.put(msg);
	}
	
	public void printCnt() {
		Iterator<String> keys = thds.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			System.out.println("#key:" + key + "  cnt:" + thds.get(key).getCnt());
		}
		System.out.println("=== Total pass cnt : " + al.get());
	}
}
