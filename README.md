## TransportTicketApp

*RESTful API для предоставления возможностей пользователям покупать транспортные билеты*

## Бэкенд-часть проекта предполагает реализацию следующего функционала: ##

### От лица пользователя:

1. Регистрация:

Пользователи могут зарегистрироваться, указав имя, фамилию, email и пароль:

`RegisterRequest`
```json
{
  "first_name": "Stan",
  "last_name": "Smith",
  "email": "stan@gmail.com",
  "password": "qwerty123456789"
}
```
В ответ получают информацию о своем аккаунте:

`UserDto`
```json
{
  "id": 1,
  "username": "stan@gmail.com",
  "email": "stan@gmail.com",
  "role": "USER",
  "first_name": "Stan",
  "last_name": "Smith",
  "enabled": true,
  "created_at": "2023-10-02T15:04:23.883Z",
  "updated_at": "2023-10-02T15:04:23.883Z"
}
```

2. Аутентификация и авторизация:

Пользователи могут войти в свой аккаунт, указав свой логин и пароль:

`LoginRequest`
```json
{
  "username": "stan@gmail.com",
  "password": "qwerty123456789"
}
```
В ответ получают JWT токены для авторизации в системе:

`LoginResponse`
```json
{
  "user_id": 1,
  "access_token": "eyJhbGciOiJIUzM4NCJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VybmFtZSI6ImFkbWluIiwiaXNzIjoiYWxla3NleVNlcnZlciIsInN1YiI6IjgiLCJleHAiOjE2OTYyNDQ1ODIsImp0aSI6IjQ5OGJlM2UyLTI0YWMtNDZmYy05YjU0LTFkYWZjNWRhYTVjYSJ9.2u_SQbANmntLLUyVOBlnezvhLHX-ClG0RQ45zNnuBeWFQIbpdgDIN-xWCPdk0Znw",
  "refresh_token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI4IiwiZXhwIjoxNjk4ODM0NzgyLCJqdGkiOiJhNTUwYTYzMi1iYjkyLTQ5MjYtYmMzMS1lY2ZiZmIwMDEwZmEifQ.Q5NEHIAFqkbs8jt1pkLfoB9pRxKOHDaSyWhWXcA2lDjlB-VoeNk6vtqWdBO8dCMt"
}
```

3. Просмотр доступных билетов:

Пользователи могут смотреть список доступных и актуальных билетов:

`ResponseWrapperTickets`
```json
{
  "count": 1,
  "results": [
    {
      "id": 9,
      "route": {
        "id": 9,
        "departure": "Riga",
        "destination": "Tallin",
        "carrier": {
          "id": 3,
          "name": "Ecolines",
          "phone": "+371 67214512"
        },
        "durations_min": 260
      },
      "departure_date_time": "2023-10-30T17:30:00",
      "place_number": 7,
      "price": 25,
      "reserved": "FREE"
    }
  ]
}
```


---

## Спецификация OpenAPI для описания API ##

[OpenAPI specification](openapi.yaml "OpenAPI")

## Использован следующий стек технологий: ##

### Producer:

Java17\
SpringBoot 3\
Security\
JWT tokens\
JDBC\
PostgreSQL\
Lombok\
Gradle\
Flyway\
Mapstruct\
OpenAPI\
JUnit\
Mockito\
Docker\
Redis\
Apache Kafka

### Структура базы данных:

![](screens/Схема БД.png)

### Consumer:

Java17\
SpringBoot 3\
Hibernate\
PostgreSQL\
Lombok\
Gradle\
Flyway\
Mapstruct\
OpenAPI\
JUnit\
Mockito\
Docker\
Apache Kafka

### Структура базы данных:

![](screens/Схема БД.png)

##

[![Typing SVG](https://readme-typing-svg.herokuapp.com?color=%2336BCF7&lines=thank+you+for+your+attention)](https://git.io/typing-svg)