package com.example.demo.processors;

import com.example.demo.consumers.KafkaMessageConsumer;
import com.example.demo.KafkaMessageProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * @author Wilfred Bett
 */
public class KafkaMessageProcessor extends KafkaMessageConsumer {
    public KafkaMessageProcessor(String topicName) {
        super(topicName);
    }

    @Override
    public void run() {
        while(!isInterrupted()) {
            // Pull messages from request-topic and send to response-topic
            final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(1000);
            for(ConsumerRecord<String, String> record : consumerRecords.records(responseTopic)) {
                System.out.println("[Processor]Received message with key: " + record.key());
                KafkaMessageProducer.getInstance().sendMessage(RESPONSE_TOPIC_NAME, record.key(), record.value());
            }
        }
    }
}
