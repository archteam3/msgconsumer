package team.three.msgconsumer.manager.arch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.infinispan.Cache;

import team.three.msgconsumer.business.SampleBusiness;
import team.three.msgconsumer.manager.data.DataManager;
import team.three.msgconsumer.manager.status.StatusManager;
import team.three.msgconsumer.message.Msg;

public class TaskExecutor extends Thread {
	private ConcurrentLinkedQueue<Msg> que;
	private Cache<String, Integer> eCache;
	private SampleBusiness biz;
	
	private long cnt;
	
	public TaskExecutor() {
		que = new ConcurrentLinkedQueue<>();
		eCache = DataManager.get().getEqpCache();
		biz = new SampleBusiness();
		cnt = 0;
	}
	
	public void put(Msg msg) {
		que.offer(msg);
	}
	
	public long getCnt() {
		return cnt;
	}
		
	public void run( ) {
		StatusManager sm = StatusManager.get();
		int lastIdx;
		try {
			while(true) {
				Msg msg = que.poll();
				if( msg != null ) {
					while(true) {
						if( sm.isMaster() ) {
							cnt++;
							biz.bizMain(msg.hdr, msg.body);
							eCache.put(msg.hdr.eqpId, msg.hdr.index);
							break;
						} else {
							lastIdx = eCache.get(msg.hdr.eqpId);
							if( lastIdx < msg.hdr.index ) {
								Thread.sleep(10);
							} else {
								break;
							}
						}
					}
				} else {
					Thread.sleep(10);
				}
			}
		} catch ( InterruptedException ie ) {
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
