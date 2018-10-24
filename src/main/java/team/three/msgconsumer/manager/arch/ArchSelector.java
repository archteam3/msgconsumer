package team.three.msgconsumer.manager.arch;

import team.three.msgconsumer.manager.arch.rabbit.RabbitMsgBroker;
import team.three.msgconsumer.manager.config.BrokerType;
import team.three.msgconsumer.manager.config.ConfigManager;

public class ArchSelector {
	private static ArchSelector instance = null;
	public static ArchSelector get() {
		if( instance == null ) {
			synchronized (ArchSelector.class) {
				if( instance == null ) {
					instance = new ArchSelector();
				}
			}
		}
		return instance;
	}
	
	private ArchSelector() { }
	
	private IArchitecture arch = null;
	private IMsgBroker broker = null;
	
	public void build() throws Exception {
		
		ConfigManager cm = ConfigManager.get();
		
		if( cm.getBrokerType() == BrokerType.KAFKA ) {
			throw new Exception("Kafka is not work yet!");
		} else if( cm.getBrokerType() == BrokerType.NATS ) {
			throw new Exception("NATS is not work yet!");
		} else if( cm.getBrokerType() == BrokerType.RabbitMQ ) {
			if( cm.getArchType() == 1 ) {
				broker = new RabbitMsgBroker();
				arch = new ArchitectureOne();
			} else {
				
			}
		}
		broker.connect();
		arch.build(broker);
		
	}
}
