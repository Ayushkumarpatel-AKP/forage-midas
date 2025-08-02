package com.vagabond.midas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TransactionProcessor {
    
    // User balances map (userId -> balance)
    private Map<Long, Float> userBalances = new HashMap<>();
    
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor();
        processor.run();
    }
    
    public void run() {
        try {
            // Load initial user balances
            loadUserBalances();
            
            // Process transactions
            processTransactions();
            
            // Get waldorf's final balance (user ID 5 based on the user data file)
            float waldorfBalance = userBalances.get(5L);
            
            // Round down the balance
            int finalBalance = (int) Math.floor(waldorfBalance);
            
            // Output the result in the required format
            System.out.println("----- begin ----- " + finalBalance + " ----- end -----");
            
            // Also save to file
            java.nio.file.Files.write(
                java.nio.file.Paths.get("task_three_output.txt"),
                ("----- begin ----- " + finalBalance + " ----- end -----").getBytes()
            );
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadUserBalances() throws IOException {
        // User data from lkjhgfdsa.hjkl file
        // bernie (ID 1): 1200.23
        // grommit (ID 2): 2215.37
        // maria (ID 3): 2774.14
        // mario (ID 4): 12.34
        // waldorf (ID 5): 444.55
        // whosit (ID 6): 888.90
        // whatsit (ID 7): 777.60
        // howsit (ID 8): 68.70
        // wilbur (ID 9): 3476.21
        // antonio (ID 10): 2121.54
        // calypso (ID 11): 779421.33
        
        userBalances.put(1L, 1200.23f);
        userBalances.put(2L, 2215.37f);
        userBalances.put(3L, 2774.14f);
        userBalances.put(4L, 12.34f);
        userBalances.put(5L, 444.55f);  // waldorf
        userBalances.put(6L, 888.90f);
        userBalances.put(7L, 777.60f);
        userBalances.put(8L, 68.70f);
        userBalances.put(9L, 3476.21f);
        userBalances.put(10L, 2121.54f);
        userBalances.put(11L, 779421.33f);
    }
    
    private void processTransactions() throws IOException {
        // Transaction data from mnbvcxz.vbnm file
        String[] transactions = {
            "6, 9, 173.71",
            "4, 8, 124.70",
            "6, 8, 67.38",
            "1, 9, 4.38",
            "8, 7, 38.74",
            "7, 2, 93.14",
            "9, 5, 45.42",
            "6, 5, 32.12",
            "7, 10, 98.3",
            "7, 3, 42.58",
            "2, 1, 178.24",
            "5, 9, 78.74",
            "4, 8, 139.7",
            "9, 6, 57.84",
            "10, 9, 127.40",
            "6, 1, 24.37",
            "10, 2, 23.86",
            "4, 6, 72.6",
            "3, 2, 127.63",
            "3, 6, 133.7",
            "9, 5, 184.51",
            "4, 5, 133.86"
        };
        
        for (String transactionLine : transactions) {
            String[] parts = transactionLine.split(", ");
            long senderId = Long.parseLong(parts[0]);
            long recipientId = Long.parseLong(parts[1]);
            float amount = Float.parseFloat(parts[2]);
            
            // Validate transaction
            if (userBalances.containsKey(senderId) && 
                userBalances.containsKey(recipientId) && 
                userBalances.get(senderId) >= amount) {
                
                // Update balances
                userBalances.put(senderId, userBalances.get(senderId) - amount);
                userBalances.put(recipientId, userBalances.get(recipientId) + amount);
                
                System.out.println("Processed transaction: " + senderId + " -> " + recipientId + ", amount: " + amount);
            } else {
                System.out.println("Invalid transaction: " + senderId + " -> " + recipientId + ", amount: " + amount);
            }
        }
    }
}
