package rmanager.commons.repository.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import rmanager.commons.entity.Product;
import rmanager.commons.repository.filter.ProductFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class ProductListRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Product> getByFilter(ProductFilter filter) {
        String availableFilter = " ";
        if (filter.getIsAvailable() != null) {
            availableFilter = " and p.is_available = :isAvailable ";
        }
        String categoryFilter = " ";
        if (filter.getProductCategoryId() != null) {
            categoryFilter = " and p.category_id = :categoryId ";
        }
        Query query = entityManager.createNativeQuery("select * from products as p where p.product_id is not null " +
                availableFilter +
                categoryFilter +
                " and p.is_archived is not true order by p.product_id desc ", Product.class);

        if (filter.getIsAvailable() != null) {
            query.setParameter("isAvailable", filter.getIsAvailable());
        }
        if (filter.getProductCategoryId() != null) {
            query.setParameter("categoryId", filter.getProductCategoryId());
        }
        return query.getResultList();
    }

    public Page<List<Product>> getByFilter(ProductFilter filter, PageRequest pageable) {
        String availableFilter = " ";
        if (filter.getIsAvailable() != null) {
            availableFilter = " and p.is_available = :isAvailable ";
        }
        String categoryFilter = " ";
        if (filter.getProductCategoryId() != null) {
            categoryFilter = " and p.category_id = :categoryId ";
        }
        Query query = entityManager.createNativeQuery("select * from products as p where p.product_id is not null " +
                availableFilter +
                categoryFilter +
                " and p.is_archived is not true order by p.product_id desc ", Product.class);

        if (filter.getIsAvailable() != null) {
            query.setParameter("isAvailable", filter.getIsAvailable());
        }
        if (filter.getProductCategoryId() != null) {
            query.setParameter("categoryId", filter.getProductCategoryId());
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList());
    }

    public Long countByFilter(ProductFilter filter) {
        String availableFilter = " ";
        if (filter.getIsAvailable() != null) {
            availableFilter = " and p.is_available = :isAvailable ";
        }
        String categoryFilter = " ";
        if (filter.getProductCategoryId() != null) {
            categoryFilter = " and p.category_id = :categoryId ";
        }
        Query query = entityManager.createNativeQuery("select count(p.product_id) from products as p where p.product_id is not null " +
                availableFilter +
                categoryFilter +
                " and p.is_archived is not true ");

        if (filter.getIsAvailable() != null) {
            query.setParameter("isAvailable", filter.getIsAvailable());
        }
        if (filter.getProductCategoryId() != null) {
            query.setParameter("categoryId", filter.getProductCategoryId());
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }

}
