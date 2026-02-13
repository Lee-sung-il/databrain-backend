package com.sungil.springboot.databrainbackend.api.repository;

import com.sungil.springboot.databrainbackend.api.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> {

}