package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.OrderItem;
import lombok.Getter;

@Getter // 이런씩으로 아예 완전히 dto로 바꾸어야하낟
public class OrderItemDto {
    //이렇게 하면 외부에서는 orderdto에서 orderitemdto를 랩핑해서 나가기때문에 문제가 해결됨
    private  String itemName; // 상품명
    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
}
