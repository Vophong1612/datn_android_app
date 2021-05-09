package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.Comments
import com.example.arfashion.presentation.app.models.product.ProductResponse
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
            thumbnail.thumbMobile
        },
        if (sales.isNotEmpty()) sales[0].discount
        else 0,
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
        (parser.parse(created_at)?: Date())
    )

//fun Sale.toSale(): Sales {
//    return Sales(discount, (parser.parse(end_date)?: Date()))
//}