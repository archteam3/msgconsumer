package team.three.msgconsumer.manager.arch.kafka;

import java.util.List;

import team.three.msgconsumer.manager.arch.IMsgBroker;

public class KafkaMsgBroker implements IMsgBroker {

	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public Thread createArchOneThread(List<String> eqpLst) throws Exception {
		// TODO Auto-generated method stub
		KafkaWorkerI kwi = new KafkaWorkerI();		
		return kwi;
	}

	@Override
	public Thread createArchTwoThread(List<String> eqpLst) throws Exception {
		// TODO Auto-generated method stub
		KafkaWorkerII kwi = new KafkaWorkerII();
		return kwi;
	}

}
