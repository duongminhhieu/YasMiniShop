# YASmini: Yet Another Shop Mini

YASmini is a pet project aimed to practice building a typical Spring Boot application in Java

<div style="text-align: center;">
    <img width="315" alt="Screenshot 2024-06-02 at 23 47 57" src="https://github.com/duongminhhieu/YasMiniShop/assets/76527212/936dabf5-2935-4335-95ec-cae392da309d">
</div>


[![Development CI-CD](https://github.com/duongminhhieu/SpringSecurity/actions/workflows/development-cicd.yml/badge.svg)](https://github.com/duongminhhieu/SpringSecurity/actions/workflows/development-cicd.yml)
[![Production CI-CD](https://github.com/duongminhhieu/SpringSecurity/actions/workflows/production-cicd.yml/badge.svg)](https://github.com/duongminhhieu/SpringSecurity/actions/workflows/production-cicd.yml)

# Technologies
- Java 21
- Spring boot 3.2.5
- PostgreSQL
- Firebase Storage
- Vertex AI
- GitHub Actions
- SonarCloud

# Architecture

<img width="1399" alt="Screenshot 2024-06-02 at 23 44 13" src="https://github.com/duongminhhieu/YasMiniShop/assets/76527212/4298b894-e318-4a18-9df1-3bf32cee4b74">


# Use Case
![YasMini drawio (6)](https://github.com/duongminhhieu/YasMiniShop/assets/76527212/5174d2db-e41a-42cd-9464-1e637f23c317)

## Overview

Yas Mini is an e-commerce platform that allows public users, customers, and admin users to interact with various functionalities such as viewing products, managing orders, and handling customer data. The system is divided into modules for public users, registered customers, and administrators.

## Actors

- **Public User**: Anyone who visits the site without logging in.
- **Customer**: A registered user who can purchase products.
- **Admin**: A user with administrative privileges to manage products, orders, customers, and other administrative tasks.

## Use Cases Summary

| Use Case              | Description                                                             | Actor        | Preconditions                   | Postconditions                     |
|-----------------------|-------------------------------------------------------------------------|--------------|----------------------------------|------------------------------------|
| **View Products**     | View product details, featured products, and products by category       | Public User  | None                             | None                               |
| **Search Products**   | Search for products based on various criteria                           | Public User  | None                             | Display search results             |
| **Register**          | Create a new user account                                               | Public User  | None                             | User account created               |
| **Add to Cart**       | Add products to shopping cart                                           | Customer     | User must be logged in           | Product added to cart              |
| **Order Product**     | Place an order for products in the cart                                 | Customer     | Products in cart, user logged in | Order placed                       |
| **View My Orders**    | View past and current orders                                            | Customer     | User must be logged in           | Display order list                 |
| **Search by Image with AI** | Upload an image to search for similar products using AI           | Customer     | User must be logged in           | Display search results             |
| **Rate Product**      | Rate products (one rating per product)                                  | Customer     | User must be logged in, not rated before | Rating submitted           |
| **Manage Orders**     | View, paginate, and change order status                                 | Admin        | Admin must be logged in          | Order statuses updated             |
| **Manage Products**   | Create, read, update, delete, and filter products                       | Admin        | Admin must be logged in          | Product list updated               |
| **Manage Categories** | Create, update, and delete product categories                           | Admin        | Admin must be logged in          | Category list updated              |
| **Manage Customers**  | View, activate, deactivate, and paginate customer records               | Admin        | Admin must be logged in          | Customer list updated              |
| **View Statistics**   | View platform performance metrics and statistics                        | Admin        | Admin must be logged in          | Display statistics                 |
| **Authentication**    | Handle user login, logout, and session management 

# Database Modeling
![rookie_phase1_db - public](https://github.com/duongminhhieu/YasMiniShop/assets/76527212/4df44922-26dc-47d6-b1d9-d63b0f44c2b5)

# Setting Up and Running at Local

## Configuration

Include setup steps for Firebase authentication, PostgreSQL database connection, and any other necessary configurations.

1. Set up Firebase:

    - Create a Firebase project in the Firebase Console.
    - Configure Firebase Authentication and download the service account JSON file.

2. Set up PostgreSQL:

    - Install PostgreSQL on your system.
    - Create a new PostgreSQL database for your application.
    
3. Config **application.yml** file to run in the local environment:

    - In **application.yml** file. Let change the **json-file** (path to your credentials file) and **storage-bucket**
   ```bash
      firebase:
        json-file: this is your credentials file (.json)
        storage-bucket: yasmini.appspot.com (this is your storage-bucket)
   ```
   - After that, change **url**, **username** and **password** of your database.
   ```bash
     datasource:
        url: this is your url
        username: this is your username
        password: this is your password
   ```
## Run
1. Build the Project
```bash
    ./mvnw clean install  
```
2. Run the Application
```bash
    ./mvnw spring-boot:run
```

# Contact
* Skype: live:duongminhhieu2082002
* Please ping me if you can't run the app.

# References
1. [JPA & JWT (Hoang Nguyen)] (https://github.com/hoangnd-dev/rookies-java)
2. [Springboot Demo (Phu Le)] (https://github.com/phulecse2420/demo)
3. [Devteria] (https://github.com/devteria/identity-service.git)
4. [Yas real] (https://github.com/nashtech-garage/yas)
5. NashTech Slide
6. GitHub Copilot




