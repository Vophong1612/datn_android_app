package com.example.arfashion.presentation.app.widget.indicator

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.arfashion.R
import kotlin.math.min

private const val DEFAULT_RADIUS = 10f
private const val RECT_WIDTH = DEFAULT_RADIUS * 2 * 3

class IndicatorSlideView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var defaultRadius = DEFAULT_RADIUS
    private val rectWidth = RECT_WIDTH
    private var selectedIndex = 0
    private var unselectedColor = Color.WHITE
    private var selectedColor = Color.WHITE
    private var unselectedAlpha = 77
    private var selectedAlpha = 178
    private var dots: MutableList<Dot> = mutableListOf()

    init {
        attrs?.let {
            val typedArray: TypedArray =
                context.obtainStyledAttributes(it, R.styleable.IndicatorSlideView)

            try {
                defaultRadius = typedArray.getDimensionPixelSize(
                    R.styleable.IndicatorSlideView_radius, DEFAULT_RADIUS.toInt()).toFloat()

                selectedColor = typedArray.getColor(
                    R.styleable.IndicatorSlideView_color_selected,
                    ContextCompat.getColor(context, R.color.palattes_2)
                )

                unselectedColor = typedArray.getColor(
                    R.styleable.IndicatorSlideView_color_unselected,
                    ContextCompat.getColor(context, R.color.palattes_3)
                )
            } finally {
                typedArray.recycle()
            }
        }
    }

    fun setDots(count: Int) {
        dots.clear()
        for (i in 0 until count) {
            dots.add(Dot())
        }
        selectedIndex = 1
        requestLayout()
    }

    fun onDotSelected(index: Int) {
        selectedIndex = index
        requestLayout()
    }

    fun getDotsCount(): Int {
        return dots.size
    }

    fun getCurrentPosition(): Int {
        return selectedIndex
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val desireHeight = 2 * defaultRadius.toInt()
        val minimumCalWidth = (defaultRadius * 2 * 10).toInt()
        val widthResult =
            if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
                min(minimumCalWidth, MeasureSpec.getSize(widthMeasureSpec))
            } else {
                minimumCalWidth
            }
        val heightResult: Int =
            if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST) {
                min(desireHeight, MeasureSpec.getSize(heightMeasureSpec))
            } else {
                desireHeight
            }

        setMeasuredDimension(widthResult, heightResult)
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        //circle
        val radius = defaultRadius
        val yCenter = height.toFloat() / 2

        //round rect
        val rectY = yCenter - radius
        val heightRect = radius * 2
        var startX = 0f

        for (i in 1 until dots.size - 1) {
            if (i == selectedIndex) {
                dots[i].isSelected = true
                dots[i].setColor(selectedColor)
                dots[i].setAlpha(selectedAlpha)
                var rectLeft: Float
                var rectRight: Float
                if (i == 0) {
                    rectLeft = startX
                    rectRight = rectLeft + rectWidth
                    startX = rectRight - (radius)
                } else {
                    rectLeft = startX + (radius * 2)
                    rectRight = rectLeft + rectWidth
                    startX = rectRight - (radius)
                }

                dots[i].setRect(rectLeft, rectY, rectRight, rectY + heightRect)
                dots[i].currentRadius = radius
            } else {
                startX = if (i == 0) {
                    startX + radius
                } else {
                    startX + (radius * 2) + radius
                }
                dots[i].setCenter(startX, yCenter)
                dots[i].isSelected = false
                dots[i].setColor(unselectedColor)
                dots[i].setAlpha(unselectedAlpha)
                dots[i].currentRadius = radius
            }

        }
    }

    override fun onDraw(canvas: Canvas) {
        for (i in 1 until dots.size - 1) {
            dots[i].draw(canvas)
        }
        super.onDraw(canvas)
    }
}