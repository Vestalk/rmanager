package rmanager.commons.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "is_archived", nullable = false)
    private Boolean isArchived;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "img_id", nullable = false, referencedColumnName = "img_id")
    private Img img;

    @Column(name = "img_id", updatable = false, insertable = false)
    private Integer imgId;

    @ManyToOne
    @JoinColumn(name="category_id")
    private ProductCategory productCategory;

    @Column(name = "category_id", updatable = false, insertable = false)
    private Integer categoryId;

    @OneToMany(mappedBy="product")
    private Set<OrderItem> orderItems = new HashSet<>();
}
