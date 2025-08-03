package com.vagabond.midas;

import com.jpmc.midascore.foundation.Balance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class TaskFiveRunner {
    public static void main(String[] args) {
        // Start the Spring Boot application
        ConfigurableApplicationContext context = SpringApplication.run(TaskFiveRunner.class, args);
        
        try {
            // Create RestTemplate for making HTTP requests
            RestTemplate restTemplate = new RestTemplate();
            
            // Query balances for userIds 0-12 and build output
            StringBuilder output = new StringBuilder("\n---begin output ---\n");
            for (int i = 0; i < 13; i++) {
                String url = "http://localhost:33400/balance?userId=" + i;
                Balance balance = restTemplate.getForObject(url, Balance.class);
                output.append(balance.toString()).append("\n");
            }
            output.append("---end output ---\n");
            
            // Print the output
            System.out.println(output.toString());
            
            // Write to task_five_output.txt
            java.nio.file.Files.write(
                java.nio.file.Paths.get("task_five_output.txt"),
                output.toString().getBytes()
            );
            
            System.out.println("Output written to task_five_output.txt");
        } catch (Exception e) {
            System.err.println("Error querying balances: " + e.getMessage());
            e.printStackTrace();
        } finally {
            context.close();
        }
    }
}
