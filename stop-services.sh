#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================"
echo -e "   Stopping Cinema Hub Services"
echo -e "========================================${NC}"
echo

# Stop services by PID if file exists
if [ -f /tmp/cinema-hub-pids.txt ]; then
    echo -e "${YELLOW}Stopping services by PID...${NC}"
    PIDS=$(cat /tmp/cinema-hub-pids.txt)
    for pid in $PIDS; do
        if kill -0 $pid 2>/dev/null; then
            echo -e "${YELLOW}Stopping process $pid...${NC}"
            kill $pid
        fi
    done
    rm -f /tmp/cinema-hub-pids.txt
fi

# Stop any remaining Java processes
echo -e "${YELLOW}Stopping any remaining Java services...${NC}"
pkill -f "java.*jar" 2>/dev/null || true

# Wait a moment for graceful shutdown
sleep 3

# Force kill if still running
echo -e "${YELLOW}Force stopping any remaining processes...${NC}"
pkill -9 -f "java.*jar" 2>/dev/null || true

echo -e "${GREEN}✅ All services stopped successfully!${NC}"
echo
echo -e "${BLUE}💡 To start services again, run:${NC}"
echo -e "   ./start-services.sh"

