# YumTea #
This is the backend API for a tea e-commerce website.
It is a Springboot application with Maven developed in Intellij IDEA.
### Technology 
- Java 17
- JDK 23
- Springboot 3

### Dependencies used in the project includes: 
- Spring BootDevTools
- Spring Web
- Spring Security 6
- JDBC API
- Spring Data JPA
- MySQL
- Lombak (Optional)
- junit (Optional)
- jakarta.validation (Optional)
- slf4j (Optional)

The Frontend is created in **React.js** and can be found here:  https://github.com/villysiu/yumtea_sb_frontend 
Youtube: 

## Running the project ##
- Clone the Git repository
- Open Intellij, 
  - File -> New -> Project from Version Control
  - Paste the cloned git link into the `URL` box
  - Click `Clone`
- Choose the Spring Boot Application file `/src/main/java/com.villysiu.yumtea/YumteaApplication/`
- Click on the triangle next to `public static void main(String[] args)` 
- The server should start.
- You are ready to test out the api in any api clients.


## Endpoints ##
**Authentication Controller**

This controller provides APIs for register, login and logout actions.
- **POST** : `/auth/signup`
  - `SignupRequest` DTO is used to map the following Json object.
    ``` 
    {
        "email": "john@doe.com",
        "password": "password",
        "nickname": "John Doe"
    } 
    ```
  - check if email already in database
  - create new User (default with ROLE_USER)
  - save User to database using AccountRepository
  - return http status `201 CREATED`
   

- **POST** : `auth/login`
  - `SigninRequest` DTO is used to map the following Json object.
     ``` 
     {
        "email": "john@doe.com",
        "password": "password1"
    }
     ```
  - authenticate with Spring Security
  - update SecurityContext using Authentication object
  - persist Authentication object in HttpSession
  - return a `SigninResponse` DTO with http status 200 OK

- **POST** : `auth/logout`
  
  - logout with Spring Security
  - invalidate and remove HttpSession
  - clear securityContextHolder
  - return http status `200 OK`


### Entity ###
- user
  - Account
  - Role
- tea
    - Category
    - Menuitem
    - Milk
    - Size
    - Sugar (Enum)
    - Temperature (Enum)
  -cart
    - Cart
  - purchase
    - Purchase
    - purchaseLineitem
    - taxRare






