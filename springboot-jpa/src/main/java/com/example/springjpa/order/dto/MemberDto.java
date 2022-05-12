package com.example.springjpa.order.dto;

public record MemberDto (
        Long id,

        String name,

        String nickName,

        int age,

        String address,

        String description
){}
