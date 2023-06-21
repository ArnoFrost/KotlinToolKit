package com.arno.tech.toolkit.data

import kotlinx.serialization.Serializable

@Serializable
data class FlatBookmark(
    val title: String,
    val url: String,
    val add_date: String,
    val tags: List<String>?
)