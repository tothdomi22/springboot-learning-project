package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Product;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByCategoryId(Byte CategoryId);
}
