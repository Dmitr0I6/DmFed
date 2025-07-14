package ru.dmitry.tgBot.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class OrderProduct {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private ClientOrder clientOrder;

    @ManyToOne
    private Product product;

    @Column(nullable = false)
    private Long countProduct;

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", clientOrder=" + clientOrder +
                ", product=" + product +
                ", countProduct=" + countProduct +
                '}';
    }

    public OrderProduct() {
    }

    public OrderProduct(Long id, ClientOrder clientOrder, Long countProduct, Product product) {
        this.id = id;
        this.clientOrder = clientOrder;
        this.countProduct = countProduct;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(Long countProduct) {
        this.countProduct = countProduct;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ClientOrder getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(ClientOrder clientOrder) {
        this.clientOrder = clientOrder;
    }
}
