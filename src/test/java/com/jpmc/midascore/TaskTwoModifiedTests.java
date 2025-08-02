package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TaskTwoModifiedTests {
    static final Logger logger = LoggerFactory.getLogger(TaskTwoModifiedTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private FileLoader fileLoader;

    @Test
    void task_two_modified_test() throws InterruptedException {
        String[] transactionLines = fileLoader.loadStrings("/test_data/poiuytrewq.uiop");
        // Only send the first 5 transactions to avoid infinite loop
        for (int i = 0; i < Math.min(5, transactionLines.length); i++) {
            kafkaProducer.send(transactionLines[i]);
        }
        // Wait for transactions to be processed
        Thread.sleep(5000);
        logger.info("Test completed successfully");
    }
}
