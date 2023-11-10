package com.cjvisions.springinventory.domain.dtos;

import java.time.Year;

public record InventoryItemDTO(
        String name,
        String sku,
        String description,
        Year yearOfManufacture,
        Long quantity,
        String location) {
}
