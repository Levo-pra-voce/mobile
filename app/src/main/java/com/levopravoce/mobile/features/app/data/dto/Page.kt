package com.levopravoce.mobile.features.app.data.dto

data class Page<T>(
    val totalElements: Int = 0,
    val content: List<T> = emptyList(),
)
