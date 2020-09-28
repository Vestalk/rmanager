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
@Table(name = "product_categories")
public class ProductCategory {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productCategoryId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy="productCategory")
    private Set<Product> products = new HashSet<>();

}
