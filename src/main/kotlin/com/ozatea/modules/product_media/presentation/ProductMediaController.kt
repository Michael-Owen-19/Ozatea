package com.ozatea.modules.product_media.presentation

import com.ozatea.modules.media.application.MediaService
import com.ozatea.modules.product.application.ProductService
import com.ozatea.modules.product_media.application.ProductMediaService
import com.ozatea.modules.product_media.domain.ProductMedia
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product-medias")
class ProductMediaController(
    private val productMediaService: ProductMediaService,
    private val mediaService: MediaService,
    private val productService: ProductService
) {

    @PostMapping(consumes = ["multipart/form-data"])
    fun create(@ModelAttribute request: ProductMediaRequest) : ResponseEntity<List<ProductMediaResponse>>{
        val productResponse = productService.findById(request.productId)
        val productMediaList = request.media.map {
            val media = mediaService.upload(file = it.file, altText = it.altText)
            productMediaService.create(
                ProductMedia(
                    product = productResponse.toProduct(),
                    media = media,
                    isThumbnail = it.isThumbnail,
                    orderIndex = it.orderIndex?:0
                )
            )

        }
        return ResponseEntity.ok(productMediaList)
    }
}