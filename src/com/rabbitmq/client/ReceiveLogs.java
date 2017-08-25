package com.rabbitmq.client;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
/**
 * ��־��¼ϵͳ
 * Exchange ����ʱ���а����ڽ��ն�����Ϣ
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
		//�󶨶��к�ѡ���� ������������·��
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
