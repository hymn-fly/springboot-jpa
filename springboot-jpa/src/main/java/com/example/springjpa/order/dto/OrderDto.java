package com.example.springjpa.order.dto;

import com.example.springjpa.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        String uuid,

        LocalDateTime orderDatetime,

        OrderStatus orderStatus,

        String memo,

        MemberDto memberDto,

        List<OrderItemDto> orderItemDtos
) {}
