package com.arno.tech.toolkit.service

import com.arno.tech.toolkit.data.Bookmark
import com.arno.tech.toolkit.data.BookmarkRoot
import com.arno.tech.toolkit.data.FlatBookmark
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun main() {
    val inputFile = "/Users/xuxin14/Desktop/input.json"
    val outputFile = "/Users/xuxin14/Desktop/output.json"
    val flattenOutputFile = "/Users/xuxin14/Desktop/flatten.json"
    addTagsToBookmarks(inputFile, outputFile)
    convertBookmarksToFlatJson(outputFile, flattenOutputFile)
}

//region 1. 为书签添加tags
fun addTagsToBookmarks(inputFile: String, outputFile: String) {
    val json = Json { prettyPrint = true }

    // 读取输入文件
    val jsonString = try {
        File(inputFile).readText()
    } catch (e: Exception) {
        println("Warning: Cannot read input file. $e")
        return
    }

    // 解析输入 JSON
    val bookmarkRoot = try {
        json.decodeFromString<BookmarkRoot>(jsonString)
    } catch (e: Exception) {
        println("Warning: Cannot parse input JSON. $e")
        return
    }

    // 添加标签
    for (child in bookmarkRoot.children) {
        processBookmark(child, listOf())
    }

    // 输出 JSON
    val result = json.encodeToString(bookmarkRoot)

    // 写入到输出文件
    try {
        File(outputFile).writeText(result)
        println("Success: The output JSON has been written to $outputFile")
    } catch (e: Exception) {
        println("Warning: Cannot write to output file. $e")
    }
}

fun processBookmark(bookmark: Bookmark, tags: List<String>) {
    if (bookmark.type == "text/x-moz-place") {
        // 这是一个书签
        bookmark.tags = tags.toMutableList().filter { validateBookmark(it) }.toMutableList()
        println("Info: Tags added to bookmark ${bookmark.title}: ${bookmark.tags}")
    } else if (bookmark.children != null) {
        // 这是一个文件夹
        val newTags = tags + bookmark.title
        if (bookmark.title.contains(" ")) {
            println("Info: Folder name contains space: ${bookmark.title}")
        }
        for (child in bookmark.children) {
            processBookmark(child, newTags)
        }
    } else {
        println("Warning: Unknown type of bookmark object: ${bookmark.title}")
    }
}

private fun validateBookmark(it: String) = it != "unfiled" && it != "toolbar" && it != "menu"
//endregion

//region 2. 将书签转换为扁平化的json
fun convertBookmarksToFlatJson(inputFile: String, outputFile: String) {
    val json = Json { prettyPrint = true }

    // 读取输入文件
    val jsonString = try {
        File(inputFile).readText()
    } catch (e: Exception) {
        println("Warning: Cannot read input file. $e")
        return
    }

    // 解析输入 JSON
    val bookmarkRoot = try {
        json.decodeFromString<BookmarkRoot>(jsonString)
    } catch (e: Exception) {
        println("Warning: Cannot parse input JSON. $e")
        return
    }

    // 扁平化书签并收集到列表中
    val flatBookmarks = bookmarkRoot.children.flatMap { flattenBookmark(it, listOf()) }

    // 输出 JSON
    val result = json.encodeToString(flatBookmarks)

    // 写入到输出文件
    try {
        File(outputFile).writeText(result)
        println("Success: The flattened JSON has been written to $outputFile")
    } catch (e: Exception) {
        println("Warning: Cannot write to output file. $e")
    }
}

fun flattenBookmark(bookmark: Bookmark, tags: List<String>): List<FlatBookmark> {
    return if (bookmark.children != null) {
        bookmark.children.flatMap { flattenBookmark(it, tags + bookmark.title) }
    } else {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
        val add_date = formatter.format(Instant.ofEpochMilli(bookmark.dateAdded / 1000))
        listOf(FlatBookmark(bookmark.title, bookmark.uri ?: "", add_date, bookmark.tags))
    }
}
//endregion

//region 3. 将扁平化的json转换为markdown
fun convertJsonToMarkdown(inputFile: String, outputFile: String) {
    val json = Json { prettyPrint = true }

    // 读取输入文件
    val jsonString = try {
        File(inputFile).readText()
    } catch (e: Exception) {
        println("Warning: Cannot read input file. $e")
        return
    }

    // 解析输入 JSON
    val bookmarks = try {
        json.decodeFromString<List<FlatBookmark>>(jsonString)
    } catch (e: Exception) {
        println("Warning: Cannot parse input JSON. $e")
        return
    }

    // 转换成 Markdown 格式
    val markdown = bookmarks.joinToString("\n") { bookmarkToMarkdown(it) }

    // 写入到输出文件
    try {
        File(outputFile).writeText(markdown)
        println("Success: The Markdown content has been written to $outputFile")
    } catch (e: Exception) {
        println("Warning: Cannot write to output file. $e")
    }
}

fun bookmarkToMarkdown(bookmark: FlatBookmark): String {
    val tags = bookmark.tags?.joinToString(" ") { " #$it " }
    return "[${bookmark.title}](${bookmark.url}) $tags ,date=${bookmark.add_date}"
}
//endregion

