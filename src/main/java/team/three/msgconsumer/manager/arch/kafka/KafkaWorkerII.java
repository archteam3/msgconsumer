package team.three.msgconsumer.manager.arch.kafka;

import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import team.three.msgconsumer.manager.arch.TaskManager;

public class KafkaWorkerII extends Thread {
	public void run() {
		KafkaConsumer<String, byte[]> consumer = null;
		TaskManager tm = TaskManager.get();
		try {
			Properties props = new Properties();
		    props.put("bootstrap.servers", "localhost:9092");
		    props.put("group.id", "test");
		    props.put("enable.auto.commit", "true");
		    props.put("auto.commit.interval.ms", "1000");
		    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		    props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		    consumer = new KafkaConsumer<>(props);
		    Pattern ptn = Pattern.compile("EQP-*");
		    consumer.subscribe(ptn);
		    while (true) {
		        ConsumerRecords<String, byte[]> records = consumer.poll(20);
		        for (ConsumerRecord<String, byte[]> record : records)
		        	tm.msg(record.value());
		    }	
		} catch ( Exception ie ) {
			ie.printStackTrace();
		} finally {
			consumer.close();
		}
	}
}
