# Mini Bank System

## System that solves Danske Bank challenge 
* Created a mini bank system that allows you to create, update and search the account owners.

## Commands to run project

* If you have maven, for starting application, you can run: `mvn spring-boot:run` or run AccountsApplication.java main class
* When application is running you can acces H2 console via http://localhost:8080/api/h2-console
* To populate and test endpoints import Postman Collection to Postman from:
src/main/resources/postman-collections/AccountCustomers.postman_collection.json
* There are unit tests for service methods - to run tests, you can run `mvn test` or go to the test/java folder and run using Intellij

#### This application uses: Java 17, Spring Boot, Maven, H2, Lombok, Spring Data Jpa, Spring Web