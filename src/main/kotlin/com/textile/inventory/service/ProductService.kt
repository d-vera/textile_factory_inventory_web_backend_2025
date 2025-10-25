package com.textile.inventory.service

import com.textile.inventory.dto.ProductRequest
import com.textile.inventory.dto.ProductResponse
import com.textile.inventory.model.Product
import com.textile.inventory.repository.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    
    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { ProductResponse.fromProduct(it) }
    }
    
    fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Product not found with id: $id") }
        return ProductResponse.fromProduct(product)
    }
    
    fun createProduct(request: ProductRequest): ProductResponse {
        val product = Product(
            name = request.name,
            description = request.description,
            color = request.color,
            size = request.size,
            image = request.image
        )
        val savedProduct = productRepository.save(product)
        return ProductResponse.fromProduct(savedProduct)
    }
    
    fun updateProduct(id: Long, request: ProductRequest): ProductResponse {
        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Product not found with id: $id") }
        
        product.name = request.name
        product.description = request.description
        product.color = request.color
        product.size = request.size
        product.image = request.image
        product.updatedAt = LocalDateTime.now()
        
        val updatedProduct = productRepository.save(product)
        return ProductResponse.fromProduct(updatedProduct)
    }
    
    fun deleteProduct(id: Long) {
        if (!productRepository.existsById(id)) {
            throw NoSuchElementException("Product not found with id: $id")
        }
        productRepository.deleteById(id)
    }
    
    fun searchProductsByName(name: String): List<ProductResponse> {
        return productRepository.findByNameContainingIgnoreCase(name)
            .map { ProductResponse.fromProduct(it) }
    }
    
    fun getProductsByColor(color: String): List<ProductResponse> {
        return productRepository.findByColor(color)
            .map { ProductResponse.fromProduct(it) }
    }
    
    fun getProductsBySize(size: String): List<ProductResponse> {
        return productRepository.findBySize(size)
            .map { ProductResponse.fromProduct(it) }
    }
}
