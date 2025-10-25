package com.textile.inventory.dto

import jakarta.validation.constraints.NotBlank

data class ProductRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    val description: String? = null,
    
    @field:NotBlank(message = "Color is required")
    val color: String,
    
    @field:NotBlank(message = "Size is required")
    val size: String,
    
    val image: String? = null
)
