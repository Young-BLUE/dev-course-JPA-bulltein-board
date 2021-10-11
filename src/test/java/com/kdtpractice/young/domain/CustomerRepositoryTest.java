package com.kdtpractice.young.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

@Slf4j
@SpringBootTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    EntityManagerFactory emf;

    @BeforeEach
    void SetUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("생성")
    void create() {
        Customer customer = new Customer(1L,"YoungSeo","서울시 송파구",123456,"010-1234-5678","test02@gmail.com");

        repository.save(customer);

        List<Customer> entity = repository.findAll();
        Customer personal = repository.findById(1L).get();
        log.info("{}", entity);
        log.info("{}번 Customer {}", personal.getId(), personal.getName());
    }

    @Test
    void find() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer1 = entityManager.find(Customer.class, 1L);
        Customer customer2 = entityManager.find(Customer.class, 2L);
        log.info("{}", customer1);
        log.info("{}", customer2);
    }


    @Test
    @DisplayName("EntityManager 를 통한 Customer")
    void CustomerEntitySave() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(2L,"YoungSeo","서울시 송파구",123456,"010-1234-5678","test02@gmail.com");

        entityManager.persist(customer);
        transaction.commit();

    }

    @Test
    @DisplayName("EntityManager 를 통한 Customer 조회")
    void CustomerEntityFind() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(2L,"YoungSeo","서울시 송파구",123456,"010-1234-5678","test02@gmail.com");

        entityManager.persist(customer);
        transaction.commit();

        entityManager.detach(customer);

        Customer selected = entityManager.find(Customer.class, 2L);
        log.info("{}", selected.getName());
    }

    @Test
    @DisplayName("1차 캐시 이용한 조회")
    void findInCache() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(2L,"YoungSeo","서울시 송파구",123456,"010-1234-5678","test02@gmail.com");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        Customer selected = entityManager.find(Customer.class, 2L);
        log.info("{}", selected.getName());
    }


    @Test
    @DisplayName("변경감지를 통한 수정")
    void dirtyCheck() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(2L,"YoungSeo","서울시 송파구",123456,"010-1234-5678","test02@gmail.com");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        transaction.begin();
        customer.changeName("Beom");

        transaction.commit();
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();

        Customer customer = new Customer(2L,"YoungSeo","서울시 송파구",123456,"010-1234-5678","test02@gmail.com");

        entityManager.persist(customer); // 비영속 -> 영속 (영속화)
        transaction.commit(); //entityManager.flush();

        transaction.begin();

        entityManager.remove(customer);

        transaction.commit();
    }
}
