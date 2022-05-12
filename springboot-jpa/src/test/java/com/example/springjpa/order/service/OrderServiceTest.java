package com.example.springjpa.order.service;

import com.example.springjpa.domain.OrderRepository;
import com.example.springjpa.domain.OrderStatus;
import com.example.springjpa.item.ItemDto;
import com.example.springjpa.item.ItemType;
import com.example.springjpa.order.dto.MemberDto;
import com.example.springjpa.order.dto.OrderDto;
import com.example.springjpa.order.dto.OrderItemDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    private final MemberDto memberDto = new MemberDto(1L, "고객", "메롱메롱", 25, "행운동", "우와아");

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L).type(ItemType.CAR).price(2000000).stockQuantity(100).power(2000).build();

    private final OrderItemDto orderItemDto = new OrderItemDto(1L, 2000, 2, itemDto);
    private final OrderItemDto orderItemDto2 = new OrderItemDto(2L, 3000, 3, itemDto);

    private final OrderDto orderDtoData = new OrderDto(
            UUID.randomUUID().toString(),
            LocalDateTime.now(),
            OrderStatus.ACCEPTED,
            "기본 데이터",
            memberDto,
            List.of(orderItemDto)
    );

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setup(){
        orderService.save(orderDtoData);
    }

    @AfterEach
    void deleteAll(){
        orderRepository.deleteAll();
    }


    @Test
    void saveTest() {
        //Given
        OrderDto orderDto = new OrderDto(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                OrderStatus.ACCEPTED,
                "테스트 메모",
                memberDto,
                List.of(orderItemDto, orderItemDto2)
        );

        // When
        String uuid = orderService.save(orderDto);

        // Then
        assertThat(uuid).isEqualTo(orderDto.uuid());
    }

    @Test
    void findAllTest() {
        //Given
        // When
        Page<OrderDto> orderDtos = orderService.getOrders(PageRequest.of(0, 10));

        // Then
        assertThat(orderDtos.getContent()).hasSize(1);
        assertThat(orderDtos.getContent().get(0).uuid()).isEqualTo(orderDtoData.uuid());
    }

    @Test
    void findOneTest() {
        //Given
        // When
        OrderDto byId = orderService.getOrderById(orderDtoData.uuid());

        // Then
        assertThat(byId.uuid()).isEqualTo(orderDtoData.uuid());
    }
}