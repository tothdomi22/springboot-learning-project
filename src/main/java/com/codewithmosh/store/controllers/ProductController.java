package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
  private ProductRepository productRepository;
  private ProductMapper productMapper;

  @GetMapping
  public List<ProductDto> getAllProducts(
      @RequestParam(name = "categoryId", required = false) Byte categoryId) {
    if (categoryId == null) {
      return productRepository.findAll().stream().map(productMapper::toDto).toList();
    } else {
      return productRepository.findByCategoryId(categoryId).stream()
          .map(productMapper::toDto)
          .toList();
    }
  }

  @GetMapping("${productId}")
  public ResponseEntity<ProductDto> getProduct(Long productId) {
    var product = productRepository.findById(productId).orElse(null);
    if (product == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(productMapper.toDto(product));
    }
  }
}
