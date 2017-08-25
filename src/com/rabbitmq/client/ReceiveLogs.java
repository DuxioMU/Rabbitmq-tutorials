package com.rabbitmq.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * 日志记录系统
 * Exchange 与临时队列绑定用于接收订阅消息
 * @author ASUS
 */
public class ReceiveLogs {
	
	private static final String EXCHANGE_NAME = "logs";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		
		channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.FANOUT);
		String queueName = channel.queueDeclare().getQueue();
		//绑定队列和选择器 第三个参数是路由
		channel.queueBind(queueName, EXCHANGE_NAME,"");
		
		System.out.println("[*] Waiting for message .To exit press CTRL+C");
		
		Consumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag,Envelope envelope
					,AMQP.BasicProperties properties,byte[] body)throws IOException{
				String message = new String(body,"UTF-8");
				System.out.println("[x] Received '"+ message+"'");
			}
		};
		channel.basicConsume(queueName,true,consumer);
	}
}
