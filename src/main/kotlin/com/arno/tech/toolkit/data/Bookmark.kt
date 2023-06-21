package com.arno.tech.toolkit.data

import kotlinx.serialization.Serializable

@Serializable
data class Bookmark(
    val guid: String,
    val title: String,
    val index: Int,
    val dateAdded: Long,
    val lastModified: Long,
    val id: Int,
    val typeCode: Int,
    val type: String,
    val root: String? = null,
    val uri: String? = null,
    val iconUri: String? = null,
    val children: MutableList<Bookmark>? = null,
    var tags: MutableList<String>? = null
)
