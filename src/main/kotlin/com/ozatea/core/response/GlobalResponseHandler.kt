package com.ozatea.core.response

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class GlobalResponseHandler : ResponseBodyAdvice<Any> {

    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        // Skip wrapping for specific response types
        val type = returnType.parameterType
        return type != ApiResponse::class.java &&
                type != String::class.java &&
                type != ByteArray::class.java &&
                type != ResponseEntity::class.java
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        // Don’t wrap if it’s already wrapped, raw bytes, or a ResponseEntity
        return when (body) {
            is ApiResponse<*> -> body
            is ByteArray -> body
            is String -> body
            is ResponseEntity<*> -> body
            null -> ApiResponse(success = true, message = "No content", data = null)
            else -> ApiResponse(success = true, message = "Success", data = body)
        }
    }
}
