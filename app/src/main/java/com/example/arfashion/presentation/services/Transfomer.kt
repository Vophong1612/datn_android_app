package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.CategoriesResponse
import com.example.arfashion.presentation.app.models.product.Comments
import com.example.arfashion.presentation.app.models.product.ProductResponse
import com.example.arfashion.presentation.app.models.product.RelatedProductResponse
import com.example.arfashion.presentation.data.model.*
import java.text.SimpleDateFormat
import java.util.*

val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

fun GetCarouselResponse.toCarousel(): Carousel =
    Carousel(name, id, image)

fun ProductResponse.toProduct(): Product {
    return Product(
        id,
        name,
        sizes.map { size ->
            size.name
        },
        comments.map { comment ->
            comment.toComment()
        },
        images.map { image ->
            image.url
        },
        images.map { thumbnail ->
            thumbnail.mobile
        },
        price,
        description,
        details.toString(),
        tags.map { tag ->
            tag.name
        },
        total,
        star,
        priceSale
    )
}

fun Comments.toComment(): Comment =
    Comment(
        content,
        star,
        like,
        _id,
        Profile(
            id = created_by._id,
            name = created_by.name,
            avatar = created_by.avatar
        ),
        responses.map {
            it.toComment()
        },
//        (parser.parse(created_at)?: Date())
    )

fun RelatedProductResponse.toProduct(): Product =
    Product(id = id,
        name = name,
        prices = price,
        priceSale = if (priceSale.isNotEmpty()) {
             price - priceSale[0].discount
        } else price,
        images = images.map {
            it.url
        },
        tag = tags.map { tag ->
            tag.name
        }
    )

fun CategoriesResponse.toCategory() : Category =
    Category(name = name,
        tag = tags.map { it.name },
        id = _id,
        imageCategory = listImage.img_category.mobile,
        imageBanner = listImage.img_category.mobile
    )