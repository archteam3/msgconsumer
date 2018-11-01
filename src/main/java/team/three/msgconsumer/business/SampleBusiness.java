package team.three.msgconsumer.business;

import org.infinispan.Cache;

import team.three.msgconsumer.manager.data.DataManager;
import team.three.msgconsumer.message.MsgHeader;

public class SampleBusiness {
	private Cache<String, Object> dCache;
	
	public SampleBusiness() {
		dCache = DataManager.get().getDataCache();
	}
	
	public void bizMain(MsgHeader hdr, byte[] body) {
		BizItem bi = (BizItem)dCache.get(hdr.eqpId);
		System.out.println(hdr.eqpId + "  : get Cache : " + bi);
		if( bi == null ) {
			bi = new BizItem();
		}
		bi.doit(hdr);
		dCache.put(hdr.eqpId, bi);
		
		System.out.println(hdr.eqpId + "  : " + bi.toString());
	}
}
