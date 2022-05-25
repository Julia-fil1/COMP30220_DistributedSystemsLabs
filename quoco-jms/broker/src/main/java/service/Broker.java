package service;

import org.apache.activemq.ActiveMQConnectionFactory;
import service.core.ClientInfo;
import service.core.Quotation;
import service.message.ClientApplicationMessage;
import service.message.QuotationRequestMessage;
import service.message.QuotationResponseMessage;

import javax.jms.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Broker {
    private static Map<Long, ClientInfo> cache = new HashMap<Long, ClientInfo>();
    private static Connection connection;

    public static void main(String[] args) throws JMSException {
        String host = args.length > 0 ? args[0] : "localhost";
        ConnectionFactory factory =
                new ActiveMQConnectionFactory("failover://tcp://" + host + ":61616");
        connection = factory.createConnection();
        connection.setClientID("broker");


        Runnable clientThread = new Runnable() {
            @Override
            public void run() {
                try {
                    Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

                    Queue requestQueue = session.createQueue("REQUESTS");
                    Topic topic = session.createTopic("APPLICATIONS");
                    MessageProducer producer = session.createProducer(topic);
                    MessageConsumer consumer = session.createConsumer(requestQueue);


                    connection.start();

                    while (true) {
                        Message message = consumer.receive();
                        if (message instanceof ObjectMessage) {
                            System.out.println("Successfully received message");
                            Object content = ((ObjectMessage) message).getObject();
                            if (content instanceof QuotationRequestMessage) {
                                QuotationRequestMessage request = (QuotationRequestMessage) content;
                                producer.send(message);
                                cache.put(request.id, request.info);
                            }
                        } else {
                            System.out.println("Unknown message type: " +
                                    message.getClass().getCanonicalName());
                        }

                    }

                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };

        Runnable brokerThread = new Runnable() {
            @Override
            public void run() {
                try {
                    Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

                    Queue quotationsQueue = session.createQueue("QUOTATIONS");
                    Topic applicationsTopic = session.createTopic("RESPONSES");

                    MessageConsumer consumer = session.createConsumer(quotationsQueue);
                    MessageProducer producer = session.createProducer(applicationsTopic);


                    connection.start();

                    while (true) {
                        long t1 = 0;
                        ArrayList<Quotation> quotations = new ArrayList<>();
                        long id = -1;
                        while (t1 != 3) {
                            Message message = consumer.receive();
                            if (message instanceof ObjectMessage) {
                                System.out.println("Broker thread " + id + " - message received");
                                Object content = ((ObjectMessage) message).getObject();
                                if (content instanceof QuotationResponseMessage) {
                                    QuotationResponseMessage response = (QuotationResponseMessage) content;
                                    System.out.println(response.id);
                                    if ((id == -1) || (id == response.id)) {
                                        message.acknowledge();
                                        quotations.add(response.quotation);
                                        id = response.id;
                                        t1++;
                                    }

                                }
                            } else {
                                System.out.println("Unknown message type: " + message.getClass().getCanonicalName());
                            }
                        }

                        System.out.println(quotations);
                        Message application = session.createObjectMessage(new ClientApplicationMessage(id, cache.get(id), quotations));
                        producer.send(application);
                        System.out.println("Thread response sent, broker " + id + " finished");
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread cThread = new Thread(clientThread);
        Thread bThread = new Thread(brokerThread);
        cThread.start();
        bThread.start();
    }
}