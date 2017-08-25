package com.rabbitmq.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {
	
	private final static String QUEUE_NAME = "hello";
	
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		String message = "Hello World!";
		for(int i=0 ;i<Integer.MAX_VALUE;i++) {
			channel.basicPublish("", QUEUE_NAME, null,message.getBytes("UTF-8"));
//			System.out.println("["+i+"] sent '"+message+"'"+i);
			Thread.sleep(1000);
		}
		channel.close();
		connection.close();
	}
}
