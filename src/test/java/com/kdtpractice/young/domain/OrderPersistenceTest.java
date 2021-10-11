package com.kdtpractice.young.domain;

import com.kdtpractice.young.domain.order.*;
import com.kdtpractice.young.domain.order.Member;
import com.kdtpractice.young.domain.order.Order;
import com.kdtpractice.young.domain.order.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class OrderPersistenceTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager entityManager;

    @Autowired
    OrderRepository repository;

    @BeforeEach
    void SetUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Member Entity 생성")
    void member_insert() {
        entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Member member = Member.builder()
                .id(1L)
                .name("YoungBeom")
                .nickName("beommy")
                .age(13)
                .address("서울시 강남구")
                .description("어떻게하면 개발을 잘할 수 있을까")
                .description("0")
                .build();

        entityManager.persist(member);

        transaction.commit();
    }

    @Test
    @DisplayName("Member와 Order간의 fk test")
    void fk_test() {
        entityManager = emf.createEntityManager();
        EntityTransaction transaction1 = entityManager.getTransaction();

        transaction1.begin();

        Member member = Member.builder()
                .id(1L)
                .name("YoungBeom")
                .nickName("beommy-")
                .age(13)
                .address("서울시 강남구")
                .description("어떻게하면 개발을 잘할 수 있을까")
                .description("0")
                .build();

        entityManager.persist(member);

        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .memo("부재시 연락주세요.")
                .orderStatus(OrderStatus.OPENED)
                .orderDataTime(LocalDateTime.now())
                .member(member)
                .build();

        entityManager.persist(order);

        transaction1.commit();

        entityManager.clear();
        Order entity = entityManager.find(Order.class, order.getId());

        log.info("{}", entity.getMember().getNickName());
        log.info("{}", entity.getMember().getOrders().size());
        log.info("{}", order.getMember().getOrders().size());
    }
}
