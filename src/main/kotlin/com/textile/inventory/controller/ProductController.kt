package com.textile.inventory.controller

import com.textile.inventory.dto.ProductRequest
import com.textile.inventory.dto.ProductResponse
import com.textile.inventory.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = ["*"])
@Tag(name = "Products", description = "Product management endpoints for textile fabric inventory")
@SecurityRequirement(name = "bearerAuth")
class ProductController(
    private val productService: ProductService
) {
    
    private val logger = LoggerFactory.getLogger(ProductController::class.java)
    
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get all products", description = "Retrieve all textile products (USER & ADMIN)")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    fun getAllProducts(): ResponseEntity<List<ProductResponse>> {
        logger.info("Fetching all products")
        val products = productService.getAllProducts()
        logger.info("Retrieved {} products", products.size)
        return ResponseEntity.ok(products)
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get product by ID", description = "Retrieve a specific product by its ID (USER & ADMIN)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product found"),
            ApiResponse(responseCode = "404", description = "Product not found")
        ]
    )
    fun getProductById(
        @Parameter(description = "Product ID") @PathVariable id: Long
    ): ResponseEntity<ProductResponse> {
        logger.info("Fetching product with id: {}", id)
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create product", description = "Create a new textile product (ADMIN only)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Product created successfully"),
            ApiResponse(responseCode = "400", description = "Invalid input"),
            ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
        ]
    )
    fun createProduct(@Valid @RequestBody request: ProductRequest): ResponseEntity<ProductResponse> {
        logger.info("Creating new product: {}", request.name)
        val product = productService.createProduct(request)
        logger.info("Product created successfully with id: {}", product.id)
        return ResponseEntity.status(HttpStatus.CREATED).body(product)
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update product", description = "Update an existing product (ADMIN only)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product updated successfully"),
            ApiResponse(responseCode = "404", description = "Product not found"),
            ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
        ]
    )
    fun updateProduct(
        @Parameter(description = "Product ID") @PathVariable id: Long,
        @Valid @RequestBody request: ProductRequest
    ): ResponseEntity<ProductResponse> {
        logger.info("Updating product with id: {}", id)
        val product = productService.updateProduct(id, request)
        logger.info("Product {} updated successfully", id)
        return ResponseEntity.ok(product)
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete product", description = "Delete a product by ID (ADMIN only)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            ApiResponse(responseCode = "404", description = "Product not found"),
            ApiResponse(responseCode = "403", description = "Access denied - Admin role required")
        ]
    )
    fun deleteProduct(
        @Parameter(description = "Product ID") @PathVariable id: Long
    ): ResponseEntity<Map<String, String>> {
        logger.info("Deleting product with id: {}", id)
        productService.deleteProduct(id)
        logger.info("Product {} deleted successfully", id)
        return ResponseEntity.ok(mapOf("message" to "Product deleted successfully"))
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Search products", description = "Search products by name (USER & ADMIN)")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    fun searchProducts(
        @Parameter(description = "Product name to search") @RequestParam name: String
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Searching products with name: {}", name)
        val products = productService.searchProductsByName(name)
        logger.info("Found {} products matching '{}'", products.size, name)
        return ResponseEntity.ok(products)
    }
    
    @GetMapping("/color/{color}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get products by color", description = "Filter products by color (USER & ADMIN)")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    fun getProductsByColor(
        @Parameter(description = "Product color") @PathVariable color: String
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Fetching products with color: {}", color)
        val products = productService.getProductsByColor(color)
        logger.info("Found {} products with color '{}'", products.size, color)
        return ResponseEntity.ok(products)
    }
    
    @GetMapping("/size/{size}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get products by size", description = "Filter products by size (USER & ADMIN)")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    fun getProductsBySize(
        @Parameter(description = "Product size") @PathVariable size: String
    ): ResponseEntity<List<ProductResponse>> {
        logger.info("Fetching products with size: {}", size)
        val products = productService.getProductsBySize(size)
        logger.info("Found {} products with size '{}'", products.size, size)
        return ResponseEntity.ok(products)
    }
}
