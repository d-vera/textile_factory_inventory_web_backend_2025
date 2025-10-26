package com.textile.inventory.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class ImageUploadService {
    
    @Value("\${file.upload-dir:uploads/images}")
    private lateinit var uploadDir: String
    
    private val allowedExtensions = setOf("jpg", "jpeg", "png", "gif", "webp")
    private val maxFileSize = 10 * 1024 * 1024 // 10MB
    
    fun uploadImage(file: MultipartFile): String {
        // Validate file
        validateFile(file)
        
        // Generate unique filename
        val filename = generateUniqueFilename(file.originalFilename ?: "image")
        
        // Create upload directory if it doesn't exist
        val uploadPath = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }
        
        // Save file
        val filePath = uploadPath.resolve(filename)
        try {
            Files.copy(file.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file: ${e.message}")
        }
        
        return filename
    }
    
    fun deleteImage(filename: String) {
        try {
            val filePath = Paths.get(uploadDir).resolve(filename)
            Files.deleteIfExists(filePath)
        } catch (e: IOException) {
            throw RuntimeException("Failed to delete file: ${e.message}")
        }
    }
    
    fun getImagePath(filename: String): Path {
        return Paths.get(uploadDir).resolve(filename)
    }
    
    private fun validateFile(file: MultipartFile) {
        // Check if file is empty
        if (file.isEmpty) {
            throw IllegalArgumentException("File is empty")
        }
        
        // Check file size
        if (file.size > maxFileSize) {
            throw IllegalArgumentException("File size exceeds maximum limit of 10MB")
        }
        
        // Check file extension
        val extension = getFileExtension(file.originalFilename ?: "")
        if (extension !in allowedExtensions) {
            throw IllegalArgumentException("Invalid file type. Allowed types: ${allowedExtensions.joinToString(", ")}")
        }
    }
    
    private fun generateUniqueFilename(originalFilename: String): String {
        val extension = getFileExtension(originalFilename)
        val uuid = UUID.randomUUID().toString()
        val timestamp = System.currentTimeMillis()
        return "${timestamp}_${uuid}.${extension}"
    }
    
    private fun getFileExtension(filename: String): String {
        val lastDotIndex = filename.lastIndexOf('.')
        return if (lastDotIndex > 0) {
            filename.substring(lastDotIndex + 1).lowercase()
        } else {
            ""
        }
    }
}
