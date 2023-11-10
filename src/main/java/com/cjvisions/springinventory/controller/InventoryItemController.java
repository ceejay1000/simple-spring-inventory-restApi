package com.cjvisions.springinventory.controller;

import com.cjvisions.springinventory.domain.dtos.InventoryItemDTO;
import com.cjvisions.springinventory.domain.dtos.MessageDTO;
import com.cjvisions.springinventory.domain.entities.InventoryItem;
import com.cjvisions.springinventory.repository.InventoryItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/items")
public class InventoryItemController {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemController(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<MessageDTO> getInventoryItemByName(@PathVariable String name){
        var inventoryItem = this.inventoryItemRepository.findByName(name);
        if (inventoryItem.isPresent()) {
            return ResponseEntity.ok(new MessageDTO("Item found", HttpStatus.FOUND, inventoryItem.get()));
        }
        return ResponseEntity.ok(new MessageDTO("No item found", HttpStatus.NOT_FOUND, null));
    }

    @GetMapping("/{id}")
    public MessageDTO getInventoryItemById(@PathVariable Long id){
        var inventoryItem = this.inventoryItemRepository.findById(id);

        if (inventoryItem.isPresent())
             return new MessageDTO("Inventory item", HttpStatus.FOUND, inventoryItem);

        return new MessageDTO("Item with ID " + id + " does not exist", HttpStatus.NOT_FOUND, null);
    }

    @GetMapping("/")
    public MessageDTO getInventoryItems(){
        var inventoryItems = this.inventoryItemRepository.findAll();
        return new MessageDTO("Inventory items", HttpStatus.FOUND, inventoryItems);
    }

    @GetMapping("/sku/{sku}")
    public MessageDTO getInventoryItemBySku(@PathVariable String sku){
        var inventoryItem = this.inventoryItemRepository.findBySku(sku);
        return inventoryItem.map(item -> new MessageDTO("Inventory items", HttpStatus.FOUND, item))
                .orElseGet(() -> new MessageDTO("Inventory items", HttpStatus.NOT_FOUND, null));

    }

    @Transactional
    @PostMapping("/save-all")
    public MessageDTO addItems(@RequestBody List<InventoryItemDTO> inventoryItemDTOs){
       inventoryItemRepository.saveAll( mapToInventoryItems(inventoryItemDTOs));
       return new MessageDTO("Item saved successfully", HttpStatus.CREATED, null);
    }

    @PostMapping("/new-inventory-item")
    public MessageDTO addItem(@RequestBody InventoryItemDTO inventoryItemDTO){
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setName(inventoryItemDTO.name());
        inventoryItem.setSku(inventoryItemDTO.sku());
        inventoryItem.setDescription(inventoryItemDTO.description());
        inventoryItem.setLocation(inventoryItemDTO.location());
        inventoryItem.setQuantity(inventoryItemDTO.quantity());
        inventoryItem.setYearOfManufacture(inventoryItemDTO.yearOfManufacture());
        var savedItem = this.inventoryItemRepository.save(inventoryItem);
        return new MessageDTO("Item saved successfully", HttpStatus.CREATED, savedItem);
    }

    @Transactional
    @PutMapping("/update/{id}")
    public MessageDTO updateItem(@PathVariable Long id, @RequestBody InventoryItemDTO inventoryItemDTO){
         var existingItem = inventoryItemRepository.findById(id);

         if (existingItem.isEmpty())
             return new MessageDTO("Item not found", HttpStatus.NOT_FOUND, null);

         var concreteItem = existingItem.get();

         if (Objects.nonNull(inventoryItemDTO.name())){
             concreteItem.setName(inventoryItemDTO.name());
         }

         if (Objects.nonNull(inventoryItemDTO.sku())){
             concreteItem.setSku(inventoryItemDTO.sku());
         }

         if (Objects.nonNull(inventoryItemDTO.quantity())){
             concreteItem.setQuantity(inventoryItemDTO.quantity());
         }

         if (Objects.nonNull(inventoryItemDTO.location())){
             concreteItem.setLocation(inventoryItemDTO.location());
         }

         if (Objects.nonNull(inventoryItemDTO.description())){
             concreteItem.setDescription(inventoryItemDTO.description());
         }

         var updatedItem = inventoryItemRepository.save(concreteItem);

         return new MessageDTO("Item updated successfully", HttpStatus.CREATED, updatedItem);

    }

    @Transactional
    @DeleteMapping("/{id}/delete")
    public MessageDTO deleteInventoryItem(@PathVariable Long id){
        var existingInventoryItem = inventoryItemRepository.findById(id);
        if (!existingInventoryItem.isPresent())
            return new MessageDTO("Item not found", HttpStatus.NOT_FOUND, null);

        this.inventoryItemRepository.deleteById(id);
        return new MessageDTO("Item deleted successfully", HttpStatus.NO_CONTENT, null);
    }

    private List<InventoryItem> mapToInventoryItems(List<InventoryItemDTO> inventoryItemDTOS){
        return inventoryItemDTOS.stream().map(inventoryItemDTO -> {
            InventoryItem inventoryItem = new InventoryItem();
            inventoryItem.setName(inventoryItemDTO.name());
            inventoryItem.setSku(inventoryItemDTO.sku());
            inventoryItem.setDescription(inventoryItemDTO.description());
            inventoryItem.setLocation(inventoryItemDTO.location());
            inventoryItem.setQuantity(inventoryItemDTO.quantity());
            inventoryItem.setYearOfManufacture(inventoryItemDTO.yearOfManufacture());
            return inventoryItem;
        }).toList();
    }
}
