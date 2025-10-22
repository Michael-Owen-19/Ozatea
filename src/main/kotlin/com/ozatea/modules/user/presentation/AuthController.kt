package com.ozatea.modules.user.presentation

import com.ozatea.core.response.ApiResponse
import com.ozatea.modules.user.application.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): ResponseEntity<ApiResponse<String>> {
        authService.register(username = req.username, password = req.password, email = req.email, name = req.name)
        return ResponseEntity.ok(ApiResponse.success(message = "User registered successfully"))
    }

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<ApiResponse<Map<String, String>>> {
        val tokens = authService.login(req.username, req.password)
        return ResponseEntity.ok(ApiResponse.success(tokens, "Login successful"))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody req: RefreshRequest): ResponseEntity<ApiResponse<Map<String, String>>> {
        val tokens = authService.refresh(req.refreshToken)
        return ResponseEntity.ok(ApiResponse.success(tokens, "Token refreshed"))
    }
}
