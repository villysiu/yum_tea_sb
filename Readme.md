# YumTea #
This is the backend API for a tea e-commerce application built with Springboot, secured by Spring Security and JWT.

The Frontend is developed in **React.js** and can be found here: 
https://github.com/villysiu/yumtea_sb_frontend

Youtube: Coming soon
### Technology 
- Java 17
- JDK 23
- Springboot 3

### Dependencies used in the project includes: 
- Spring BootDevTools
- **Spring Web** for creating RESTful APIs
- **Spring Security 6** for handling authentication and authorization
- **Json Web Token** for JWT authentication
- **JDBC API** for connecting to MySQL through a JDBC driver
- **Spring Data JPA** for interacting with MySQL
- MySQL
- Lombak (Optional)
- junit (Optional)
- jakarta.validation (Optional)
- slf4j (Optional)

## Before you start ##
-  Check if MySQL is installed, 
-  Mac -> terminal or  windows -> command prompt
```
  mysql -V
```
  - For installation instruction, please visit [MySQL website](https://dev.mysql.com/doc/mysql-getting-started/en/).
- I also  highly recommend using **MySQLWorkbench** to view the database. It can be downloaded [here](https://dev.mysql.com/downloads/workbench/).
- Start up MySQL at `localhost:3306`
- create a new schema,`yumtea_sb2`. (The name of the database can be found in `resources/application.properties`)

![](https://github.com/villysiu/yum_tea_sb/blob/main/src/main/resources/static/images/MySQLWorkbench.png?raw=true)
## Running the project for the first time ##
- Clone the Git repository https://github.com/villysiu/yum_tea_sb.git
- Open Intellij, 
  - File -> New -> Project from Version Control
  - Paste the cloned git link into the `URL` box
  - Click `Clone`

- Choose the Spring Boot Application file `/src/main/java/com.villysiu.yumtea/YumteaApplication/`. Click on the triangle next to `public static void main(String[] args)`
- The server will start at `http://localhost:8080`.

## Database Diagram
![](https://github.com/villysiu/yum_tea_sb/blob/main/src/main/resources/static/images/yumtea_sb@localhost.png?raw=true)

- If this is the first time you start up the application, 
  - the following data will be automatically inserted into the database. 
    - a super admin `Account` with `ROLE_ADMIN` authority
      - email: **springadmin@gg.com**
      - password: **password1**
      - nickname: **superadmin**
      
    - 3 user `Accounts` with `ROLE_USER` authority
      - email: **springuser@gg.com, springuser2@gg.com, springuser3@gg.com**
      - password: **password1**
      - nickname: **superuser**
    
    - `Categories`
      - **Black Tea**, **Oolong Tea**, **Jasmine Tea**, and **Caffeine Free**
    - `Milks`
      - **NA, No Milk, Whole Milk, Nonfat Milk, Almond Milk, Oat Milk, Coconut Milk**
    - `Sizes`
      - **8oz, 12oz, 16oz**
    
    - `Menuitems`
      - **ChaiEarl, Grey, English Breakfast, Jasmine, Dragon Pearl, Silver Needle, Genmaicha, 
  Iced Strawberry Lemonade, Tumeric Ginger, Chamomile, Hibiscus Berry, Min, Peppermint, Frozen Lemonade,
    Hot Chocolate, Oolong, Iron Goddess of Mercy, High Mountain Tea**
    - **50** `Purchases` by random user `Accounts`, with various `PurchaseLineitems` with random `Menuitems` and options.
    

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
  - check if **email** already in database
  - throws `EntityExistsException` if email already in database and returns http status `409 Conflict`
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
  - throw `AuthenticationException` if bad credential and returns http status `401 unauthorized`
  - update SecurityContext using Authentication object
  - generates **JWT token** and stored it in cookie to be sent along in HttpServeletRequest and HttpServeletResponse
  - return a `SigninResponse` DTO with http status `200 OK`

- **POST** : `/auth/logout`
  
  - logout with Spring Security
  - clear securityContextHolder
  - remove **JWT token**
  - return http status `200 OK`


### Authorization Controller

This controller provides APIs for **authenticated user** with valid JWT tokens. It throws `401 Unauthorized` if there is no authenticated account. Authenticated account can only access its own account, not others' account.

- **GET** : `/resource/user`
  - fetch the current authenticated user
  - get authenticated account from `@AuthenticationPrincipal` (provided by Spring Security)
  - return a `SigninResponse` DTO with http status `200 Ok`
  

- **PATCH** : `/resource/user`
  - Updates user nickname
  - `Map<String, Object> userRequestDto` is used to map the following Json object.
       ``` 
       {
          "nickname": "JohnUser"
       }
       ```
    - get authenticated account from `@AuthenticationPrincipal` (provided by Spring Security)
    - update nickname and save to database using `AccountRepo` 
    - return a `SigninResponse` DTO with http status `200 OK`
    
- **PATCH** : `/resource/updatePassword`
  - Updates user nickname
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

**TaxRateController**
- **GET**: `/taxes/{state}`
  - get `taxRate` by state from database using `TaxRepo`
  - return `taxRate` with http status `200 OK`


### Private resources for ROLE_ADMIN
Thees private resources are **ONLY** available to `Account` with `Role` **ROLE_ADMIN** authority. Throws `Unauthorized 401` if not **ROLE_ADMIN**.

**MenuitemController**
- **POST** : `/menuitem`
  - create new `Menuitem`
  - input `MenuitemDto` and save to database using `createMenuitem` in MenuitemService
      ``` 
    MenuitemDto
      {
          "title": "Chai",
          "imageUrl": "",
          "description": "chai",
          "categoryId": 1,
          "milkId": 2,
          "temperature": "FREE",
          "sugar": "ZERO",
          "price": 5
      }
      ```
      - return Menuiem http status `201 CREATED`
    

- **PATCH** : `/menuitem/{id}`
  - update fields of `Menuitem` by `id`
     ``` 
     Map<String, Object> menuitemDto
     {
       "title": "Chai2",
       "sugar": "FIFTY"
     }
     ```
 - return `Menuitem` http status `200 OK`
 - throw `EntityNotFoundException` if `Menuitem` with `id` not existed


- **DELETE** : `/menuitem/{id}`
  - throw `EntityNotFoundException` if `Menuitem` with `id` not existed
  - delete `Menuitem` by `id`
  - return http status `404 No Content`


- **POST** : `/menuitem/{id}/img`
  - stores `file` in Multipart object in designated folder `src/main/resources/static/images`
  - update `Menuitem` with `image` file. 
  - return `Menuitem` http status `200 OK`
  - throw `EntityNotFoundException` if `Menuitem` with `id` not existed


- **DELETE** : `/menuitem/{id}/img`
  - update `menuitem.imageUrl` to null.
  - return http status `404 No Content`
  - throw `EntityNotFoundException` if `Menuitem` with `id` not existed


- **PATCH** : `/menuitem/{id}/toggleActive`
  - toggle Menuitem visibility
  - return http status `200 OK`
  - throw `EntityNotFoundException` if `Menuitem` with `id` not existed
  
**CategoryController**
- **POST** : `/category`
  - create new `Category`
    ```
    {
      "title": "Black Tea",
      "description": "Black Tea",
      "imageUrl": "blacktea.jpg"
    }
    ```
  - return `Category` http status `201 CREATED`
  - throw `EntityNotFoundException` if `Category` with `id` not existed


- **PATCH** : `/category/{id}`
  - update `Category` fields
      ``` 
      {
        "description": "Black tea is one of the bestseller"
      }
      ```
  - return `Category` http status `200 OK`
  - throw `EntityNotFoundException` if `Category` with `id` not existed


- **DELETE** : `/category/{id}`
  - delete `Category` from database
  - return http status `404 No Content`
  - throw `EntityNotFoundException` if `Category` with `id` not existed

- **MilkController**
  - **POST** : `/milk`
  - **PATCH** : `/milk/{id}`
  - **DELETE** : `/milk/{id}`

- **SizeController**
  - **POST** : `/size`
  - **PATCH** : `/size/{id}`
  - **DELETE** : `/size/{id}`
  
- **GET**: `/purchases/all`
  - get all purchases with details by all users

- **GET**: `/accounts`
  - get all user accounts

- **PATCH**: `/accounts/{id}`
  - toggle `Account` with `id` between `ROLE_ADMIN` and `ROLE_USER`
  - throw `EntityNotFoundException` if `Account` with `id` does not exist

- **DELETE**: `/accounts/{id}`
  - delete `Account` with `id`
  - throw `EntityNotFoundException` if `Account` with `id` does not exist


### Private Resources accessed **only** by authenticated account. 
- **Only** authenticated `Account` with `ROLE_USER` can access his own `Cart` and `Purchase` objects.

**CartController**
- **GET** : `/carts`
    - get `Account` from `@AuthenticationPrincipal` (provided by Spring Security)
    - get all `Cart` owned by `Account` from database using `CartRepo` 
    - return `Cart` list  with http status `200 OK`

- **POST** : `/cart`
  - create new `Cart`
  - `CartInputDto` DTO is used to map the following Json object.
    ``` 
    {
       "menuitemId": 2,
       "milkId": 9,
       "sizeId": 2,
       "quantity": 1,
       "sugar": "TWENTY_FIVE",
       "temperature": "HOT"
    }
    ```
  - save `Cart` to database using `CartRepo`
  - return `Cart` http status `201 CREATED`
  
- **PUT** : `/cart/{id}`
  - update `Cart` by `id` 
  - `CartInputDto` DTO is used to map the following Json object.
    ``` 
    {
       "milkId": 3,
       "sizeId": 2,
       "quantity": 2,
       "sugar": "SEVENTY_FIVE",
       "temperature": "HOT"
    }
    ```
  - return `Cart` http status `200 OK`
  - throw `EntityNotFoundException` if `Cart` with `id` not existed
  

- **DELETE** : `/cart/{id}`
    - delete `Cart` from database using `CartRepo`
    - return http status `404 No Content`
    - throw `EntityNotFoundException` if `Cart` with `id` not existed


**PurchaseController**
- **GET** : `/purchases`
    - get all `Purchase` owned by authenticated `Account` from database using `PurchaseRepo`
    - return `Purchase` list  with http status `200 OK`


- **GET** : `/purchases/{id}`
    - get `Purchase` by `id` and by authenticated `Account` from database using `PurchaseRepo`
    - return `Purchase`  with http status `200 OK`
    - throw `EntityNotFoundException` if `Purchase` with `id` not existed

- **POST** : `/purchase`
  - create new `Purchase`
  - `PurchaseRequest` DTO is used to map the following Json object.
    ``` 
    {
       "tip": 5.5,
       "state": "WA"
    }
    ```
  - fetch all `Cart` belonged to authenticated `Account` from database using `CartRepo`
  - save each `Cart` to `PurchaseLineitem`
  - calculate `tax` by `state` and `total` 
  - save `Purchase` to database using `PurchaseRepo` again
  - remove all `Cart` belonged to `Account` from database 
  - return `Purchase` with http status `201 CREATED`


- **DELETE** : `/purchase/{id}`
    - delete `Purchase` from database 
    - return http status `404 No Content`
    - throw `EntityNotFoundException` if `Purchase` with `id` not existed

  
**Thank you for reading this far. 
I hope you found this API helpful in creating ecommerce in Springboot.
Happy Coding!**





