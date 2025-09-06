# Проект “Погода”
## Веб-приложение для просмотра текущей погоды. Пользователь может зарегистрироваться и добавить в коллекцию одну или несколько локаций (городов, сёл, других пунктов), после чего главная страница приложения начинает отображать список локаций с их текущей погодой.

# main pages
## searching locations
<img width="1907" height="587" alt="image" src="https://github.com/user-attachments/assets/3fc2a620-def8-44fa-8ac1-8f555fc81f05" />

## check weather of saved locations
<img width="1912" height="720" alt="image" src="https://github.com/user-attachments/assets/8c3daf01-eb27-43ca-8ad9-d8b37d998ce2" />

## To run a programm
1. Install Docker
2. clone repository
3. from project root directory run command:  ./mvnw package -DskipTests=true
4. in root project folder create file '.env' with the following content
     WEATHER_API_KEY=your_api_key
     POSTGRES_PASSWORD=anything
5. from project root directory run command: docker-compose up
6. app should be available on localhost:8080 (check if port is free before running)
