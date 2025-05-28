package com.project.ordersystemapi.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    private Integer quantity;

    private BigDecimal price;

    // Constructor, Getter e Setter

    public OrderItem(Long id, Product product, Order order, Integer quantity, BigDecimal price) {
        this.id = id;
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public Product getProduct() {
        return this.product;
    }

    public Order getOrder() {
        return this.order;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof OrderItem)) return false;
        final OrderItem other = (OrderItem) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$product = this.getProduct();
        final Object other$product = other.getProduct();
        if (this$product == null ? other$product != null : !this$product.equals(other$product)) return false;
        final Object this$order = this.getOrder();
        final Object other$order = other.getOrder();
        if (this$order == null ? other$order != null : !this$order.equals(other$order)) return false;
        final Object this$quantity = this.getQuantity();
        final Object other$quantity = other.getQuantity();
        if (this$quantity == null ? other$quantity != null : !this$quantity.equals(other$quantity)) return false;
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null ? other$price != null : !this$price.equals(other$price)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof OrderItem;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $product = this.getProduct();
        result = result * PRIME + ($product == null ? 43 : $product.hashCode());
        final Object $order = this.getOrder();
        result = result * PRIME + ($order == null ? 43 : $order.hashCode());
        final Object $quantity = this.getQuantity();
        result = result * PRIME + ($quantity == null ? 43 : $quantity.hashCode());
        final Object $price = this.getPrice();
        result = result * PRIME + ($price == null ? 43 : $price.hashCode());
        return result;
    }

    public String toString() {
        return "OrderItem(id=" + this.getId() + ", product=" + this.getProduct() + ", order=" + this.getOrder() + ", quantity=" + this.getQuantity() + ", price=" + this.getPrice() + ")";
    }

    public static class OrderItemBuilder {
        private Long id;
        private Product product;
        private Order order;
        private Integer quantity;
        private BigDecimal price;

        OrderItemBuilder() {
        }

        public OrderItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OrderItemBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public OrderItemBuilder order(Order order) {
            this.order = order;
            return this;
        }

        public OrderItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(this.id, this.product, this.order, this.quantity, this.price);
        }

        public String toString() {
            return "OrderItem.OrderItemBuilder(id=" + this.id + ", product=" + this.product + ", order=" + this.order + ", quantity=" + this.quantity + ", price=" + this.price + ")";
        }
    }
}
