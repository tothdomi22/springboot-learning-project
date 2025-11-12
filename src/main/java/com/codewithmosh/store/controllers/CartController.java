package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCardRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.UpdateCartItemRequest;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
public class CartController {
  private final CartService cartService;

  @PostMapping
  public ResponseEntity<CartDto> createCart() {
    CartDto cartDto = cartService.createCart();
    return new ResponseEntity<>(cartDto, HttpStatus.CREATED);
  }

  @PostMapping("/{cartId}/items")
  public ResponseEntity<CartItemDto> addToCart(
      @PathVariable UUID cartId, @Valid @RequestBody AddItemToCardRequest request) {
    CartItemDto cartItemDto = cartService.addToCart(cartId, request.getProductId());
    return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
  }

  @GetMapping("/{cartId}")
  public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
    CartDto cartDto = cartService.getCart(cartId);
    return ResponseEntity.status(HttpStatus.OK).body(cartDto);
  }

  @PutMapping("/{cartId}/{productId}")
  public ResponseEntity<?> updateItem(
      @PathVariable UUID cartId,
      @PathVariable Long productId,
      @Valid @RequestBody UpdateCartItemRequest request) {
    CartDto cartDto = cartService.updateItem(cartId, productId, request.getQuantity());
    return ResponseEntity.status(HttpStatus.OK).body(cartDto);
  }

  @DeleteMapping("/{cartId}/items/{productId}")
  public ResponseEntity<?> deleteItem(@PathVariable UUID cartId, @PathVariable Long productId) {
    cartService.deleteItem(cartId, productId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{cartId}/items")
  public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
    cartService.clearCart(cartId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @ExceptionHandler(CartNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCartNotFound() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleProductNotFound() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("error", "Product not found in the cart"));
  }
}
