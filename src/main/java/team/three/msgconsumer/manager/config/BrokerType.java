package team.three.msgconsumer.manager.config;

import java.util.Locale;

public enum BrokerType {
	KAFKA("Kafka"),
	NATS("NATS"),
	RabbitMQ("RabbitMQ")
	;
	private String name;
	
	BrokerType(String text){
		this.name = text;
	}
	
	public String getString() {
		return this.name;
	}
	
	public String getUpperString() {
		return this.name().toUpperCase(Locale.KOREAN);
	}
}
