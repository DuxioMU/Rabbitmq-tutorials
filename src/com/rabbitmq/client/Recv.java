package com.rabbitmq.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class Recv {
	
	private final static String QUEUE_NAME ="hello";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername("guest");
		factory.setPassword("guest");
		factory.setVirtualHost("/");
		factory.setHost("192.168.41.131");
		factory.setPort(8080);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		System.out.println("[*] Waiting for messages .To exit press CTRL +C");
		
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag,Envelope envelop,AMQP.BasicProperties properties,byte[] body) throws UnsupportedEncodingException{
				String message = new String(body,"UTF-8");
				System.out.println("[x] Received'"+message+"'");
			}
		};
		channel.basicConsume(QUEUE_NAME,true,consumer);
	}
	
}
