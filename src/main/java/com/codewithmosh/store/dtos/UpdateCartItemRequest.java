package com.codewithmosh.store.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCartItemRequest {
  @NotNull(message = "Quanitity must be provided")
  @Min(value = 1, message = "Quanitity must be min 1")
  @Max(value = 1000, message = "Quantity must be max 1000")
  private Integer quantity;
}
