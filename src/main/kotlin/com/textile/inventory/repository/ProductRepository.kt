package com.textile.inventory.repository

import com.textile.inventory.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {
    fun findByNameContainingIgnoreCase(name: String): List<Product>
    fun findByColor(color: String): List<Product>
    fun findBySize(size: String): List<Product>
}
