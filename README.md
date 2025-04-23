# API Beneficial Owners

This project is a REST API built with Java 21 and Spring Boot. It allows managing the beneficial owners of a company, based on the following scenarios:

- Direct ownership of more than 25% of capital
- Indirect ownership through other companies
- Combined direct and indirect ownership

## ⚙️ Run the Application

### Requirements

- Java 21
- Maven 3.9+


### Steps

```bash
# Clone the repository
git clone https://github.com/your-account/api-beneficiaires.git
cd api-beneficiaires

# Start the application
./mvnw spring-boot:run

The API will be available at: http://localhost:8080

# Run tests 

./mvnw test

This will run all unit and integration tests.
```

## Project Structure

The project is organized using Clean Architecture and Domain-Driven Design (DDD):

- domain.model: domain entities like Company, Beneficiary, Percentage

- domain.service: business logic (e.g., BeneficiaryFilter)

- application.usecase: use cases (GetBeneficiariesUseCase)

- infrastructure.repository: in-memory implementation of repositories

- interfacecontroller: REST controllers

- config: application configuration and test data initialization

 ## Suggested Improvements

- Add authentication & authorization (Spring Security)

- Add Swagger / OpenAPI documentation

- Replace in-memory database with a persistent store (e.g., PostgreSQL)

- Dockerize the application

- Move business logic (`BeneficiaryFilter`) directly into the domain to follow rich domain principles (DDD)

- Add code coverage tracking 
 



