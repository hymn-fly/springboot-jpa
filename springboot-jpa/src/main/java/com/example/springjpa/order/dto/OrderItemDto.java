package com.example.springjpa.order.dto;

import com.example.springjpa.domain.Item;
import com.example.springjpa.item.ItemDto;

public record OrderItemDto(Long id, int price, int quantity, ItemDto itemDto) {}

