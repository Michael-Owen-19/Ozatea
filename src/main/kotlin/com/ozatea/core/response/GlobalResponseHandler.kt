package com.ozatea.core.response

import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class GlobalResponseHandler : ResponseBodyAdvice<Any>  {
    override fun supports(
        returnType: MethodParameter,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        // Apply wrapper to all responses, except when already ApiResponse
        return returnType.parameterType != ApiResponse::class.java
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return when (body) {
            is ApiResponse<*> -> body // already wrapped
            is String -> body // avoid issues with StringHttpMessageConverter
            else -> ApiResponse(success = true, message = "Success", data = body)
        }
    }
}