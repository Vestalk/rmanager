package rmanager.commons.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmanager.commons.entity.other.OrderStatus;
import rmanager.commons.entity.other.PaymentMethod;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private TelegramUser user;

    @Column(name = "user_id", updatable = false, nullable = false, insertable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "date_created")
    private Date dateCreate;

    @Column(name = "date_cooking")
    private Date dateCooking;

    @Column(name = "date_execute")
    private Date dateExecute;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_pethod")
    private PaymentMethod paymentMethod;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="order", cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

}
