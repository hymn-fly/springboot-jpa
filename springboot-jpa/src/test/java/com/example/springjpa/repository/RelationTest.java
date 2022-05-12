package com.example.springjpa.repository;

import com.example.springjpa.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
@Slf4j
public class RelationTest {

    @Autowired
    EntityManagerFactory emf;

    @Test
    void orderTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Member member = new Member();
        member.setName("geuno");
        member.setAddress("관악");
        member.setAge(30);
        member.setNickName("gilgil");
        em.persist(member);

        Order order = new Order();
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setMemo("주문 접수");
        order.setUuid(UUID.randomUUID().toString());
        order.setMember(member);
        em.persist(order);

        transaction.commit();
        em.clear();

        Order order1 = em.find(Order.class, order.getUuid());

        log.info("{}", order1.getMember().getNickName());
        log.info("{}", order1.getMember().getOrders());
        log.info("{}", order.getMember().getOrders());
    }

    @Test
    void orderItemTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Car item = new Car();
        item.setPrice(1500);
        item.setStockQuantity(100);
        item.setPower(100);
        em.persist(item);

        OrderItem orderItem = new OrderItem();
        orderItem.setPrice(item.getPrice());
        orderItem.setQuantity(3);
        orderItem.setItem(item);
        em.persist(orderItem);

        Order order = new Order();
        order.addOrderItem(orderItem);
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setMemo("주문 접수");
        order.setUuid(UUID.randomUUID().toString());
        em.persist(order);
        transaction.commit();
    }

    @Test
    void fetchTypeTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Member member = new Member();
        member.setName("geuno");
        member.setAddress("관악");
        member.setAge(30);
        member.setNickName("gilgil");
        em.persist(member);

        Order order = new Order();
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setMemo("주문 접수");
        order.setUuid(UUID.randomUUID().toString());
        order.setMember(member);
        em.persist(order);

        transaction.commit();
        em.clear();

        System.out.println(member.getOrders());

        Member findMember = em.find(Member.class, 1L);

        log.info("orders is loaded : {}", em.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(findMember.getOrders()));
        log.info("{}", findMember.getOrders());

        log.info("-------");
        log.info("{}" ,findMember.getOrders().get(0).getMemo());
        log.info("orders is loaded : {}", em.getEntityManagerFactory()
                .getPersistenceUnitUtil().isLoaded(findMember.getOrders()));
    }

    @Test
    void cascadeTest() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Member member = new Member();
        member.setName("geuno");
        member.setAddress("관악");
        member.setAge(30);
        member.setNickName("gilgil");

        Order order = new Order();
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setMemo("주문 접수");
        order.setUuid(UUID.randomUUID().toString());
        order.setMember(member);

        // em.persist(order); 만약 Order 쪽에서 cascade 해줬으면 order를 저장
        em.persist(member); // member 쪽에서 cascade 옵션 줬으면 member를 저장

        transaction.commit();
    }

    @Test
    void 고아객체_테스트() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();
        Member member = new Member();
        member.setName("geuno");
        member.setAddress("관악");
        member.setAge(30);
        member.setNickName("gilgil");

        Order order = new Order();
        order.setOrderDatetime(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.ACCEPTED);
        order.setMemo("주문 접수");
        order.setUuid(UUID.randomUUID().toString());
        order.setMember(member);

        // em.persist(order); 만약 Order 쪽에서 cascade 해줬으면 order를 저장
        em.persist(member); // member 쪽에서 cascade 옵션 줬으면 member를 저장

        transaction.commit();

        transaction.begin();
        Member member1 = em.find(Member.class, member.getId());
        member1.getOrders().remove(0);
        transaction.commit();
    }
}
