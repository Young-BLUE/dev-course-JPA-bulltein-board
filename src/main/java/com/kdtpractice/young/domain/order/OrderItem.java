package com.kdtpractice.young.domain.order;

import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "order_item")
@Getter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private int price;

    private int quantity;

    // fk
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "item_id")
    private Long itemId;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Item item;

    public void setOrder(Order order) {
        if (Objects.nonNull(this.order)) {
            order.getOrderItems().remove(this);
        }
        this.order = order;
        order.getOrderItems().add(this);
    }

    public void setItem(Item item) {
        if(Objects.nonNull(this.item)) {
            item.getOrderItems().remove(this);
        }
        this.item = item;
        item.getOrderItems().add(this);
    }
}
