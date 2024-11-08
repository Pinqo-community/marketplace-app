# Le Comptoir Local

## Overview

Welcome to the local products and short supply chains marketplace project! This platform aims to connect local producers with consumers by providing a simple and efficient solution to discover and purchase quality products, while supporting the local economy and reducing your environmental footprint.

## Context

Local consumption is on the rise, but producers often struggle to find suitable distribution channels. Our marketplace aims to bridge this gap by offering an intuitive and comprehensive platform that meets the needs of both parties:

Producers: A dedicated space to showcase their products, manage their orders, and track their sales.

Consumers: Easy access to a wide range of local products, with advanced search tools and transparent information about the origin of products.

## Objectives

Marketplace: Directly connect producers and consumers.
Advanced filtering: Allow users to quickly find the products they are looking for based on specific criteria (location, product type, certifications, etc.).
Order management: Simplify the ordering and delivery process, with complete traceability.
Producer interface: Provide producers with a clear dashboard to manage their business and analyze their performance.
Analytical tools: Allow consumers to assess the environmental impact of their purchases.

## Technologies

### **Frontend**

![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)
![Redux Toolkit](https://img.shields.io/badge/Redux%20Toolkit-764ABC?style=for-the-badge&logo=redux&logoColor=white)
![SCSS](https://img.shields.io/badge/SCSS%20Modules-CC6699?style=for-the-badge&logo=sass&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)

---

### **Backend**

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Stripe](https://img.shields.io/badge/Stripe-008CDD?style=for-the-badge&logo=stripe&logoColor=white)

## Features FrontEnd

- **Product browsing:** Search and filter products based on various criteria.
- **Product details:** Detailed product information
- **Shopping cart:** Add items to the cart, view cart contents, and proceed to checkout.
- **User profile:** Manage account information, view order history.

## Features BackEnd

- **User management:** User registration, authentication, and authorization.
- **Product management:** CRUD operations for products, including categories and attributes.
- **Order management:** Creation, processing, and tracking of orders.
- **Payment processing:** Integration with Stripe for secure payments.
- **Admin panel:** For managing the platform, including product moderation and user management.

## Setup environment :

- Install Java and Gradle
- Create a PostgreSQL database and configure the connection details in api/src/main/resources/application.yml
- Create a Stripe account and obtain your API keys.
- Configure the map API key:
  - Obtain an API key from [Jawg Maps](https://jawg.io).
  - Add the API key to your environment variables file or configuration setup.
  - Example setup in `.env`:
    ```env
    REACT_APP_MAP_API_KEY=your-api-key-here
    ```

## Getting Started

1. **Clone the repository:**
2. **Install dependencies:**
3. **Start developpment server**

## Quick start with docker-compose

1. **Clone the repository:**
2. **Create a .env file at the root of the project and set the following environment variables for the postgres database :**
   - DB_NAME
   - DB_USER
   - DB_PASSWORD
3. **Check if Docker is installed on your machine (or install it) and verify that the Docker daemon is running**
4. **Run the project**

```bash
docker compose up
```

## API Documentation

## Contributing

Feel free to contribute to this project by:

- Forking the repository
- Creating a new branch
- Making your changes
- Submitting a pull request

## Rules for branches

**Branch naming convention**

Please use kebab-case convention (words must be in lower case and linked by hyphens “-”) preceded by ID[issue]

- **feature**: New feature added;
- **bugfix**: Bugfix;
- **hotfix**: Critical bug fix;
- **chore**: Code cleanup;
- **experiment**: Functionality experimentation.

(e.g.: #1-feature-user-account)

## Licence
