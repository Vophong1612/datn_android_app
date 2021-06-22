package com.example.arfashion.presentation.app.widget.indicator

import androidx.viewpager2.widget.ViewPager2
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.visible

typealias UpdateTransformData = (list: List<*>) -> Unit

class PagerIndicatorController(
    private val pager: ViewPager2,
    private val indicator: IndicatorSlideView,
    private val updateData: UpdateTransformData
) {

    init {
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.onDotSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                val currentPosition = indicator.getCurrentPosition()
                val dotsCount = indicator.getDotsCount()
                if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    if (currentPosition == 0)
                        pager.setCurrentItem(dotsCount - 2, false)
                    else if (currentPosition == dotsCount - 1)
                        pager.setCurrentItem(1, false)
                }
            }
        })
    }

    fun handleData(data: List<*>) {
        if (data.isNotEmpty()) {
            transformDataToInfinity(data)
            if (data.size >= 2) {
                indicator.visible()
                pager.adapter?.itemCount?.let { it -> indicator.setDots(it) }
                pager.setCurrentItem(1, false)
            } else {
                indicator.gone()
            }
        }
    }

    private fun transformDataToInfinity(data: List<*>) {
        val transformData = arrayListOf(data.last())
        transformData.addAll(data)
        transformData.add(data.first())
        updateData.invoke(transformData)
    }
}
