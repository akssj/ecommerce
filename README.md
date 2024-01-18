# MyFirstWebApp aka alledrogo

![obraz](https://github.com/akssj/MyFirstWebApp/assets/127445850/9864ce7c-8620-4513-9524-bd8561fc4d69)

## Intro
This is an attempt to make a scalable web app using rest api. 
Technologies and code itself is not well-thought-out due to lack of knowledge about any of this. 
Anyway im doing it to get some know-how about creating and maintaining project.

###
Project documentation:
https://akssj.github.io/MyFirstWebApp/index.html

## Features
+ REST API webapp
+ Login (authentication)
+ JWT and User data saved in cookies
+ CRUD products, users (TODO authorization)
+ Database (User and Products tables)
+ Unit and integration api tests (Java + TestNG + REST Assured + Mockito)
+ Javadoc Documentation
+ Current task: FRONTEND :(
+ TODO external api
+ TODO User profile, product related pages
+ TODO Admin role, page, etc.
+ TODO Track user behavior
+ TODO Backend gui
+ TODO Fix bugs (ie errors after token expiration - user is not auto logged out)
+ TODO Error messages, codes
+ TODO Security
+ TODO refactor backend + Update code to be less painful to look at

## Tech
+ architecture: mvc/rest
+ Language: 
  + Backend: Java(Spring boot, JWT)
  + Frontend: html, css(Bootstrap), .js
+ Database: postgresql
+ Test tech: TestNG, REST Assured, postman

## Api
+ MainPage:
+ /main - GET main page, provides main.html file
+ /static/* - GET provides .js, css files for the user
+ USER RELATED: /auth + :
+ /login - POST self-explanatory
+ /signup - POST self-explanatory
+ /{id}/userStatus - GET provide user with current account data
+ /{id}/delete - DELETE deletes current user account
+ /{id}/update - PUT update current user account data
+ PRODUCT RELATED: /product + :
+ /forSale - GET provides list of products available to buy
+ /bought - GET provides list of products bought by current user
+ /sold - GET provides list of products sold by current user
+ /all - GET provides list of all the products
+ /handling/add - POST adds product for sale
+ /handling/{id}/delete - DELETE delete product created by current user
+ /handling/{id}/buy - PUT buy product created by other user



