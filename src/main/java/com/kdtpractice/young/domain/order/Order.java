package com.kdtpractice.young.domain.order;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Order {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "Id")
    private String id;

    @Column(name = "memo")
    private String memo;

    @Column(name = "order_status")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "order_datetime", columnDefinition = "TIMESTAMP")
    private LocalDateTime orderDataTime;

    // member_fk
    @Column(name = "member_id", insertable = false, updatable = false) // fk
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "id") // 참조할 ColumnNmae, 만약 따로 설정하지 않으면 스네이크 케이스 테이블의 Id 값을 참조
    private Member member; // 참조를 통해서 관계를 맺는다

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public void setMember(Member member) {
        if(Objects.nonNull(this.member)){
            member.getOrders().remove(this);
        }

        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
    }
}
