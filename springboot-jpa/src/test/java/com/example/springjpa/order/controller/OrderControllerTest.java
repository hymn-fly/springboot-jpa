package com.example.springjpa.order.controller;

import com.example.springjpa.domain.OrderRepository;
import com.example.springjpa.domain.OrderStatus;
import com.example.springjpa.item.ItemDto;
import com.example.springjpa.item.ItemType;
import com.example.springjpa.order.dto.MemberDto;
import com.example.springjpa.order.dto.OrderDto;
import com.example.springjpa.order.dto.OrderItemDto;
import com.example.springjpa.order.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTest {

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

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        orderService.save(orderDtoData);
    }

    @AfterEach
    void deleteAll(){
        orderRepository.deleteAll();
    }


    @Test
    void createOrderTest() throws Exception {
        mvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDtoData)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getAllTest() throws Exception{
        mvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getOneTest() throws Exception{
        mvc.perform(get("/orders/{uuid}", orderDtoData.uuid()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}