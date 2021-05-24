package com.example.arfashion.presentation.app

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.View.*
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.arfashion.presentation.app.presentation.main.ui.categories.KEY_PRODUCT_ID
import com.example.arfashion.presentation.app.presentation.product.detail.ProductDetailActivity
import java.text.SimpleDateFormat
import java.util.*

private val DAY_MONTH_YEAR_TIME_UTC = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

fun Context?.dp(size: Float): Int {
    if (this == null) {
        return 0
    }
    return kotlin.math.ceil(size * this.resources.displayMetrics.density).toInt()
}

fun Context?.dpf(size: Float): Float {
    if (this == null) {
        return 0f
    }
    return kotlin.math.ceil(size * this.resources.displayMetrics.density)
}

fun Context?.dp2(size: Float): Int {
    if (this == null) {
        return 0
    }
    return kotlin.math.floor(size * this.resources.displayMetrics.density).toInt()
}

fun Context?.dp2px(size: Float): Float {
    if (this == null) {
        return 0f
    }
    return size * this.resources.displayMetrics.density
}

fun Context?.sp(size: Float): Float {
    if (this == null) {
        return 0f
    }
    return size * this.resources.displayMetrics.scaledDensity
}

fun Context?.px2sp(pixel: Float): Float {
    if (this == null) {
        return 0f
    }
    return pixel / resources.displayMetrics.scaledDensity
}

fun Context.getColorCompat(@ColorRes colorRes: Int): Int =
    ContextCompat.getColor(this, colorRes)

fun Context.getFont(@FontRes fontRes: Int): Typeface? =
    try {
        ResourcesCompat.getFont(this, fontRes)
    } catch (e: Resources.NotFoundException) {
        null
    }

fun Context.getDrawableCompat(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun Context.getDrawableUri(@DrawableRes drawableRes: Int): Uri =
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(this.resources.getResourcePackageName(drawableRes))
        .appendPath(this.resources.getResourceTypeName(drawableRes))
        .appendPath(this.resources.getResourceEntryName(drawableRes))
        .build()

fun Context.getResourceArray(@ArrayRes arrayRes: Int): List<Int> {
    val ret: MutableList<Int> = ArrayList()

    val typedArray: TypedArray = resources.obtainTypedArray(arrayRes)

    try {
        val length = typedArray.length()
        for (i in 0 until length) {
            val resourceId = typedArray.getResourceId(i, 0)
            ret.add(resourceId)
        }
    } catch (e: Exception) {
    } finally {
        typedArray.recycle()
    }

    return ret
}

fun ViewGroup.inflate(@LayoutRes layout: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layout, this, attachToRoot)
}

fun View.visible() = run { visibility = VISIBLE }

fun View.gone() = run { visibility = GONE }

fun View.invisible() = run { visibility = INVISIBLE }

infix fun Int?.isIn(range: IntRange): Boolean {
    return this != null && range.first <= this && this <= range.last
}

fun Context.openProductDetailActivity(productId: String) {
    val intent = Intent(this, ProductDetailActivity::class.java)
    intent.putExtra(KEY_PRODUCT_ID, productId)
    startActivity(intent)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> Collection<*>.checkItemsAre() =
    if (all { it is T })
        this as List<T>
    else null