package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //상품저장
    public void save(Item item){
        if(item.getId() == null){
            em.persist(item);
        }else{
            em.persist(item); //강제 업데이트
        }
    }
    public Item findOne(Long id){
        return em.find(Item.class, id);
    }
    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}