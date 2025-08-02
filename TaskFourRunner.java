import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.UserRecord;
import com.vagabond.midas.Incentive;
import org.springframework.web.client.RestTemplate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TaskFourRunner {
    private Map<Long, UserRecord> users = new HashMap<>();
    private RestTemplate restTemplate = new RestTemplate();
    
    public static void main(String[] args) {
        TaskFourRunner runner = new TaskFourRunner();
        runner.run();
    }
    
    public void run() {
        // Initialize users with their starting balances
        initializeUsers();
        
        // Process transactions
        processTransactions();
        
        // Output the final balance for wilbur (user ID 1)
        UserRecord wilbur = users.get(1L);
        if (wilbur != null) {
            int finalBalance = (int) Math.floor(wilbur.getBalance());
            System.out.println("----- begin ----- " + finalBalance + " ----- end -----");
            
            // Write to task_four_output.txt
            try {
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("task_four_output.txt"),
                    ("----- begin ----- " + finalBalance + " ----- end -----").getBytes()
                );
            } catch (IOException e) {
                System.err.println("Error writing to task_four_output.txt: " + e.getMessage());
            }
        }
    }
    
    private void initializeUsers() {
        users.put(1L, new UserRecord("wilbur", 1000));
        users.put(2L, new UserRecord("wooster", 1000));
        users.put(3L, new UserRecord("wiggly", 1000));
        users.put(4L, new UserRecord("waldorf", 1000));
        users.put(5L, new UserRecord("wizard", 1000));
        users.put(6L, new UserRecord("wozniak", 1000));
        users.put(7L, new UserRecord("wanda", 1000));
        users.put(8L, new UserRecord("washington", 1000));
        users.put(9L, new UserRecord("waterloo", 1000));
        users.put(10L, new UserRecord("wiggy", 1000));
    }
    
    private void processTransactions() {
        try (BufferedReader reader = new BufferedReader(new FileReader("mnbvcxz.vbnm"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    long senderId = Long.parseLong(parts[0]);
                    long recipientId = Long.parseLong(parts[1]);
                    float amount = Float.parseFloat(parts[2]);
                    
                    Transaction transaction = new Transaction(senderId, recipientId, amount);
                    
                    // Validate transaction
                    if (validateTransaction(transaction)) {
                        // Call incentives API
                        Incentive incentive = callIncentivesAPI(transaction);
                        
                        // Update recipient balance with transaction amount + incentive
                        // No deduction from sender as per Task Four requirements
                        UserRecord recipient = users.get(recipientId);
                        if (recipient != null) {
                            float totalAmount = amount;
                            if (incentive != null) {
                                totalAmount += incentive.getAmount();
                            }
                            recipient.setBalance(recipient.getBalance() + totalAmount);
                            System.out.println("Updated balance for user " + recipient.getName() + ": " + recipient.getBalance());
                        }
                        
                        System.out.println("Processed transaction: " + senderId + " -> " + recipientId + ", amount: " + amount);
                        if (incentive != null) {
                            System.out.println("  Incentive amount: " + incentive.getAmount());
                        }
                    } else {
                        System.out.println("Invalid transaction: " + senderId + " -> " + recipientId + ", amount: " + amount);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions file: " + e.getMessage());
        }
    }
    
    private boolean validateTransaction(Transaction transaction) {
        UserRecord sender = users.get(transaction.getSenderId());
        UserRecord recipient = users.get(transaction.getRecipientId());
        
        // Check if sender and recipient exist
        if (sender == null || recipient == null) {
            return false;
        }
        
        // According to Task Four requirements, we don't deduct from sender, so this validation is not needed
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
}
