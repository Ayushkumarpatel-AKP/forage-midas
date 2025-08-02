import requests
import math

# Initialize users with their starting balances
users = {
    1: {"name": "wilbur", "balance": 1000},
    2: {"name": "wooster", "balance": 1000},
    3: {"name": "wiggly", "balance": 1000},
    4: {"name": "waldorf", "balance": 1000},
    5: {"name": "wizard", "balance": 1000},
    6: {"name": "wozniak", "balance": 1000},
    7: {"name": "wanda", "balance": 1000},
    8: {"name": "washington", "balance": 1000},
    9: {"name": "waterloo", "balance": 1000},
    10: {"name": "wiggy", "balance": 1000}
}

# Read transactions from file
with open("mnbvcxz.vbnm", "r") as f:
    transactions = f.readlines()

# Process each transaction
for transaction_line in transactions:
    parts = transaction_line.strip().split(" ")
    if len(parts) == 3:
        sender_id = int(parts[0])
        recipient_id = int(parts[1])
        amount = float(parts[2])
        
        # Create transaction object
        transaction = {
            "senderId": sender_id,
            "recipientId": recipient_id,
            "amount": amount
        }
        
        # Call incentives API
        try:
            response = requests.post("http://localhost:8080/incentive", json=transaction)
            if response.status_code == 200:
                incentive_data = response.json()
                incentive_amount = incentive_data.get("amount", 0)
                print(f"Transaction: {sender_id} -> {recipient_id}, Amount: {amount}, Incentive: {incentive_amount}")
                
                # Update recipient balance with transaction amount + incentive
                # No deduction from sender as per Task Four requirements
                users[recipient_id]["balance"] += amount + incentive_amount
                print(f"Updated balance for {users[recipient_id]['name']}: {users[recipient_id]['balance']}")
            else:
                print(f"Error calling incentives API: {response.status_code}")
                # Still update recipient balance with just the transaction amount
                users[recipient_id]["balance"] += amount
                print(f"Updated balance for {users[recipient_id]['name']}: {users[recipient_id]['balance']}")
        except Exception as e:
            print(f"Error calling incentives API: {e}")
            # Still update recipient balance with just the transaction amount
            users[recipient_id]["balance"] += amount
            print(f"Updated balance for {users[recipient_id]['name']}: {users[recipient_id]['balance']}")

# Calculate final balance for wilbur (user ID 1)
wilbur_balance = users[1]["balance"]
final_balance = math.floor(wilbur_balance)

print(f"----- begin ----- {final_balance} ----- end -----")

# Write to task_four_output.txt
with open("task_four_output.txt", "w") as f:
    f.write(f"----- begin ----- {final_balance} ----- end -----")
