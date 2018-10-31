package team.three.msgconsumer.manager.arch.kafka;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import team.three.msgconsumer.manager.arch.TaskManager;
import team.three.msgconsumer.manager.config.ConfigManager;

public class KafkaWorkerII extends Thread {
	public void run() {
		KafkaConsumer<String, byte[]> consumer = null;
		TaskManager tm = TaskManager.get();
		try {
			Properties props = new Properties();
			Map<String, String> cMap = ConfigManager.get().getIndiv();
			Iterator<String> keys = cMap.keySet().iterator();
			while( keys.hasNext() ) {
				String key = keys.next();
				props.put(key, cMap.get(key));
			}
		    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		    props.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		    consumer = new KafkaConsumer<>(props);
		    Pattern ptn = Pattern.compile("EQP-*");
		    consumer.subscribe(ptn);
		    while (true) {
		        ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
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
