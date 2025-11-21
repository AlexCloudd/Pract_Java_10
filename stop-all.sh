#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Stopping Cinema Hub Microservices...${NC}"

# Function to kill process by PID
kill_service() {
    local pid=$1
    local service_name=$2
    
    if [ ! -z "$pid" ] && kill -0 $pid 2>/dev/null; then
        echo -e "${YELLOW}Stopping $service_name (PID: $pid)...${NC}"
        kill $pid
        sleep 2
        
        # Force kill if still running
        if kill -0 $pid 2>/dev/null; then
            echo -e "${RED}Force killing $service_name...${NC}"
            kill -9 $pid
        fi
        echo -e "${GREEN}✅ $service_name stopped${NC}"
    else
        echo -e "${RED}❌ $service_name not running${NC}"
    fi
}

# Read PIDs from file if it exists
if [ -f "/tmp/cinema-hub-pids.txt" ]; then
    PIDS=$(cat /tmp/cinema-hub-pids.txt)
    SERVICES=("Eureka Server" "Config Server" "API Gateway" "Cinema Hub" "User Service" "Movie Service" "Rating Service")
    
    i=0
    for pid in $PIDS; do
        kill_service $pid "${SERVICES[$i]}"
        i=$((i + 1))
    done
    
    # Remove PID file
    rm -f /tmp/cinema-hub-pids.txt
else
    echo -e "${YELLOW}No PID file found, trying to kill by process name...${NC}"
    
    # Kill by process name
    pkill -f "eureka-server" && echo -e "${GREEN}✅ Eureka Server stopped${NC}"
    pkill -f "config-server" && echo -e "${GREEN}✅ Config Server stopped${NC}"
    pkill -f "api-gateway" && echo -e "${GREEN}✅ API Gateway stopped${NC}"
    pkill -f "cinema-hub" && echo -e "${GREEN}✅ Cinema Hub stopped${NC}"
    pkill -f "user-service" && echo -e "${GREEN}✅ User Service stopped${NC}"
    pkill -f "movie-service" && echo -e "${GREEN}✅ Movie Service stopped${NC}"
    pkill -f "rating-service" && echo -e "${GREEN}✅ Rating Service stopped${NC}"
fi

# Kill any remaining Java processes related to our services
echo -e "${YELLOW}Cleaning up any remaining processes...${NC}"
pkill -f "spring-boot:run" 2>/dev/null
pkill -f "pr3-0.0.1-SNAPSHOT.jar" 2>/dev/null

# Clean up log files
echo -e "${YELLOW}Cleaning up log files...${NC}"
rm -f /tmp/eureka-server.log
rm -f /tmp/config-server.log
rm -f /tmp/api-gateway.log
rm -f /tmp/cinema-hub.log
rm -f /tmp/user-service.log
rm -f /tmp/movie-service.log
rm -f /tmp/rating-service.log

echo
echo -e "${GREEN}========================================"
echo -e "   🛑 ALL SERVICES STOPPED SUCCESSFULLY!"
echo -e "========================================${NC}"
echo -e "${BLUE}All microservices have been stopped and cleaned up.${NC}"
