package com.rabbitmq.client;
/**
 * 发送日志记录到Exchange
 * @author ASUS
 *
 */
public class EmitLog {
	private static final String EXCHANGE_NAME = "logs";
	
	 public static void main(String[] argv) throws Exception {
		    ConnectionFactory factory = new ConnectionFactory();
		    factory.setHost("localhost");
		    Connection connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		
		    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
		    
		    String message = getMessage(argv);
		    
		    //发送日志到RabbitMQ
		    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
		    System.out.println(" [x] Sent'"+message+"'");
		    
		    channel.close();
		    connection.close();
	 }
	 private static String getMessage(String[] strings){
		    if (strings.length < 1)
		    	    return "info: Hello World!";
		    return joinStrings(strings, " ");
		  }

		  private static String joinStrings(String[] strings, String delimiter) {
		    int length = strings.length;
		    if (length == 0) return "";
		    StringBuilder words = new StringBuilder(strings[0]);
		    for (int i = 1; i < length; i++) {
		        words.append(delimiter).append(strings[i]);
		    }
		    return words.toString();
		  }
}
