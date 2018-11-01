package team.three.msgconsumer.business;

import java.io.Serializable;

import team.three.msgconsumer.message.MsgHeader;

public class BizItem  implements Serializable {

	private int prevIdx;
	private int totalCnt;
	private int abnormalCnt;
	private long elapsedTotal;
	
	public BizItem() {
		init();
	}
	
	public void init() {
		prevIdx = 0;
		totalCnt = 0;
		abnormalCnt = 0;
		elapsedTotal = 0;
	}
	
	public int getTotalCnt() {
		return totalCnt;
	}
	
	public int getAbnormalCnt() {
		return abnormalCnt;
	}
	
	public long getElapsedNano() {
		return elapsedTotal;		
	}
	
	public long doit(MsgHeader hdr) {
		long ret = 0L;
		totalCnt++;
		if( hdr.index <= prevIdx ) {
			abnormalCnt++;
		}
		prevIdx = hdr.index;
		ret = System.nanoTime() - hdr.nanoTime;
		elapsedTotal += ret / 1000000;
		return ret;
	}

	@Override
	public String toString() {
		return "BizItem [prevIdx=" + prevIdx + ", totalCnt=" + totalCnt + ", abnormalCnt=" + abnormalCnt
				+ ", elapsedTotal=" + elapsedTotal + "]";
	}
}
