package com.example.arfashion.presentation.app.widget.indicator

import android.graphics.*

class Dot {
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rectF = RectF(0f, 0f, 0f, 0f)
    private var rect = Rect()
    private var center = PointF()

    var isSelected = false
    var currentRadius = 0f

    fun setCenter(x: Float, y: Float) {
        center.set(x, y)
    }

    fun setRect(left: Float, top: Float, right: Float, bottom: Float) {
        rect.set(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        center.set(left, top)
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    fun draw(canvas: Canvas) {
        if (!isSelected) {
            canvas.drawCircle(center.x, center.y, currentRadius, paint)
        } else {
            rectF.set(rect)
            canvas.drawRoundRect(rectF, currentRadius, currentRadius, paint)
        }
    }
}