package com.vagabond.midas;

import com.jpmc.midascore.foundation.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class SimpleTransactionTest {
    
    @Autowired
    private KafkaListenerConfig kafkaListener;
    
    @Test
    void simpleTransactionTest() {
        // Create a simple transaction
        Transaction transaction = new Transaction(5, 9, 10.0f);
        
        // Process the transaction
        kafkaListener.listen(transaction);
        
        System.out.println("Simple transaction test completed");
    }
}
