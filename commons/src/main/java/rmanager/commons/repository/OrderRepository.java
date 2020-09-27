package rmanager.commons.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmanager.commons.entity.Order;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Order findById(Long id) {
        Query query = entityManager.createNativeQuery("select * from orders as o where o.order_id = :orderId");
        query.setParameter("orderId", id);
        List<Order> orderList = query.getResultList();
        if (orderList.isEmpty()) {
            return null;
        } else {
            return orderList.get(0);
        }
    }

    @Transactional
    public void save(Order order) {
        entityManager.persist(order);
        entityManager.flush();
    }

    @Transactional
    public void update(Order order) {
        entityManager.merge(order);
        entityManager.flush();
    }

    @Transactional
    public void delete(Order order) {
        order = findById(order.getOrderId());
        entityManager.remove(order);
        entityManager.flush();
    }

}
