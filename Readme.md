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

**TaxRateController **
- **GET**: `/taxes/{state}`
  - get `taxRate` by state from database using `TaxRepo`
  - return `taxRate` with http status `200 OK`


### Private resources for USER_ADMIN
Thees private resources are **ONLY** available to `Account` with `Role` **USER_ADMIN** authority.

**MenuitemController**
- **POST** : `/menuitem`
    - `MenuitemDto` DTO is used to map the following Json object.
      ``` 
      {
          "title": "Chai",
          "imageUrl": "chai.jpg",
          "description": "chai",
          "categoryId": 1,
          "milkId": 2,
          "temperature": "FREE",
          "sugar": "ZERO",
          "price": 5
      }
      ```
    - create new `Menuitem` 
    - save `Menuitem` to database using `MenuitemRepo`
    - return Menuiem http status `201 CREATED`
- **PATCH** : `/menuitem/{id}`
    - `Map<String, Object> menuitemDto` DTO is used to map the following Json object.
      ``` 
      {
        "title": "Chai2",
        "sugar": "FIFTY"
      }
      ```
    - fetch `Menuitem` by `id` from database using `MenuitemRepo`
    - update `Menuitem` fields
    - save `Menuitem` to database using `MenuitemRepo`
    - return `Menuitem` http status `200 OK`
- **DELETE** : `/menuitem/{id}`

  - verify `Menuitem` existed in database using `MenuitemRepo`
  - delete `Menuitem` to database using `MenuitemRepo`
  - return http status `404 No Content`
  - 
**CategoryController**
- **POST** : `/category`
    - `CategoryDto` DTO is used to map the following Json object.
      ``` 
      {
        "title": "Black Tea",
        "description": "Black Tea",
        "imageUrl": "blacktea.jpg"
      }
      ```
    - create new `Category`
    - save `Category` to database using `CategoryRepo`
    - return `Category` http status `201 CREATED`
- **PATCH** : `/category/{id}`
    - `Map<String, Object> categoryDto` DTO is used to map the following Json object.
      ``` 
      {
        "description": "Black tea is one of the bestseller"
      }
      ```
    - fetch `Category` by `id` from database using `MenuitemRepo`
    - update `Category` fields
    - save `Category` to database using `MenuitemRepo`
    - return `Category` http status `200 OK`
- **DELETE** : `/category/{id}`

    - verify `Category` existed in database using `CategoryRepo`
    - delete `Category` from database using `CategoryRepo`
    - return http status `404 No Content`

**MilkController**
- **POST** : `/milk`
- **PATCH** : `/milk/{id}`
- **DELETE** : `/milk/{id}`

**SizeController**
- **POST** : `/size`
- **PATCH** : `/size/{id}`
- **DELETE** : `/size/{id}`

**SugarController**
- **POST** : `/sugar`
- **PATCH** : `/sugar/{id}`
- **DELETE** : `/sugar/{id}`

### Private Resources accessed **only** by authenticated account. 
- Authenticated `Account` with `ROLE_USER` can only access his own `Cart` and `Purchase` objects.

**CartController**
- **GET** : `/carts`
    - get `Account` from `@AuthenticationPrincipal` (provided by Spring Security)
    - get all `Cart` owned by `Account` from database using `Cartepo` 
    - return `Cart` list  with http status `200 OK`

- **POST** : `/cart`
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
    - create new `Cart`
    - save `Cart` to database using `CartRepo`
    - return `Cart` http status `201 CREATED`
  
- **PUT** : `/cart/{id}`
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
    - fetch `Cart` by `id` from database using `CartRepo`
    - update `Cart` fields
    - save `Cart` to database using `CartRepo`
    - return `Cart` http status `200 OK`
  

- **DELETE** : `/cart/{id}`

    - verify `Cart` existed in database using `CartRepo`
    - delete `Cart` from database using `CartRepo`
    - return http status `404 No Content`


**PurchaseController**
- **GET** : `/purchases`
    - get `Account` from `@AuthenticationPrincipal` (provided by Spring Security)
    - get all `Purchase` owned by `Account` from database using `PurchaseRepo`
    - return `Purchase` list  with http status `200 OK`
- **GET** : `/purchases/{id}`
    - get `Account` from `@AuthenticationPrincipal` (provided by Spring Security)
    - get `Purchase` by `id` and `Account` from database using `PurchaseRepo`
    - return `Purchase`  with http status `200 OK`

- **POST** : `/purchase`
    - `PurchaseRequest` DTO is used to map the following Json object.
      ``` 
      {
         "tip": 5.5,
         "state": "WA"
      }
      ```
    - get `Account` from `@AuthenticationPrincipal` (provided by Spring Security)
    - create new `Purchase`
    - save `Purchase` to database using `PurchaseRepo`
    - fetch all `Cart` belonged to `Account` from database using `CartRepo`
    - save each `Cart` to `PurchaseLineitem`
    - calculate `tax` by `state` and `total` 
    - save `Purchase` to database using `PurchaseRepo` again
    - remove all `Cart` belonged to `Account` from database usign `CartRepo`
    - return `Purchase` with http status `201 CREATED`


- **DELETE** : `/purchase/{id}`

    - verify `Purchase` existed in database using `PurchaseRepo`
    - delete `Purchase` from database using `PurchaseRepo`
    - return http status `404 No Content`


## Database Diagram
![](/images/yumtea_sb@localhost.png)

**Thank you for reading this far. 
I hope you found this API helpful in creating ecommerce in Springboot.
Happy Coding!**





