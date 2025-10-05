# Project Functionalities Report

## Project Name: Marketplace

## Description

This project is a Marketplace application developed for UADE. It provides a RESTful API for managing users, products, categories, orders, and authentication.

## Technologies Used

*   **Java Version:** 17
*   **Framework:** Spring Boot 3.5.4
*   **Libraries:**
    *   Spring Web
    *   Spring Data JPA
    *   Spring Security
    *   JWT (JSON Web Token)
    *   Lombok
*   **Build Tool:** Maven
*   **Database:** MySQL

## Functionalities

### Authentication

*   **Register a new user:**
    *   **Endpoint:** `POST /auth/register`
    *   **Description:** Allows a new user to register in the system.
*   **Authenticate a user:**
    *   **Endpoint:** `POST /auth/authenticate`
    *   **Description:** Authenticates a registered user and returns a JWT token for authorization.

### Users

*   **Create a new user:**
    *   **Endpoint:** `POST /users`
    *   **Description:** Creates a new user.
*   **Get all users:**
    *   **Endpoint:** `GET /users`
    *   **Description:** Retrieves a list of all registered users.
*   **Get user by ID:**
    *   **Endpoint:** `GET /users/{id}`
    *   **Description:** Retrieves a specific user by their ID.
*   **Get user by username:**
    *   **Endpoint:** `GET /users/username/{username}`
    *   **Description:** Retrieves a specific user by their username.
*   **Search users by username:**
    *   **Endpoint:** `GET /users/search?username={username}`
    *   **Description:** Searches for users whose username contains the given string.
*   **Update a user:**
    *   **Endpoint:** `PUT /users/{id}`
    *   **Description:** Updates the details of a specific user.
*   **Delete a user:**
    *   **Endpoint:** `DELETE /users/{id}`
    *   **Description:** Deletes a specific user from the system.

### Roles

*   **Create a new role:**
    *   **Endpoint:** `POST /roles`
    *   **Description:** Creates a new user role (e.g., ADMIN, USER).
*   **Get all roles:**
    *   **Endpoint:** `GET /roles`
    *   **Description:** Retrieves a list of all available roles.

### Categories

*   **Create a new category:**
    *   **Endpoint:** `POST /categories`
    *   **Description:** Creates a new product category.
*   **Get all categories:**
    *   **Endpoint:** `GET /categories`
    *   **Description:** Retrieves a list of all product categories.
*   **Get category by ID:**
    *   **Endpoint:** `GET /categories/{id}`
    *   **Description:** Retrieves a specific category by its ID.
*   **Update a category:**
    *   **Endpoint:** `PUT /categories/{id}`
    *   **Description:** Updates the details of a specific category.
*   **Delete a category:**
    *   **Endpoint:** `DELETE /categories/{id}`
    *   **Description:** Deletes a specific category.

### Products

*   **Create a new product:**
    *   **Endpoint:** `POST /products`
    *   **Description:** Creates a new product. This endpoint supports `multipart/form-data` for uploading product images along with the product details.
*   **Get all products:**
    *   **Endpoint:** `GET /products`
    *   **Description:** Retrieves a list of all available products.
*   **Get product by ID:**
    *   **Endpoint:** `GET /products/{id}`
    *   **Description:** Retrieves a specific product by its ID.
*   **Search products by name:**
    *   **Endpoint:** `GET /products/search?name={name}`
    *   **Description:** Searches for products whose name contains the given string.
*   **Get products by category:**
    *   **Endpoint:** `GET /products/category/{categoryId}`
    *   **Description:** Retrieves a list of all products belonging to a specific category.

### Product Images

*   **Upload an image for a product:**
    *   **Endpoint:** `POST /product-images/{productId}`
    *   **Description:** Uploads an image and associates it with a specific product.
*   **Get an image by ID:**
    *   **Endpoint:** `GET /product-images/{id}`
    *   **Description:** Retrieves a specific image by its ID.
*   **Get all images for a product:**
    *   **Endpoint:** `GET /product-images/product/{productId}`
    *   **Description:** Retrieves all images associated with a specific product.
*   **Delete an image:**
    *   **Endpoint:** `DELETE /product-images/{id}`
    *   **Description:** Deletes a specific image.

### Orders

*   **Create a new order:**
    *   **Endpoint:** `POST /order/users/{buyerId}/orders`
    *   **Description:** Creates a new order for a specific user.
*   **Get all orders for a user:**
    *   **Endpoint:** `GET /order/users/{buyerId}/orders`
    *   **Description:** Retrieves a list of all orders placed by a specific user.
*   **Get a specific order for a user:**
    *   **Endpoint:** `GET /order/users/{buyerId}/orders/{orderId}`
    *   **Description:** Retrieves a specific order for a specific user.
*   **Delete an order for a user:**
    *   **Endpoint:** `DELETE /order/users/{buyerId}/orders/{orderId}`
    *   **Description:** Deletes a specific order for a specific user.
