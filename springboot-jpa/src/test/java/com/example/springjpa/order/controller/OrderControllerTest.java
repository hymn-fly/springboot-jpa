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
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
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
                .andDo(print())
                .andDo(document("order-save",
                        requestFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID"),
                                fieldWithPath("orderDatetime").type(JsonFieldType.STRING).description("orderDatetime"),
                                fieldWithPath("orderStatus").type(JsonFieldType.STRING).description("orderStatus"),
                                fieldWithPath("memo").type(JsonFieldType.STRING).description("memo"),
                                fieldWithPath("memberDto").type(JsonFieldType.OBJECT).description("memberDto"),
                                fieldWithPath("memberDto.id").type(JsonFieldType.NUMBER).description("memberDto.id"),
                                fieldWithPath("memberDto.name").type(JsonFieldType.STRING).description("memberDto.name"),
                                fieldWithPath("memberDto.nickName").type(JsonFieldType.STRING).description("memberDto.nickName"),
                                fieldWithPath("memberDto.age").type(JsonFieldType.NUMBER).description("memberDto.age"),
                                fieldWithPath("memberDto.address").type(JsonFieldType.STRING).description("memberDto.address"),
                                fieldWithPath("memberDto.description").type(JsonFieldType.STRING).description("memberDto.description"),
                                fieldWithPath("orderItemDtos").type(JsonFieldType.ARRAY).description("orderItemDtos"),
                                fieldWithPath("orderItemDtos[].id").type(JsonFieldType.NUMBER).description("orderItemDtos[].id"),
                                fieldWithPath("orderItemDtos[].quantity").type(JsonFieldType.NUMBER).description("orderItemDtos[].quantity"),
                                fieldWithPath("orderItemDtos[].price").type(JsonFieldType.NUMBER).description("orderItemDtos[].price"),
                                fieldWithPath("orderItemDtos[].itemDto").type(JsonFieldType.OBJECT).description("orderItemDtos[].itemDto"),
                                fieldWithPath("orderItemDtos[].itemDto.id").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDto.id"),
                                fieldWithPath("orderItemDtos[].itemDto.type").type(JsonFieldType.STRING).description("orderItemDtos[].itemDto.type"),
                                fieldWithPath("orderItemDtos[].itemDto.price").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDto.price"),
                                fieldWithPath("orderItemDtos[].itemDto.stockQuantity").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDto.stockQuantity"),
                                fieldWithPath("orderItemDtos[].itemDto.power").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDto.power"),
                                fieldWithPath("orderItemDtos[].itemDto.chef").type(JsonFieldType.NULL).description("orderItemDtos[].itemDto.chef"),
                                fieldWithPath("orderItemDtos[].itemDto.width").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDto.width"),
                                fieldWithPath("orderItemDtos[].itemDto.height").type(JsonFieldType.NUMBER).description("orderItemDtos[].itemDto.height")
                        ),
                        responseFields(
                                fieldWithPath("statusCode").type(JsonFieldType.NUMBER).description("상태코드"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("데이터"),
                                fieldWithPath("serverDateTime").type(JsonFieldType.STRING).description("서버시간")
                        )));
    }

    @Test
    void getAllTest() throws Exception{
        mvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getOneTest() throws Exception{
        mvc.perform(get("/orders/{uuid}", orderDtoData.uuid()))
                .andExpect(status().isOk())
                .andDo(print());

    }
}