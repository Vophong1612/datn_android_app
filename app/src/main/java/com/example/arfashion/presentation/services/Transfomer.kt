package com.example.arfashion.presentation.services

import com.example.arfashion.presentation.app.models.bill.BillItemResponse
import com.example.arfashion.presentation.app.models.cart.Cart
import com.example.arfashion.presentation.app.models.home.GetCarouselResponse
import com.example.arfashion.presentation.app.models.product.*
import com.example.arfashion.presentation.data.model.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.arfashion.presentation.data.model.Cart as CartModel

val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

fun GetCarouselResponse.toCarousel(): Carousel =
    Carousel(name, id, image)

fun ProductResponse.toProduct(): Product {
    return Product(
        id,
        name,
        images.map { it.color },
        sizes.map { size ->
            Size(
                size._id,
                size.name
            )
        },
        comments,
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
        priceSale,
        mask,
    )
}

fun Comments.toComment(): Comment =
    Comment(
        content,
        star,
        _id,
        Profile(
            id = created_by._id,
            name = created_by.name,
            avatar = created_by.avatar
        ),
        responses?.map {
            it.toComment()
        } ?: listOf(),
//        (parser.parse(created_at)?: Date())
    )

//fun RelatedProductResponse.toProduct(): Product =
//    Product(id = id,
//        name = name,
//        prices = price,
//        priceSale = if (priceSale.isNotEmpty()) {
//            price - priceSale[0].discount
//        } else price,
//        images = images.map {
//            it.url
//        },
//        tag = tags.map { tag ->
//            tag.name
//        }
//    )

fun CategoriesResponse.toCategory(): Category =
    Category(
        name = name,
        tag = tags.map { it.name },
        id = _id,
        imageCategory = listImage.img_category.mobile,
        imageBanner = listImage.img_category.mobile
    )

fun ProductByCondition.toProduct(): Product =
    Product(
        id = id,
        name = name,
        prices = price,
        priceSale = if (priceSale.isNotEmpty()) {
            price - priceSale[0].discount
        } else price,
        images = images.map {
            it.url
        },
        tag = /*tags*/ tags.map { tag ->
            tag.name
        }
    )

fun Cart.toCart(): CartModel =
    CartModel(
        id = _id,
        product = products.map {
            Product(
                id = it._id,
                name = it.name,
                prices = it.price,
                priceSale = it.priceSale,
                images = listOf(it.images.mobile),
                sizes = listOf(Size(it.size._id, it.size.name)),
                total = it.total,
                colors = listOf(it.color)
            )
        }
    )

fun Tags.toTag(): Tag =
    Tag(
        id = _id,
        name = name
    )

fun BillItemResponse.toBill() =
    Bill(
        totalCost = totalCost,
        totalProduct = totalProduct,
        note = note,
        id = _id,
        products = products.map { product ->
            Product(
                colors = listOf(product.color),
                prices = product.price,
                sizes = listOf(product.size.let { Size(it._id, it.name) }),
                total = product.total,
                name = product.name,
                id = product._id,
                images = listOf(product.image)
            )
        },
        address = address,
        payment = Payment(payment._id, payment.name),
        billStatus = BillStatus(status._id, status.name)
    )