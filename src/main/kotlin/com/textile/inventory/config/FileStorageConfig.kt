package com.textile.inventory.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class FileStorageConfig : WebMvcConfigurer {
    
    @Value("\${file.upload-dir:uploads/images}")
    private lateinit var uploadDir: String
    
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/uploads/images/**")
            .addResourceLocations("file:${uploadDir}/")
    }
}
