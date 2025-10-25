package com.textile.inventory.dto

import com.textile.inventory.model.Product
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val color: String,
    val size: String,
    val image: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun fromProduct(product: Product): ProductResponse {
            return ProductResponse(
                id = product.id,
                name = product.name,
                description = product.description,
                color = product.color,
                size = product.size,
                image = product.image,
                createdAt = product.createdAt,
                updatedAt = product.updatedAt
            )
        }
    }
}
