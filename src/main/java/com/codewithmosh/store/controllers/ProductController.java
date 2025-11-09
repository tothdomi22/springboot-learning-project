package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
  private final UserRepository userRepository;
  private final CategoryRepository categoryRepository;
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

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId) {
    var product = productRepository.findById(productId).orElse(null);
    if (product == null) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(productMapper.toDto(product));
    }
  }

  @PostMapping
  public ResponseEntity<ProductDto> createProduct(
      @RequestBody ProductDto request, UriComponentsBuilder uriComponentsBuilder) {
    var product = productMapper.toEntity(request);

    productRepository.save(product);

    var productDto = productMapper.toDto(product);
    var uri =
        uriComponentsBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> updateProduct(
      @PathVariable Long id, @RequestBody ProductDto request) {
    var product = productRepository.findById(id).orElse(null);
    var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
    if (product == null || category == null) {
      return ResponseEntity.notFound().build();
    }
    productMapper.update(request, product);
    productRepository.save(product);
    product.setId(product.getId());
    product.setCategory(category);
    return ResponseEntity.ok(request);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    var product = productRepository.findById(id).orElse(null);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }
    productRepository.delete(product);
    return ResponseEntity.noContent().build();
  }
}
