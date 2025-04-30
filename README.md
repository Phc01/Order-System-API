# Order System APi

## Diagrama de Classes
```mermaid
classDiagram
    class User {
        +Long id
        +String name
        +String email
    }

    class Category {
        +Long id
        +String name
    }

    class Product {
        +Long id
        +String name
        +String description
        +BigDecimal price
        +int stock
    }

    class Order {
        +Long id
        +LocalDateTime createdAt
        +BigDecimal total
        +String status
    }

    class OrderItem {
        +Long id
        +int quantity
        +BigDecimal unitPrice
        +BigDecimal subtotal
    }

    User "1" --> "*" Order : places
    Order "*" --> "*" OrderItem : contains
    Product "1" --> "*" OrderItem : part of

