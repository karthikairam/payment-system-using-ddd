# Receipt microservice
This service is responsible fetch the receipts of the payment collected in skiply system.

## To run the application
 Please refer the parent [README.md](../README.md#local-run) file on how to run the services.

## API Documentation
Note: Make sure application is running.
 - Refer: [api-docs](http://localhost:8083/v3/api-docs) for Open API Specification 3.0
 - To access Swagger UI [click here](http://localhost:8083/swagger-ui/index.html)
   - It is a fully working client to rest the rest endpoint. So postman is actually not needed.
   - Refer the below sample request & response to test

## Sample request & response
- Request
  - Endpoint
    ```
    GET http://localhost:8083/v1/receipts?paymentReferenceNumber=1673232016382360789
    ```
- Response
  - HTTP code
    ```
    200 Ok (or) 202 Accepted when the status is PENDING
    ```
  - Payload
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

## H2 DB Client Web Console
Note: Make sure application is running.
  - Step 1: Open the console DB Web Console : [client here](http://localhost:8083/h2-console)
  - Step 2: Use below values for login
    - Driver class: `org.h2.Driver` 
    - Connection string: `jdbc:h2:file:./h2-db/skiply-receipt-db;MODE=PostgreSQL`
    - User Name: `sa`
    - Password `password`  - if you have overridden it then use it accordingly.
