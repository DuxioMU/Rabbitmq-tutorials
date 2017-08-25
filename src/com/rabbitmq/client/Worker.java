package com.rabbitmq.client;

import java.io.IOException;
/**
 * 工作对队列消费者
 * 消费者死亡,任务也不会丢失
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
		//第二个参数为true保证了消息的耐久性,即使RabbitMQ崩溃/重启,消息也不会丢失
		channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
		System.out.println("[x] Waiting for messages. To exit press CTRC+C");
		
		//公平分配,不给多个消息到一个工人.
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
					//消息应答默认 true
					//work死亡后不久所有未确认的消息将被发送
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
