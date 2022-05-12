package com.example.springjpa.order.service;

import com.example.springjpa.domain.Order;
import com.example.springjpa.domain.OrderRepository;
import com.example.springjpa.order.converter.OrderConverter;
import com.example.springjpa.order.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderConverter orderConverter;

    public OrderService(OrderRepository orderRepository, OrderConverter orderConverter) {
        this.orderRepository = orderRepository;
        this.orderConverter = orderConverter;
    }

    public String save(OrderDto orderDto){
        Order order = orderConverter.converToOrder(orderDto);

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getUuid();
    }

    public List<OrderDto> findAll(){
        return orderRepository.findAll()
                .stream()
                .map(orderConverter::convertToOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto findById(String uuid){
        return orderRepository.findById(uuid)
                .map(orderConverter::convertToOrderDto)
                .orElseThrow();
    }
}
