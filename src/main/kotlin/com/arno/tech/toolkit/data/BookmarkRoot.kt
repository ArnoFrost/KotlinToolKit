package com.arno.tech.toolkit.data

import kotlinx.serialization.Serializable

@Serializable
data class BookmarkRoot(
    val guid: String,
    val title: String,
    val index: Int,
    val dateAdded: Long,
    val lastModified: Long,
    val id: Int,
    val typeCode: Int,
    val type: String,
    val root: String,
    val children: MutableList<Bookmark>
)