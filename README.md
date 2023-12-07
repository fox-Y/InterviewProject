# InterviewProject
Create a spring-boot microservice that will accept 5 input parameters (Name, SSN, Phone, Email & BirthDate). The service should validate input parameters using minimalist coding (use annotations as much as possible).
Then make 3 dummy calls to 3 backend services (SSN Validation, Phone Validation and Email Validation) using multi-threading or asynchronous calls
If all 3 are successful, store the entire object in a DB and create a unique CustomerID and send back to the calling client
