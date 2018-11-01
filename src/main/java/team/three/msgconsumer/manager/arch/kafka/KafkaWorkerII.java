package team.three.msgconsumer.manager.arch.kafka;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import team.three.msgconsumer.manager.arch.TaskManager;
import team.three.msgconsumer.manager.config.ConfigManager;
import team.three.msgconsumer.props.IdMaker;

public class KafkaWorkerII extends Thread {
	public void run() {
		KafkaConsumer<String, byte[]> consumer = null;
		TaskManager tm = TaskManager.get();
		List<String> topicArr = new ArrayList<>();
		for( int i=ConfigManager.get().getEqpSIdx(); i<=ConfigManager.get().getEqpEIdx(); i++) {
			topicArr.add(IdMaker.makeEqpId(i));
		}
		
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
		    //Pattern ptn = Pattern.compile("EQP-*");
		    consumer.subscribe(topicArr);
		    long cnt = 0L;
		    long bCnt = 0L;
		    long rCnt = 0L;
		    while (true) {
		        ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(50));
		        cnt = cnt + records.count();
		        for (ConsumerRecord<String, byte[]> record : records) {
		        	//rCnt++;
		        	tm.msg(record.value());
		        }
		        /*
		        if( bCnt != cnt) {
		        	System.out.println("> cnt : " + cnt + " rCnt : " + rCnt);
		        	bCnt = cnt;
		        }
		        */
		    }	
		} catch ( Exception ie ) {
			ie.printStackTrace();
		} finally {
			consumer.close();
		}
	}
}
