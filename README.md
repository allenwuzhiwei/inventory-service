# 📦 Inventory MicroService - E-commerce Backend Module

## Overview

The **Inventory Service** is a core microservice within our e-commerce platform.  
It is responsible for managing product inventory data, including real-time stock tracking, deduction during order placement, restoration on order cancellation, and inventory logging.

It is built using **Spring Boot**, **MyBatis-Plus**, and supports RESTful API interaction with other microservices like Order Service and Product Service.

---

## Project Structure

The `inventory-service` project follows a modular **Spring Boot + MyBatis Plus** architecture,  
with a layered structure for configuration, controller, service, mapper, and entity responsibilities.

- `config/`  
  ➤ Contains API response model, global exception handling, Swagger integration, and security configuration.

- `controller/`  
  ➤ Exposes RESTful APIs for inventory operations such as retrieval, deduction, restoration, and log viewing.

- `dto/`  
  ➤ Contains request body definitions for stock deduction and rollback (e.g. `InventoryChangeRequest`, `InventoryRestoreRequest`).

- `entity/`  
  ➤ Domain model classes:  
  `Inventory` - represents stock quantity for a product  
  `InventoryLog` - records each inventory operation

- `mapper/`  
  ➤ MyBatis Plus mappers for performing CRUD operations on inventory and logs.

- `service/` and `service/impl/`  
  ➤ Business logic for managing stock and audit logs.

- `resources/`  
  ➤ Contains `application.properties` and other Spring Boot resource files.

- `InventoryApplication.java`  
  ➤ Main entry point of the inventory microservice.

- `pom.xml`  
  ➤ Maven project configuration for dependencies and build management.

---

## ✅ Implemented Features

### 🧾 Inventory Module

#### ✅ Core Functions

- Get inventory quantity by product ID
- Add, update, and delete inventory records
- Deduct stock (used during order creation)
- Restore stock (used for order cancellation)
- Retrieve inventory operation logs

#### 💡 Extended Logic

- Maintains full audit logs (`InventoryLog`) for all stock changes (IN / OUT)
- Supports atomic deduction and rollback for integration with Order Service
- Extensible with Feign clients or message queues in future

---

## 👥 Collaborators

| Name             | Role               | Description                                                                                                                                                |
|------------------|--------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Song Yinrui**  | Backend Developer  | Responsible for core backend development. Designed and implemented RESTful APIs, business logic, and API documentation/testing for Inventory MicroService. |
| **Wu Zhiwei**    | System Architect   | Designing and setting up the microservice architecture and framework.  Creating the corresponding database tables.                                         |

---

## Author

- Song Yinrui
- National University of Singapore, Institute of Systems Science (NUS-ISS)
- Development Start: May 2025
