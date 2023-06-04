package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderDto;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
        * V1. 엔티티 직접 노출
        * - 엔티티가 변하면 API 스펙이 변한다.
        * - 트랜잭션 안에서 지연 로딩 필요
        * - 양방향 연관관계 문제
        *
        * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
        * - 트랜잭션 안에서 지연 로딩 필요
        * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용O)
        * - 페이징 시에는 N 부분을 포기해야함(대신에 batch fetch size? 옵션 주면 N -> 1 쿼리로 변경
        가능)
        *
        * V4. JPA에서 DTO로 바로 조회, 컬렉션 N 조회 (1 + N Query)
        * - 페이징 가능
        * V5. JPA에서 DTO로 바로 조회, 컬렉션 1 조회 최적화 버전 (1 + 1 Query)
        * - 페이징 가능
        * V6. JPA에서 DTO로 바로 조회, 플랫 데이터(1Query) (1 Query)
        * - 페이징 불가능...
        *
        */
@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders") //api entity를 노출하지 말것
    public List<Order> orderV1(){ // collection 노출
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for(Order order:all){
            order.getMember().getUsername();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems(); // 아이템의 정보를  강제 초기화 하이버네이트 레이즈 로딩을 데이터를 안뿌림
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }
    /* DTO로 변환*/
    /* ordderitems가 안나오는 이유는  Entity여서 안나옴
    완전히 entity에 의존을 없애야한다,
    * */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
    }
    private final OrderQueryService orderQueryService;

    @GetMapping("/api/v3/orders") // collection fetch 조인으로하겠다
    public List<OrderDto> ordersV3() {
        return  orderQueryService.ordersV3();
//        List<Order> orders = orderRepository.findAllWithItem();
//        List<OrderDto> result = orders.stream()
//                .map(o -> new OrderDto(o))
//                .collect(toList());
//        return result;
    }
    @GetMapping("/api/v3.1/orders") //페이징하면서 페치조인함
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return result;
    }
    @GetMapping("/api/v4/orders") //페이징하면서 페치조인함
    public List<OrderQueryDto> ordersv4(){
       return orderQueryRepository.findOrderQueryDtos();
    }
    @GetMapping("/api/v5/orders") //페이징하면서 페치조인함
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }



}
