package team.three.msgconsumer.manager.arch;

import java.util.List;

public interface IMsgBroker {
	void connect() throws Exception;
	void disconnect();
	
	Thread createArchOneThread(List<String> eqpLst) throws Exception;
	Thread createArchTwoThread(List<String> eqpLst) throws Exception;
}
