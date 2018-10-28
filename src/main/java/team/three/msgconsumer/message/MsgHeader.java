package team.three.msgconsumer.message;

import java.util.Arrays;

public class MsgHeader {
	public final long nanoTime;
	public final String eqpId;
	public final int index;
	
	public MsgHeader(byte[] body) {
		nanoTime = Long.parseLong(new String(Arrays.copyOfRange(body, 0, 20)).trim());
		eqpId = new String(Arrays.copyOfRange(body, 21, 30)).trim();
		index = Integer.parseInt(new String(Arrays.copyOfRange(body, 31, 40)).trim());
	}
}
