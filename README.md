To run a programm
1. Install Docker
2. clone repository
3. from project root directory run command:  ./mvnw package -DskipTests=true
4. in root project folder create file '.env' with the following content
     WEATHER_API_KEY=your_api_key
     POSTGRES_PASSWORD=anything
5. from project root directory run command: docker-compose up
6. app should be available on localhost:8080 (check if port is free before running)
