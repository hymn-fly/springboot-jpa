package com.example.springjpa.order.service;

import com.example.springjpa.domain.Order;
import com.example.springjpa.domain.OrderRepository;
import com.example.springjpa.order.converter.OrderConverter;
import com.example.springjpa.order.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public Page<OrderDto> getOrders(Pageable pageable){
        return orderRepository.findAll(pageable)
                .map(orderConverter::convertToOrderDto);
    }

    public OrderDto getOrderById(String uuid){
        return orderRepository.findById(uuid)
                .map(orderConverter::convertToOrderDto)
                .orElseThrow();
    }
}
