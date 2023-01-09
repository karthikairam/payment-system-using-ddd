# Student microservice
This service is responsible for registering student with skiply system and maintains it.

## To run the application
 Please refer the parent [README.md](../README.md) file on how to run the services.

## API Documentation for student service
Note: Make sure application is running.
 - Refer: [api-docs](http://localhost:8081/v3/api-docs) for Open API Specification 3.0
 - To access Swagger UI [click here](http://localhost:8081/swagger-ui/index.html)
   - It is a fully working client to rest the rest endpoint. So postman is actually not needed.
   - Refer the below sample request & response to test

## Sample request & response
- Request
  - Endpoint
    ```
    POST  http://localhost:8081/v1/students
    ```
  - Header
    ```
    accept: application/json
    Content-Type: application/json
    ```
  - Payload
    ```json
    {
        "studentId": "98989899",
        "studentName": "FirstName MiddleName LastName",
        "grade": "Grade 1",
        "mobileNumber": "+971555555555",
        "schoolName": "School Name"
    }
    ```
- Response
  - HTTP code `201`
  - Payload
    ```json
    {
       "studentId": "98989899"
    }
    ```

## H2 DB Client Web Console
Note: Make sure application is running.
  - Step 1: Open the console DB Web Console : [client here](http://localhost:8081/h2-console)
  - Step 2: Use below values for login
    - Driver class: `org.h2.Driver` 
    - Connection string: `jdbc:h2:file:./h2-db/skiply-student-db;MODE=PostgreSQL`
    - User Name: `sa`
    - Password `password`  - if you have overridden it then use it accordingly.
