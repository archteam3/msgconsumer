package team.three.msgconsumer.message;

public class Msg {
	public MsgHeader hdr;
	public byte[] body;
	
	public Msg(byte[] body) {
		this.body = body;
		hdr = new MsgHeader( this.body );
	}
}
