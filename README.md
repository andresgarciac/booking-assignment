# Booking assignment Andres Garcia

## Description
This is a Spring Boot Java 11 application with MySql to store all the Availability and Booking information contained with Docker and deployed with docker-compose.

In order to guarantee high availability, the implementation was made in a microservice and deployed in a container orchestrator (in a real environment is better to use kubernetes) for easily scale the services.

## Deployment process
In order to deploy the application is necessary to install docker and docker-compose and execute the next command to build the containers and deploy them.
```sh
docker build -t booking-service . && docker-compose up
```
Once the application is deployed go to the next URL to check the Swagger documentation with the endpoints ready to be used.
http://localhost:8080/swagger-ui/#/booking-controller

If you want to perform an operation directly in the MySql database, connect to the MySQL container using the next command.
```sh
docker exec -it {MYSQL_CONTAINER_ID} /bin/bash
```
Then you can connect to the database using the next command and providing the password = 1234.
```sh
mysql -usa -p
use bookingdb;
```
You can perform queries like
```sh
select * from availability_date;
select * from bookings;
```
## Features

- Get the room availability
- Place a booking
- Modify a booking by transactionId
- Cancel a booking by transactionId

## Database model

For the assignment purposes the database design is pretty simple using just two tables described next:
- ROOM_AVAILABILITY -> contains all the dates configured by the hotel where the room can be reserved.
- BOOKINGS -> contains all the bookings by userId and date.

![Alt text](src/main/resources/static/DATABASE_MODEL.png?raw=true "")
