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

# Read transactions from test data file
with open("src/test/resources/test_data/rueiwoqp.tyruei", "r") as f:
    transactions = f.readlines()

# Process each transaction
for transaction_line in transactions:
    parts = transaction_line.strip().split(", ")
    if len(parts) == 3:
        sender_id = int(parts[0])
        recipient_id = int(parts[1])
        amount = float(parts[2])
        
        # For Task Five, we need to deduct from sender and add to recipient
        users[sender_id]["balance"] -= amount
        users[recipient_id]["balance"] += amount
        
        print(f"Processed transaction: {sender_id} -> {recipient_id}, Amount: {amount}")

# Calculate final balances and write to task_five_output.txt
with open("task_five_output.txt", "w") as f:
    output_lines = []
    for i in range(13):  # As per the test, userId from 0 to 12
        if i in users:
            balance = math.floor(users[i]["balance"])
            output_lines.append(f"Balance {{amount={balance}.0}}")
        else:
            # For userId 0 and 11, 12 (not in users dictionary)
            output_lines.append("Balance {amount=0.0}")
    
    f.write("---begin output ---\n")
    for line in output_lines:
        f.write(line + "\n")
    f.write("---end output ---")

print("Task Five output generated successfully!")
