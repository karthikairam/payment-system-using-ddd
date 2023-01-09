# Payment microservice
This service is responsible to collect the payment in skiply system for the school fees.

## To run the application
 Please refer the parent [README.md](../README.md#local-run) file on how to run the services.

## API Documentation
Note: Make sure application is running.
 - Refer: [api-docs](http://localhost:8082/v3/api-docs) for Open API Specification 3.0
 - To access Swagger UI [click here](http://localhost:8082/swagger-ui/index.html)
   - It is a fully working client to rest the rest endpoint. So postman is actually not needed.
   - Refer the below sample request & response to test

## Sample request & response
- Request
  - Endpoint
    ```
    POST  http://localhost:8082/v1/payments
    ```

  - Headers
    ```
    accept: application/json
    Content-Type: application/json
    ```
    
  - Payload
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
    
- Response
  - HTTP code  `201`
    
  - Payload
    ```json
    {
        "paymentReferenceNumber": "1673232016382360789",
        "status": "COMPLETED"
    }
    ```

## H2 DB Client Web Console
Note: Make sure application is running.
  - Step 1: Open the console DB Web Console : [client here](http://localhost:8082/h2-console)
  - Step 2: Use below values for login
    - Driver class: `org.h2.Driver` 
    - Connection string: `jdbc:h2:file:./h2-db/skiply-payment-db;MODE=PostgreSQL`
    - User Name: `sa`
    - Password `password`  - if you have overridden it then use it accordingly.
