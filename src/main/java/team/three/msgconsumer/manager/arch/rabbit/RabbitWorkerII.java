package team.three.msgconsumer.manager.arch.rabbit;

import java.io.IOException;
import java.util.List;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import team.three.msgconsumer.manager.arch.TaskManager;
import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.props.IdMaker;

public class RabbitWorkerII extends Thread {
	private Channel channel;
	private String queName;
	
	public RabbitWorkerII() {}
	
	public void make(Connection con, List<String> topicLst) throws Exception {
		ConfigManager cm = ConfigManager.get();
		channel = con.createChannel();
		channel.exchangeDeclare(cm.getIndiv().get("exchange_name"), "topic");
		queName = cm.getMachineId();
		channel.queueDeclare(queName, true, false, true, null);
		for(String k : topicLst) {
			channel.queueBind(queName, cm.getIndiv().get("exchange_name"), k);
		}
	}
		
	public void run() {
		try {
			TaskManager tm = TaskManager.get();
			channel.basicConsume(queName, true, new DefaultConsumer(channel){
				@Override 
				public void handleDelivery(String consumerTag,
						Envelope envelope,
						AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					tm.msg(body);
				}
			});
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
