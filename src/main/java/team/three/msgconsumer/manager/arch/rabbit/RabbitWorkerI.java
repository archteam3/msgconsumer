package team.three.msgconsumer.manager.arch.rabbit;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import team.three.msgconsumer.business.SampleBusiness;
import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.manager.status.StatusManager;
import team.three.msgconsumer.message.MsgHeader;

public class RabbitWorkerI extends Thread {
	private Channel channel;
	private String queName;
	
	public RabbitWorkerI() {}
	
	public void make(Connection con, List<String> topicLst) throws Exception {
		ConfigManager cm = ConfigManager.get();
		channel = con.createChannel();
		channel.exchangeDeclare(cm.getIndiv().get("exchange_name"), "topic");
		queName = topicLst.get(0).toLowerCase(Locale.KOREAN);
		channel.queueDeclare(queName, false, false, true, null);
		for(String k : topicLst) {
			channel.queueBind(queName, cm.getIndiv().get("exchange_name"), k);
		}
	}
	
	public void run() {
		try {
			SampleBusiness sb = new SampleBusiness();
			Map<String, Object> args = new HashMap<>();
			args.put("x-priority", StatusManager.get().getPriority());
			channel.basicConsume(queName, false, args, new DefaultConsumer(channel){
				@Override
				public void handleDelivery(String consumerTag,
						Envelope envelope,
						AMQP.BasicProperties properties,
						byte[] body) throws IOException {
					MsgHeader hdr = new MsgHeader(body);
					sb.bizMain(hdr, body);
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
