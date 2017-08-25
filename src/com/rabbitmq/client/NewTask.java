package com.rabbitmq.client;

/**
 * ��������
 * ���ڴ�������
 * @author ASUS
 *
 */
public class NewTask {
	
	private static final String TASK_QUEUE_NAME = "task_queue";
	
	public static void main(String[] argv)throws Exception{
		
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		//�ڶ�������:���������;���,RabbitMQ����/����/��ϢҲ���ᶪʧ.
		channel.queueDeclare(TASK_QUEUE_NAME,true,false,false,null);
		
		String message = getMessage(argv);
		
		channel.basicPublish("",TASK_QUEUE_NAME,
				MessageProperties.PERSISTENT_TEXT_PLAIN,
				message.getBytes("UTF-8"));
		
		System.out.print("[x] Sent '" + message+"'");
		channel.close();
		connection.close();
		
		
	}

	private static String getMessage(String[] argv) {
		if(argv.length <1) {
			return "Hello World!";
		}
		return joinString(argv,"");
	}

	private static String joinString(String[] argv, String string) {
		int length = argv.length;
		if(length == 0 )return "";
		StringBuilder words = new StringBuilder(argv[0]);
		for(int i = 1 ;i < length;i++) {
			words.append(string).append(argv[i]);
		}
		return words.toString();
	}
}
