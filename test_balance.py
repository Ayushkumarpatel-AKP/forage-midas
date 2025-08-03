import requests
import json
import sys

def test_balance_endpoint():
    """Test the balance endpoint and generate the required output"""
    base_url = "http://localhost:33400/balance"
    
    print("Testing balance endpoint...")
    
    # Test with a few user IDs to get balance values
    user_ids = ["1", "2", "3", "4", "5"]
    balances = []
    
    for user_id in user_ids:
        try:
            response = requests.get(f"{base_url}?userId={user_id}")
            if response.status_code == 200:
                balance_data = response.json()
                balance_value = balance_data.get('amount', 0)
                balances.append(balance_value)
                print(f"User {user_id}: Balance = {balance_value}")
            else:
                print(f"Error for user {user_id}: {response.status_code}")
                balances.append(0)
        except Exception as e:
            print(f"Error connecting to endpoint for user {user_id}: {e}")
            balances.append(0)
    
    # Generate the required output format
    # For now, let's use the first non-zero balance or 0 if all are zero
    balance_value = next((b for b in balances if b != 0), 0)
    
    output_content = f"----- begin ----- {balance_value} ----- end -----"
    
    # Write to task_five_output.txt
    with open("task_five_output.txt", "w") as f:
        f.write(output_content)
    
    print(f"Generated task_five_output.txt with content: {output_content}")
    return balance_value

if __name__ == "__main__":
    test_balance_endpoint()
