package jpabook.jpashop.repository.order.query;

import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderFlatDto {
    //db에서 한번에 가져오기
    private Long orderId;
    private String username;
    private LocalDateTime orderDate; //주문시간
    private Address address;
    private OrderStatus orderStatus;
    private String itemName;//상품 명
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    public OrderFlatDto(Long orderId, String username, LocalDateTime orderDate, Address address, OrderStatus orderStatus, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.username = username;
        this.orderDate = orderDate;
        this.address = address;
        this.orderStatus = orderStatus;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
