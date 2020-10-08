package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmanager.commons.entity.Order;
import rmanager.commons.entity.other.OrderStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select * from orders as o where o.user_id = ?1 and o.order_status = ?2", nativeQuery = true)
    List<Order> getOrders(Long userId, String orderStatus);

}
