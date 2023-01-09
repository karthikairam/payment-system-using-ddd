# Skiply system

A student management and fee collection system. Consists of 3 micro-services for student, payment & receipt domains and a supporting kafka server.

## Pre-requisite

Please make sure your machine has the following things installed,
- IDE: IntelliJ IDEA (preferred)
  - `Java 17` (can be installed easily using IDE if you have one)
  - `Maven 3.8.*` (again comes bundled with most of the IDE)
  - Checkout this project as whole
    - It is a multi-module mono-repo consists of all the required modules together.
  - Select maven's `clean install` goals from maven section of the IDE
  
  (or)

- If you prefer terminal, you can use `mvnw` (maven wrapper) comes with this repository itself
  - `mvnc clean install` from the root of the directory to build the module.

## Module anatomy
  - All the common modules are group under `common` maven module.
  - Service modules are suffixed with `**-service` & `**-server`
    - can be run individually
  - `kafka-server` module is a `Embedded Kafka` setup using spring-boot. And it is good enough for this demo app.
    - All the required kafka `topics` are created automatically when it is up.
    - It is visible only on `localhost` and not configured for remote execution.
    - Message serialization: `Json` serializer is used for messages. Due to demo app, we haven't gone with `Avro` format
    & `schema-registry`.
 
## Local Run 
Steps to locally run the required applications,
  - Please run the below services (spring-boot),
    1. `kafka-server` - recommended to start this first.
    2. `studen-service`
    3. `payment-service`
    4. `receipt-service`
    

  - (Optional) Now, you may also compound these runs into single unit as shown below,
    ![Compound Run](https://github.com/karthikairam/skiply-system/blob/main/docs/img/img.png?raw=true)

  - Once all the services are up & running, then go to each service specific Swagger UI to execute the flows as 
described below.

## Steps to verify the flow

- **Step 1**: Register a student using student-service's post endpoint (Assumption: Student Id has to be provided to register).
   - Request
     
     URL `POST  http://localhost:8081/v1/students`

     Header
     ```
        accept: application/json
        Content-Type: application/json
     ```
  
     ```json
       {
           "studentId": "98989899",
           "studentName": "FirstName MiddleName LastName",
           "grade": "Grade 1",
           "mobileNumber": "+971555555555",
           "schoolName": "School Name"
       }
       ```
   
       - Response: `201` success response.
        ```json
          {
             "studentId": "98989899"
          }
        ```
- **Step 2**: Make a payment for the registered student-id. Again you should get `201` and a `paymentReferenceNumber` as a response.
  Note: `IdempotencyKey` has to be unique for each transaction to maintain the idempotency of the payment request.
  - Request:
  
     URL: `POST  http://localhost:8082/v1/payments`
  
     Headers:
     ```
    accept: application/json
    Content-Type: application/json
    ```

     ```json
     {
        "studentId": "98989899",
        "paidBy": "Karthik",
        "idempotencyKey": "20220102123520210",
        "cardDetail": {
                "cardNumber": "54021928179322",
                "cardType": "MC",
                "cardCvv": "9465",
                "cardExpiry": "01/31"
        },
        "totalPrice": 151.5,
        "purchaseItems": [
                {
                        "feeType": "Tuition",
                        "name": "Grade 1",
                        "quantity": 3,
                        "price": 50.5
                }
        ]
     }
     ```
  - Response: `201` Success response
      ```json
      {
          "paymentReferenceNumber": "1673232016382360789",
          "status": "COMPLETED"
      }
      ```

- **Step 3**: Retrieve the receipt using `paymentReferenceNumber` 
  - Request
 
     URL `GET http://localhost:8083/v1/receipts?paymentReferenceNumber=1673232016382360789`
 
  - Response `200` Success  (or) `202` Accepted when the status is in `pending`.
   
       ```json
       {
             "paidBy": "Karthik",
             "studentInfo": {
                     "studentId": "98989899",
                     "name": "FirstName MiddleName LastName",
                     "grade": "Grade 1"
             },
             "transactionDetail": {
                     "paymentReferenceNumber": "1673232016382360789",
                     "datetime": "2023-01-09T06:40:16.369882+04:00",
                     "cardNumber": "54021928179322",
                     "cardType": "MC"
             },
             "purchaseDetail": {
                     "totalPrice": 151.5,
                     "purchaseItems": [
                             {
                                     "type": "Tuition",
                                     "name": "Grade 1",
                                     "quantity": 3,
                                     "price": 50.5
                             }
                     ]
             },
             "receiptStatus": "COMPLETED"
       }
       ```

## Design Decisions
- To aggregate data from multiple service, the approach I took is to apply CQRS pattern. 
  - For instance, `receipt-service` needs data from `student-service` & `payment-service`. The flow is,
    - `payment-service` emits an event to `kafka` with required information once payment is collected successfully.
    - `receipt-service` then consumes it and load the data in a format it needs (since the payment data is immutable 
    once created). It is a perfect fine to capture and store locally in a structure it needs.
    - Once payment related information is persisted in the `receipt-service`'s local DB, then it will request for 
    the student info. It is achieved by request-response event model using `kafka`.
    - Both payment & student information required for receipt will be stored locally in an expected format.
  
- Used `@RequiredArgsConstructor` for constructor injection
- Two type of service classes might be used here as part of **DDD** approach 
  - `**ApplicationService` class - to execute application level business logics
  - `**DomainService` class - to execute Domain business logics.
    - **Optional** if only one **AggregateRoot** is involved. Ex: `student-service`. 
    - It is only applicable when two or more **AggregateRoots** has to be accessed and orchestrated to execute 
    Domain logic (**DDD** concept).
  - Used **ValueObjects** to embrace domain driven concepts and set a meaningful types (**DDD** concept).
    - ex: `StudentId`, etc
  - I used `Lombok` annotations in the Domain classes due to time constrain. Usually it will be made up of 
  pure Java code without any dependency from an external libs or frameworks.

## Assumptions
- Student number will be unique across the schools. So, make it as primary key after the required validations.
  - Otherwise, we need to maintain internal student id with in our system to make it unique.
- Payment service doesn't validate the student Id is a registered one before making payment. It was clarified 
before doing this assignment.