#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================"
echo -e "   Cinema Hub - Microservices Stack"
echo -e "========================================${NC}"
echo

# Function to check if port is available
check_port() {
    local port=$1
    if lsof -Pi :$port -sTCP:LISTEN -t >/dev/null ; then
        echo -e "${RED}❌ Port $port is already in use!${NC}"
        return 1
    fi
    return 0
}

# Function to wait for service to start
wait_for_service() {
    local url=$1
    local service_name=$2
    local max_attempts=30
    local attempt=1
    
    echo -e "${YELLOW}⏳ Waiting for $service_name to start...${NC}"
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo -e "${GREEN}✅ $service_name is ready!${NC}"
            return 0
        fi
        sleep 2
        attempt=$((attempt + 1))
    done
    
    echo -e "${RED}❌ $service_name failed to start after $max_attempts attempts${NC}"
    return 1
}

# Stop any existing services
echo -e "${YELLOW}Stopping existing services...${NC}"
pkill -f "java.*jar" 2>/dev/null || true
sleep 3

echo -e "${YELLOW}[1/6] Starting Eureka Server (Port 8761)...${NC}"
if ! check_port 8761; then
    echo -e "${RED}Please stop the service using port 8761 and try again${NC}"
    exit 1
fi

cd eureka-server
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar > /tmp/eureka-server.log 2>&1 &
EUREKA_PID=$!
echo "Eureka Server PID: $EUREKA_PID"
cd ..

if ! wait_for_service "http://localhost:8761" "Eureka Server"; then
    echo -e "${RED}Failed to start Eureka Server${NC}"
    kill $EUREKA_PID 2>/dev/null
    exit 1
fi

echo
echo -e "${YELLOW}[2/6] Starting User Service (Port 8082)...${NC}"
if ! check_port 8082; then
    echo -e "${RED}Please stop the service using port 8082 and try again${NC}"
    kill $EUREKA_PID 2>/dev/null
    exit 1
fi

cd user-service
java -jar target/user-service-0.0.1-SNAPSHOT.jar > /tmp/user-service.log 2>&1 &
USER_PID=$!
echo "User Service PID: $USER_PID"
cd ..

if ! wait_for_service "http://localhost:8082/actuator/health" "User Service"; then
    echo -e "${RED}Failed to start User Service${NC}"
    kill $EUREKA_PID $USER_PID 2>/dev/null
    exit 1
fi

echo
echo -e "${YELLOW}[3/6] Starting Movie Service (Port 8083)...${NC}"
if ! check_port 8083; then
    echo -e "${RED}Please stop the service using port 8083 and try again${NC}"
    kill $EUREKA_PID $USER_PID 2>/dev/null
    exit 1
fi

cd movie-service
java -jar target/movie-service-0.0.1-SNAPSHOT.jar > /tmp/movie-service.log 2>&1 &
MOVIE_PID=$!
echo "Movie Service PID: $MOVIE_PID"
cd ..

if ! wait_for_service "http://localhost:8083/actuator/health" "Movie Service"; then
    echo -e "${RED}Failed to start Movie Service${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID 2>/dev/null
    exit 1
fi

echo
echo -e "${YELLOW}[4/6] Starting Rating Service (Port 8084)...${NC}"
if ! check_port 8084; then
    echo -e "${RED}Please stop the service using port 8084 and try again${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID 2>/dev/null
    exit 1
fi

cd rating-service
java -jar target/rating-service-0.0.1-SNAPSHOT.jar > /tmp/rating-service.log 2>&1 &
RATING_PID=$!
echo "Rating Service PID: $RATING_PID"
cd ..

if ! wait_for_service "http://localhost:8084" "Rating Service"; then
    echo -e "${RED}Failed to start Rating Service${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID $RATING_PID 2>/dev/null
    exit 1
fi

echo
echo -e "${YELLOW}[5/6] Starting Cinema Hub (Port 8081)...${NC}"
if ! check_port 8081; then
    echo -e "${RED}Please stop the service using port 8081 and try again${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID $RATING_PID 2>/dev/null
    exit 1
fi

java -jar target/pr3-0.0.1-SNAPSHOT.jar --spring.profiles.active=microservice > /tmp/cinema-hub.log 2>&1 &
CINEMA_PID=$!
echo "Cinema Hub PID: $CINEMA_PID"

if ! wait_for_service "http://localhost:8081" "Cinema Hub"; then
    echo -e "${RED}Failed to start Cinema Hub${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID $RATING_PID $CINEMA_PID 2>/dev/null
    exit 1
fi

echo
echo -e "${YELLOW}[6/6] Starting API Gateway (Port 8080)...${NC}"
if ! check_port 8080; then
    echo -e "${RED}Please stop the service using port 8080 and try again${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID $RATING_PID $CINEMA_PID 2>/dev/null
    exit 1
fi

cd api-gateway
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar > /tmp/api-gateway.log 2>&1 &
GATEWAY_PID=$!
echo "API Gateway PID: $GATEWAY_PID"
cd ..

if ! wait_for_service "http://localhost:8080/actuator/health" "API Gateway"; then
    echo -e "${RED}Failed to start API Gateway${NC}"
    kill $EUREKA_PID $USER_PID $MOVIE_PID $RATING_PID $CINEMA_PID $GATEWAY_PID 2>/dev/null
    exit 1
fi

# Save PIDs to file for stop script
echo "$EUREKA_PID $USER_PID $MOVIE_PID $RATING_PID $CINEMA_PID $GATEWAY_PID" > /tmp/cinema-hub-pids.txt

echo
echo -e "${GREEN}========================================"
echo -e "   🎉 ALL SERVICES STARTED SUCCESSFULLY!"
echo -e "========================================${NC}"
echo
echo -e "${BLUE}📋 Available URLs:${NC}"
echo -e "   • Eureka Dashboard:    http://localhost:8761"
echo -e "   • Cinema Hub:         http://localhost:8081"
echo -e "   • User Service:        http://localhost:8082"
echo -e "   • Movie Service:       http://localhost:8083"
echo -e "   • Rating Service:      http://localhost:8084"
echo -e "   • API Gateway:         http://localhost:8080"
echo -e "   • Swagger UI:          http://localhost:8081/swagger-ui.html"
echo -e "   • Actuator Health:     http://localhost:8081/actuator/health"
echo
echo -e "${BLUE}💡 Tips:${NC}"
echo -e "   • Check Eureka Dashboard to see all registered services"
echo -e "   • Use './stop-services.sh' to stop all services"
echo -e "   • All services will auto-register with Eureka"
echo -e "   • Logs are available in /tmp/*.log files"
echo
echo -e "${YELLOW}Press Ctrl+C to stop all services or run './stop-services.sh'${NC}"

# Keep script running and handle Ctrl+C
trap 'echo -e "\n${YELLOW}Stopping all services...${NC}"; ./stop-services.sh; exit 0' INT

# Wait indefinitely
while true; do
    sleep 1
done

