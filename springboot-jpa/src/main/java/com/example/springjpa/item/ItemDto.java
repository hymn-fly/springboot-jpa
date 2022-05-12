package com.example.springjpa.item;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ItemDto {
    private Long id;

    private ItemType type;

    private int price;

    private int stockQuantity;

    private long power;

    private String chef;

    private long width;

    private long height;
}
