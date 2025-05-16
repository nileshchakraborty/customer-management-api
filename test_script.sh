#!/bin/bash
# test_script.sh - Run integration tests for Customer Management API via curl

set -e

# Start Spring Boot app in background
echo "Starting Spring Boot application..."
./mvnw spring-boot:run &
APP_PID=$!

# Wait for app to be ready
until curl -s http://localhost:8080/actuator/health | grep UP; do
  echo "Waiting for Spring Boot to be ready..."
  sleep 2
done

echo "Spring Boot application is running. Running endpoint tests..."

# Create customer (should be Gold tier)
CREATE_RESPONSE=$(curl -s -X POST http://localhost:8080/customer \
  -H 'Content-Type: application/json' \
  -d '{"name":"John Doe","email":"john@example.com","annualSpend":1200,"lastPurchaseDate":"2025-05-01T00:00:00"}')
echo "Create Response: $CREATE_RESPONSE"
ID=$(echo "$CREATE_RESPONSE" | grep -o '"id":"[^"]*"' | cut -d '"' -f4)

echo "\nBusiness Logic Test: Gold Tier (annualSpend=1200, lastPurchaseDate=2025-05-01)"
echo "$CREATE_RESPONSE" | grep '"tier":"Gold"' && echo "PASS" || echo "FAIL"

# Update to Platinum tier
UPDATE_RESPONSE=$(curl -s -X PUT http://localhost:8080/customer/$ID \
  -H 'Content-Type: application/json' \
  -d '{"name":"Jane Doe","email":"jane@example.com","annualSpend":15000,"lastPurchaseDate":"2025-04-01T00:00:00"}')
echo "Update Response: $UPDATE_RESPONSE"
echo "\nBusiness Logic Test: Platinum Tier (annualSpend=15000, lastPurchaseDate=2025-04-01)"
echo "$UPDATE_RESPONSE" | grep '"tier":"Platinum"' && echo "PASS" || echo "FAIL"

# Update to Silver tier
UPDATE_RESPONSE2=$(curl -s -X PUT http://localhost:8080/customer/$ID \
  -H 'Content-Type: application/json' \
  -d '{"name":"Jane Doe","email":"jane@example.com","annualSpend":500,"lastPurchaseDate":"2025-04-01T00:00:00"}')
echo "Update Response 2: $UPDATE_RESPONSE2"
echo "\nBusiness Logic Test: Silver Tier (annualSpend=500, lastPurchaseDate=2025-04-01)"
echo "$UPDATE_RESPONSE2" | grep '"tier":"Silver"' && echo "PASS" || echo "FAIL"

# Get by ID
curl -s http://localhost:8080/customer/$ID | tee >(echo "Get by ID Response: $(cat -)")


# Get by name
curl -s "http://localhost:8080/customer?name=Jane%20Doe" | tee >(echo "Get by Name Response: $(cat -)")

# Get by email
curl -s "http://localhost:8080/customer?email=jane@example.com" | tee >(echo "Get by Email Response: $(cat -)")

# Delete customer
curl -s -X DELETE http://localhost:8080/customer/$ID | tee >(echo "Delete Response: $(cat -)")


# Stop Spring Boot app
echo "Stopping Spring Boot application..."
kill $APP_PID
