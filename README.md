# MyFirstWebApp aka Alledrogo
## Intro
This is an attempt to make a scalable web app using rest api. 
Technologies and code itself is not well-thought-out due to lack of knowledge about any of this. 
Anyway im doing it to get some know-how about creating and maintaining project.

## Features
+ Single page with api to change page content (I might want to change that)
+ Login, Signup (authorization)
+ User data saved in session(local storage at this point)
+ CRUD products for users to buy (TODO authentication)
+ Database (User and Products)
+ CURRENT TASK: TODO Tests(TestNG, maybe selenium), TCs, Documentation
+ TODO Use of some external api (idk what for but I want to do that)
+ TODO User profile, data
+ TODO Looking up other users
+ TODO Admin role, page, etc.
+ TODO Track user behavior
+ TODO Update code to be less painful to look at
+ TODO FRONTEND (ie button to change language)
+ TODO Backend gui
+ TODO Fix bugs (ie errors after token expiration - user is not auto logged out)
+ TODO Error messages, codes
+ TODO Security
+ TODO .gitignore

## Tech
+ architecture: mvc/rest
+ Language: 
  + Backend: Java,
  + Frontend: html, css, .js
+ Library: Spring boot, jwt,
+ Database: postgresql
+ Test tech: TestNG, REST Assured

## Api
+ /main - GET main page, provides main.html file
+ /api/static - GET provides .js, css files for the user
+ USER RELATED:
+ /api/auth/login - POST self-explanatory
+ /api/auth/signup - POST self-explanatory
+ /api/auth/userStatus - GET update current user data on demand
+ PRODUCT RELATED:
+ /api/product/forSale - GET provides list of available products
+ /api/product/all - GET provides list of all products
+ /api/product/bought - GET provides list of bought products by current user
+ OPERATIONS ON PRODUCTS:
+ /api/product/handling/add - POST allow current user to add product for sale
+ /api/product/handling/delete/{id} - DELETE allow current user to delete created product
+ /api/product/handling/buy/{id} - PUT allow current user to buy product



