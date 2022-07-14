package com.api.Respository;

import com.api.Entity.Product;
import com.api.Entity.ProductPKID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductPKID>, PagingAndSortingRepository<Product,ProductPKID> {

    List<Product> findByMerchantId(Long Id);

    Optional<Product> findByProductId(String id);

    List<Product> findByProductCategory(String productCategory);

    List<Product> findByProductName(String name);

    Page<Product> findByProductCategory(String category, Pageable pageable);

    Page<Product> findByProductName(String name, Pageable pageable);

    Page<Product> findByMerchantId(Long id, Pageable pageable);
}
