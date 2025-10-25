package com.textile.inventory.service

import com.textile.inventory.dto.LoginRequest
import com.textile.inventory.dto.LoginResponse
import com.textile.inventory.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: CustomUserDetailsService
) {
    
    fun login(loginRequest: LoginRequest): LoginResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        )
        
        SecurityContextHolder.getContext().authentication = authentication
        
        val userDetails = userDetailsService.loadUserByUsername(loginRequest.username)
        val token = jwtTokenProvider.generateToken(userDetails)
        
        val role = userDetails.authorities.first().authority.removePrefix("ROLE_")
        
        return LoginResponse(
            token = token,
            username = userDetails.username,
            role = role
        )
    }
}
