# Taufer Tales API

**Taufer Tales API** is the Spring Boot backend for my demo project **Taufer Tales**.
The website is about managing books **(tales)** you already read and want to read.
It provides a modern interface to browse tales, manage your bookshelf, and write reviews.

The frontend source code can be found here https://github.com/elias-taufer/taufer-tales-web

The website is available under https://taufer.eu/

##  Features of the backend

- JWT login/register 
- REST endpoints for authentication, managing tales (creation, editing), and retrieving data for the frontend.
- Layered architecture with controller, services and repositories 
- Open Library integration to import tale from Open Library into the database
- Global error handler returning JSON errors
- CORS configured for your frontend origin
- Rate limiting for authentication
- Filter user input for possible harmful cross site scripts 

##  About Tales
A Tale represents a book.
Since books are not typically present in the Taufer Tales system, they must be created manually or imported from Open Library using a button in the *+New Tale* form.
For creating and editing tales a user must be logged in.

