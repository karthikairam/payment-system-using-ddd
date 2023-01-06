# skiply-system
A student fee payment system.

# Design Decisions
- Used `@RequiredArgsConstructor` for constructor injection
- Two type of service classes used here as part of **DDD** approach 
  - `**ApplicationService` class - to execute application level business logics
  - `**DomainService` class - to execute Domain business logics. 
    - **Optional** if only one **AggregateRoot** is involved. Ex: `student-service`. 
    - It is only applicable when two or more **AggregateRoots** has to be accessed and orchestrated to execute Domain logic (**DDD** concept).
  - Used **ValueObjects** to embrace domain driven concepts and set a meaningful types (**DDD** concept).
    - ex: `StudentId`, etc

# Assumptions
- Student number will be unique across the schools. So, make it as primary key after the required validations.
  - Otherwise, we need to maintain internal student id with in our system to make it unique.
- Successful student registration is denoted as below,
  - Response HTTP status code: `201`