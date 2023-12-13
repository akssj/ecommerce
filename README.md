# MyFirstWebApp aka alledrogo
## Intro
This is an attempt to make a scalable web app using rest api. 
Technologies and code itself is not well-thought-out due to lack of knowledge about any of this. 
Anyway im doing it to get some know-how about creating and maintaining project.

## Features
+ Single page web app with api to change page content (I might want to change that)
+ Login, Signup (authentication)
+ User data saved in session(local storage at this point)
+ buy, sell, list for sale products - for everyone to see!(TODO authorization need roles and such)
+ Database (User and Products tables)
+ Some tests(Unit, integration api)
+ Somewhat proper Documentation
+ Current task: FRONTEND :(
+ TODO Use of some external api
+ TODO User profile(data) page
+ TODO Looking up other users
+ TODO Admin role, page, etc.
+ TODO Track user behavior
+ TODO Backend gui
+ TODO Fix bugs (ie errors after token expiration - user is not auto logged out)
+ TODO Error messages, codes
+ TODO Security
+ TODO .gitignore
+ TODO refactor backend + Update code to be less painful to look at

## Tech
+ architecture: mvc/rest
+ Language: 
  + Backend: Java(Spring boot, jwt)
  + Frontend: html, css, .js
+ Database: postgresql
+ Test tech: TestNG, REST Assured

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



