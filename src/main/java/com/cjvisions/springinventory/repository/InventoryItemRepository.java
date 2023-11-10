package com.cjvisions.springinventory.repository;

import com.cjvisions.springinventory.domain.entities.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByName(String name);
    Optional<InventoryItem> findBySku(String sku);
    List<InventoryItem> findByLocation(String location);

}
