package jpabook.jpashop.repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대1000건
        return query.getResultList();
    }


    public List findAllWithMemberDelivery() { //fetchjoin  전략
        return em.createQuery(
                        "select o from Order o " +
                                "join fetch o.member m " +
                                "join fetch o.delivery d", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();


    }
    public List findAllWithMemberDelivery(int offset, int limit) { //fetchjoin  전략
        return em.createQuery(
                        "select o from Order o " +
                                "join fetch o.member m " +
                                "join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();


    }


    public List<Order> findAllWithItem() {
        return em.createQuery("select distinct o from Order o" +
                " join fetch o.member m " + //order에서 member fetch join
                " join fetch o.delivery d" + //member에서 delievery  fetch join 여기까지 페치 조인한다,
                " join fetch o.orderItems oi"+
                " join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
        /*
        order와 orderitems를 조인하면 1랑2로 조인하면 중복으로 데이터가 2개씩 생긴다.
        jpa에서 데이터를 가져올때 2배가 되어버린다, 의도와 다른 조인이 만들어진다.->
        distinct 로 중복을 제거한다, 그런데 문제는 전부가 같아야 중복이 제거할수 있기때문에
        db쿼리를 뽑을때는 안될수 있다.
        jpa에서 자체적으로 order가 같은 아이디 값을 중복을제거할수 있다.
        1. db에 distinct를 날린다,
        2. root에 컬렉션에 중복이되면 걸러서 담는다.
        이러면 쿼리를 한번나간다.
        * */
    }
}
