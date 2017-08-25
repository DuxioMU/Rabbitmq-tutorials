package com.rabbitmq.client;

import java.io.IOException;
/**
 * �����Զ���������
 * ����������,����Ҳ���ᶪʧ
 * @author ASUS
 *
 */
public class Worker {
	
	private static final String TASK_QUEUE_NAME = "task_queue";
	
	public static void main(String[] argv)throws Exception {
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		final Connection connection = factory.newConnection();
		final Channel channel = connection.createChannel();
		//�ڶ�������Ϊtrue��֤����Ϣ���;���,��ʹRabbitMQ����/����,��ϢҲ���ᶪʧ
		channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
		System.out.println("[x] Waiting for messages. To exit press CTRC+C");
		
		//��ƽ����,���������Ϣ��һ������.
		channel.basicQos(1);
		
		final Consumer consumer = new DefaultConsumer(channel) {
			
			public void handleDelivery(String consumerTag,
					Envelope envelope,
					AMQP.BasicProperties properties,
					byte[] body)throws IOException {
				
				String message = new String(body,"UTF-8");
				
				System.out.println("[x] Received '" +message+"'");
				try {
					doWork(message);
				}finally {
					System.out.println("[x] Done");
					//��ϢӦ��Ĭ�� true
					//work�����󲻾�����δȷ�ϵ���Ϣ��������
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		channel.basicConsume(TASK_QUEUE_NAME,false,consumer);
	}

	protected static void doWork(String task) {
		
		for(char ch:task.toCharArray()) {
			if(ch == '.') {
				try {
					Thread.sleep(1000);
				}catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
