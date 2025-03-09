# YumTea #
This is the backend API for a tea e-commerce website.
It is a Springboot application with Maven developed in Intellij IDEA.

The Frontend is created in **React.js** and can be found here: 
https://github.com/villysiu/yumtea_sb_frontend

Youtube: Coming soon
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


## Running the project ##
- Clone the Git repository https://github.com/villysiu/yum_tea_sb.git
- Open Intellij, 
  - File -> New -> Project from Version Control
  - Paste the cloned git link into the `URL` box
  - Click `Clone`
- Choose the Spring Boot Application file `/src/main/java/com.villysiu.yumtea/YumteaApplication/`
- Click on the triangle next to `public static void main(String[] args)` 
- The server should start.
- You are ready to test out the api in any api clients.


## Endpoints ##
### Authentication Controller

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
  - create new Account (default with ROLE_USER)
  - save User to database using AccountRepo
  - return http status `201 CREATED`
   

- **POST** : `/auth/login`
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

- **POST** : `/auth/logout`
  
  - logout with Spring Security
  - invalidate and remove HttpSession
  - clear securityContextHolder
  - return http status `200 OK`


### Authorization Controller

This controller provides APIs for authenticated user to fetch user, update nickname and password.
- **GET** : `/resource/user`

    - get account from `@AuthenticationPrincipal` (provided by Spring Security)
    - return a `SigninResponse` DTO with http status `201 CREATED`
- **PATCH** : `/resource/user`
    - `Map<String, Object> userRequestDto` is used to map the following Json object.
       ``` 
       {
          "nickname": "JohnUser"
       }
       ```
    - get account from `@AuthenticationPrincipal` (provided by Spring Security)
    - update nickname and save to database using `AccountRepo` 
    - return a `SigninResponse` DTO with http status `200 OK`
- **PATCH** : `/resource/updatePassword`
    - `PasswordRequestDto passwordRequestDto` is used to map the following Json object.
       ``` 
       {
          "currentPassword": "password1",
          "newPassword": "password2"
       }
       ```
    - get account from `@AuthenticationPrincipal` (provided by Spring Security)
    - verify currentPassword matches account password
    - update password and save to database using `AccountRepo`
    - return http status `200 OK`

### Public resources
The public resources is open to all, no account needed.

**MenuitemController**
   - **GET** : `/menuitems`
     - get all `Menuitem` from `MenuitemRepo`
     - return `Menuitem` list  with http status `200 OK`
     
   - **GET** : `/bestseller`
     - get top 3 bestselling `Menuitem` from a query
       ```
       @Query ("SELECT pl.menuitem.id, m.title, sum(pl.quantity) " +
            "FROM PurchaseLineitem pl " +
            "inner join Menuitem m on m.id = pl.menuitem.id " +
            "group by pl.menuitem.id " +
            "order by 3 desc"
        )
        ```
     - return `Menuitem` list  with http status `200 OK`
- **GET** : `/category/{id}/menuitems`
    - get `Menuitem` by `CategoryId` from a query
      ```
      @Query("SELECT m FROM Menuitem m Where m.category.id=:categoryId")
       ```
    - return `Menuitem` list  with http status `200 OK`
  
**CategoryController**
  - **GET** : `/categories`
      - get all `Category` from `CategoryRepo`
      - return `Category` list  with http status `200 OK`

**MilkController**
- **GET** : `/milks`
    - get all `Milk` from `MilkRepo`
    - return `Milk` list  with http status `200 OK`
- **GET** : `/milks/{id}`
  - get Milk by id from `MilkRepo`
  - return `Milk` object with http status `200 OK`

**SizeController**
- **GET** : `/sizes`
    - get all `Size` from SizeRepo`
    - return `Size` list  with http status `200 OK`
     
**SugarController**
- **GET** : `/sugars`
    - get all `Sugar` from SugarRepo`
    - return `Sugar` list  with http status `200 OK`

  
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






