package com.spring.basicapi.repository;

import com.spring.basicapi.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory,Long> {
}
