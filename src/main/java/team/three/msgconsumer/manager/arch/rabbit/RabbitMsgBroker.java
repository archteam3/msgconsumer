package team.three.msgconsumer.manager.arch.rabbit;

import java.util.List;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import team.three.msgconsumer.manager.arch.IMsgBroker;
import team.three.msgconsumer.manager.config.ConfigManager;

public class RabbitMsgBroker implements IMsgBroker {
	private ConfigManager cm;
	private Connection connection;
	@Override
	public void connect() throws Exception {
		// TODO Auto-generated method stub
		ConfigManager cm = ConfigManager.get();
		ConnectionFactory cf = new ConnectionFactory();
		cf.setHost(cm.getIndiv().get("server_ip"));
		cf.setUsername(cm.getIndiv().get("id"));
		cf.setPassword(cm.getIndiv().get("pw"));
		
		connection = cf.newConnection();
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			connection.close();
		} catch (Exception ex ) {
			ex.printStackTrace();
		}
	}

	@Override
	public Thread createArchOneThread(List<String> eqpLst)  throws Exception {
		// TODO Auto-generated method stub
		RabbitWorkerI rwi = new RabbitWorkerI();
		rwi.make(connection, eqpLst);
		return rwi;
	}

	@Override
	public Thread createArchTwoThread(List<String> eqpLst)  throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
