package com.kdtpractice.young.domain;

import com.kdtpractice.young.domain.order.Member;
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

@Slf4j
@SpringBootTest
public class PersistenceContextTest {

    @Autowired
    CustomerRepository repository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void SetUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("EntityManager를 통한 Customer Entity 저장")
    void save() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = Customer.builder()
                .id(1L)
                .name("YoungSeo")
                .address("서울시 송파구")
                .postcode(12345)
                .phoneNumber("010-1234-4567")
                .emailAddress("test01@gmail.com")
                .build();

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();
    }

    @Test
    @DisplayName("DB에서 Customer Id로 조회")
    void findInDB() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = Customer.builder()
                .id(1L)
                .name("YoungSeo")
                .address("서울시 송파구")
                .postcode(12345)
                .phoneNumber("010-1234-4567")
                .emailAddress("test01@gmail.com")
                .build();

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        entityManager.detach(customer); // 영속 -> 준영속

        Customer selected = entityManager.find(Customer.class, 1L);
        log.info("{}", selected.getName());
    }

    @Test
    @DisplayName("1차 캐시 이용한 Customer 조회")
    void findInCache() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = Customer.builder()
                .id(1L)
                .name("YoungSeo")
                .address("서울시 송파구")
                .postcode(12345)
                .phoneNumber("010-1234-4567")
                .emailAddress("test01@gmail.com")
                .build();

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        Customer selected = entityManager.find(Customer.class, 1L);
        log.info("{}", selected.getName());

        assertThat(customer, samePropertyValuesAs(selected));
    }


    @Test
    @DisplayName("변경감지를 통한 DB Data 수정")
    void dirtyCheck() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = Customer.builder()
                .id(1L)
                .name("YoungSeo")
                .address("서울시 송파구")
                .postcode(12345)
                .phoneNumber("010-1234-4567")
                .emailAddress("test01@gmail.com")
                .build();

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        transaction.begin();
        customer.changeName("Beom");

        transaction.commit();
    }

    @Test
    @DisplayName("영속성을 이용한 변경감지로 DB Data 삭제")
    void delete() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = Customer.builder()
                .id(1L)
                .name("YoungSeo")
                .address("서울시 송파구")
                .postcode(12345)
                .phoneNumber("010-1234-4567")
                .emailAddress("test01@gmail.com")
                .build();

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        transaction.begin();

        entityManager.remove(customer);

        transaction.commit();
    }
}
