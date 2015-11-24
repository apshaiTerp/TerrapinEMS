package ac.ems.ambulance.terrapinems.rabbit;

import android.content.Context;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by ac010168 on 11/13/15.
 */
public class SimpleRabbitMQ {

    private final static String EXCHANGE_NAME = "amq.topic";
    private final static String QUEUE_NAME    = "emsqueue";
    private static String ROUTING_KEY;

    private final static String CONSUMER_TAG  = "ambConsumer";

    private ConnectionFactory factory;
    private Connection        conn;
    private Channel           myChannel;

    public SimpleRabbitMQ(long ambulanceID) {
        ROUTING_KEY = "/amb" + ambulanceID;
        //ROUTING_KEY = "amb50034";

        factory   = null;
        conn      = null;
        myChannel = null;

        initConnection();
    }

    public void initConnection() {
        factory = new ConnectionFactory();

        factory.setUsername("ems");
        factory.setPassword("emsrabbit1");
        factory.setVirtualHost("/ems");
        factory.setHost("107.188.249.238");
        factory.setPort(5672);

        try {
            conn = factory.newConnection();
        } catch (Throwable t) {
            System.out.println ("Something bad happened with newConnection here:");
            t.printStackTrace();
            conn = null;
            return;
        }

        try {
            myChannel = conn.createChannel();
        } catch (Throwable t) {
            System.out.println ("Something bad happened opening the channel:");
            t.printStackTrace();
            try { conn.close(); } catch (Throwable ignoreT) { /** Ignore me */ }
            conn = null;
            return;
        }

        System.out.println("I'm open, and I have a channel!");

        //Now we need to setup the channel to point where we want
        try {
            myChannel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
            myChannel.queueDeclare(QUEUE_NAME, true, false, false, null);
            myChannel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
            myChannel.basicQos(1);

            System.out.println ("The queueName generated: " + QUEUE_NAME);

            System.out.println ("So far so good!");
        } catch (Throwable t) {
            System.out.println ("I'm having issues with the create queue binding stuff:");
            t.printStackTrace();
        }

        //Now that we've established the channel queue, now we need to subscribe for messages
        boolean autoAck = false;
        try {

            myChannel.basicConsume(QUEUE_NAME, autoAck, CONSUMER_TAG,
                    new DefaultConsumer(myChannel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String routingKey = envelope.getRoutingKey();
                            String contentType = properties.getContentType();
                            long deliveryTag = envelope.getDeliveryTag();
                            // (process the message components here ...)

                            System.out.println("I got a message!");
                            System.out.println("  The routingKey:  " + routingKey);
                            System.out.println ("  The contentType: " + contentType);
                            System.out.println ("  The deliveryTag: " + deliveryTag);

                            String messageContent = new String(body);

                            NotifyFromRabbitTask task = new NotifyFromRabbitTask(messageContent);
                            task.execute();

                            myChannel.basicAck(deliveryTag, false);
                        }
                    });

            System.out.println ("I have managed to subscribe!");
        } catch (Throwable t) {
            System.out.println ("Something went wrong inside the subscribe block");
            t.printStackTrace();
        }
    }

    public void closeConnection() {
        if (myChannel != null) {
            try {
                myChannel.basicCancel(CONSUMER_TAG);
                myChannel.close();
                conn.close();

                System.out.println ("I have shutdown successfully");
            } catch (Throwable t) {
                System.out.println ("Why won't you let me close things down!");
                t.printStackTrace();
            }
        }
    }

    public void publishMessage(String someMessage, long hospitalID) {
        byte[] messageBody = someMessage.getBytes();
        String routingKey  = "hosp" + hospitalID;

        if (myChannel != null) {
            try {
                myChannel.basicPublish(EXCHANGE_NAME, routingKey, null, messageBody);
            } catch (Throwable t) {
                System.out.println ("I had problems sending the message");
                t.printStackTrace();
            }
        }
    }

}
