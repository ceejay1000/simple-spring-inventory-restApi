package com.cjvisions.springinventory.mapper;

import com.cjvisions.springinventory.domain.dtos.InventoryItemDTO;
import com.cjvisions.springinventory.domain.entities.InventoryItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InventoryMapper {

    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);
    InventoryItemDTO inventoryToInventoryDTO(InventoryItem inventoryItem);
    InventoryItem inventoryDTOToInventory(InventoryItemDTO inventoryItemDTO);
}
