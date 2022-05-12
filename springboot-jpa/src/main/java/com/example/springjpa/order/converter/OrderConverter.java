package com.example.springjpa.order.converter;

import com.example.springjpa.domain.*;
import com.example.springjpa.item.ItemDto;
import com.example.springjpa.item.ItemType;
import com.example.springjpa.order.dto.MemberDto;
import com.example.springjpa.order.dto.OrderDto;
import com.example.springjpa.order.dto.OrderItemDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {
    public OrderDto convertToOrderDto(Order order){
        return new OrderDto(
                order.getUuid(),
                order.getOrderDatetime(),
                order.getOrderStatus(),
                order.getMemo(),
                this.converToMemberDto(order.getMember()),
                this.converToOrderItemDtoList(order.getOrderItems())
        );
    }

    public MemberDto converToMemberDto(Member member){
        return new MemberDto(
                member.getId(),
                member.getName(),
                member.getNickName(),
                member.getAge(),
                member.getAddress(),
                member.getDescription()
        );
    }

    public Member convertToMember(MemberDto memberDto){
        Member member = new Member();
        member.setId(memberDto.id());
        member.setName(memberDto.name());
        member.setNickName(memberDto.nickName());
        member.setAge(memberDto.age());
        member.setAddress(memberDto.address());
        member.setDescription(memberDto.description());

        return member;
    }

    public List<OrderItemDto> converToOrderItemDtoList(List<OrderItem> orderItems){
        return orderItems.stream().map(
                (item) -> new OrderItemDto(
                        item.getId(),
                        item.getPrice(),
                        item.getQuantity(),
                        convertItemDto(item.getItem())
                ))
                .collect(Collectors.toList());
    }

    public ItemDto convertItemDto(Item item){
        if(item instanceof Car){
            return ItemDto.builder()
                    .price(item.getPrice())
                    .type(ItemType.CAR)
                    .stockQuantity(item.getStockQuantity())
                    .power(((Car) item).getPower())
                    .build();
        }
        if(item instanceof Furniture){
            return ItemDto.builder()
                    .price(item.getPrice())
                    .type(ItemType.FURNITURE)
                    .stockQuantity(item.getStockQuantity())
                    .height(((Furniture) item).getHeight())
                    .width(((Furniture) item).getWidth())
                    .build();
        }
        if(item instanceof Food){
            return ItemDto.builder()
                    .price(item.getPrice())
                    .type(ItemType.FOOD)
                    .stockQuantity(item.getStockQuantity())
                    .chef(((Food) item).getChef())
                    .build();
        }
        throw new IllegalArgumentException("잘못된 아이템 타입입니다");
    }

    public List<OrderItem> convertOrderItem(List<OrderItemDto> orderItemDtos, Order order){
        return orderItemDtos.stream().map(
                (dto) -> {
                    OrderItem item = new OrderItem();
                    item.setQuantity(dto.quantity());
                    item.setId(dto.id());
                    item.setPrice(dto.price());
                    item.setItem(convertItem(dto.itemDto()));
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList());
    }

    public Item convertItem(ItemDto itemDto){
        if(itemDto.getType() == ItemType.CAR){
            Car car = new Car();
            car.setId(itemDto.getId());
            car.setPrice(itemDto.getPrice());
            car.setStockQuantity(itemDto.getStockQuantity());
            car.setPower(itemDto.getPower());
            return car;
        }

        if(itemDto.getType() == ItemType.FOOD){
            Food food = new Food();
            food.setId(itemDto.getId());
            food.setPrice(itemDto.getPrice());
            food.setStockQuantity(itemDto.getStockQuantity());
            food.setChef(itemDto.getChef());
            return food;
        }

        if(itemDto.getType() == ItemType.FURNITURE){
            Furniture furniture = new Furniture();
            furniture.setId(itemDto.getId());
            furniture.setPrice(itemDto.getPrice());
            furniture.setStockQuantity(itemDto.getStockQuantity());
            furniture.setHeight(itemDto.getHeight());
            furniture.setWidth(itemDto.getWidth());
        }

        throw new IllegalArgumentException("잘못된 ItemDto 타입입니다");
    }


    public Order converToOrder(OrderDto orderDto){
        Order ret = new Order();
        ret.setUuid(orderDto.uuid());
        ret.setOrderDatetime(orderDto.orderDatetime());
        ret.setOrderStatus(orderDto.orderStatus());
        ret.setMemo(orderDto.memo());
        ret.setMember(convertToMember(orderDto.memberDto()));
        ret.setOrderItems(this.convertOrderItem(orderDto.orderItemDtos(), ret));
        return ret;
    }
}
