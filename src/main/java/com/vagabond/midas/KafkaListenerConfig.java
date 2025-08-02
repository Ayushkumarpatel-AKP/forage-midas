package com.vagabond.midas;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Component
public class KafkaListenerConfig {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    private int transactionCount = 0;
    private float[] firstFourAmounts = new float[4];
    
    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-group", 
        properties = {
            "spring.json.trusted.packages=com.jpmc.midascore.foundation",
            "spring.json.value.default.type=com.jpmc.midascore.foundation.Transaction"
        })
    public void listen(Transaction transaction) {
        System.out.println(transaction.toString());
        
        // Validate transaction
        if (validateTransaction(transaction)) {
            // Call incentives API
            Incentive incentive = callIncentivesAPI(transaction);
            
            // Record valid transaction with incentive
            TransactionRecord transactionRecord = new TransactionRecord(
                transaction.getSenderId(), 
                transaction.getRecipientId(), 
                transaction.getAmount()
            );
            if (incentive != null) {
                transactionRecord.setIncentive(incentive.getAmount());
            }
            transactionRepository.save(transactionRecord);
            
            // Update user balances with incentive
            updateUserBalancesWithIncentive(transaction, incentive);
        }
        
        // Capture the first four transaction amounts (for Task Two compatibility)
        if (transactionCount < 4) {
            firstFourAmounts[transactionCount] = transaction.getAmount();
            transactionCount++;
            
            // Print the amounts when we have all four
            if (transactionCount == 4) {
                System.out.println("----- begin ----- [" + firstFourAmounts[0] + ", " + firstFourAmounts[1] + ", " + firstFourAmounts[2] + ", " + firstFourAmounts[3] + "] ----- end -----");
            }
        }
    }
    
    private boolean validateTransaction(Transaction transaction) {
        // Check if sender and recipient IDs are valid
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());
        
        if (!senderOpt.isPresent() || !recipientOpt.isPresent()) {
            System.out.println("Invalid sender or recipient ID");
            return false;
        }
        
        UserRecord sender = senderOpt.get();
        // We don't need to store the recipient in a variable since we're not using it here
        // Just verify that the recipient exists
        
        // Check if sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance for sender ID: " + transaction.getSenderId());
            return false;
        }
        
        return true;
    }
    
    private Incentive callIncentivesAPI(Transaction transaction) {
        try {
            String incentivesApiUrl = "http://localhost:8080/incentive";
            Incentive incentive = restTemplate.postForObject(incentivesApiUrl, transaction, Incentive.class);
            return incentive;
        } catch (Exception e) {
            System.out.println("Error calling incentives API: " + e.getMessage());
            return null;
        }
    }
    
    private void updateUserBalancesWithIncentive(Transaction transaction, Incentive incentive) {
        // For Task Four, we don't deduct from sender, only add to recipient with incentive
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());
        if (recipientOpt.isPresent()) {
            UserRecord recipient = recipientOpt.get();
            float totalAmount = transaction.getAmount();
            if (incentive != null) {
                totalAmount += incentive.getAmount();
            }
            recipient.setBalance(recipient.getBalance() + totalAmount);
            userRepository.save(recipient);
        }
    }
}
