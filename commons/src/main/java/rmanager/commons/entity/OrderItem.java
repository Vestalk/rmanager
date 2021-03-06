package rmanager.commons.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "number")
    private Integer number;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "product_id", updatable = false, insertable = false)
    private Integer productId;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name = "order_id", updatable = false, insertable = false)
    private Long orderId;

}
