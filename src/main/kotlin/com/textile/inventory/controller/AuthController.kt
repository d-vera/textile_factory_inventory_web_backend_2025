package com.textile.inventory.controller

import com.textile.inventory.dto.LoginRequest
import com.textile.inventory.dto.LoginResponse
import com.textile.inventory.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"])
@Tag(name = "Authentication", description = "Authentication endpoints for user login")
class AuthController(
    private val authService: AuthService
) {
    
    private val logger = LoggerFactory.getLogger(AuthController::class.java)
    
    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticate user and receive JWT token for subsequent API calls"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Login successful",
                content = [Content(schema = Schema(implementation = LoginResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid credentials"
            )
        ]
    )
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<LoginResponse> {
        logger.info("Login attempt for user: {}", loginRequest.username)
        val response = authService.login(loginRequest)
        logger.info("User {} logged in successfully with role {}", loginRequest.username, response.role)
        return ResponseEntity.ok(response)
    }
}
