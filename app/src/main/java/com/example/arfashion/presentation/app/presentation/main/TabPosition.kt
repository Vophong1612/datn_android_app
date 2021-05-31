package com.example.arfashion.presentation.app.presentation.main

sealed class TabPosition(val index: Int) {
    object Categories : TabPosition(0)
    object Home : TabPosition(1)
    object Profile : TabPosition(2)
}
