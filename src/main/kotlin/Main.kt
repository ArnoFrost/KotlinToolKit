import com.arno.tech.toolkit.service.addTagsToBookmarks
import com.arno.tech.toolkit.service.convertBookmarksToFlatJson
import com.arno.tech.toolkit.service.convertJsonToMarkdown
import com.arno.tech.toolkit.utils.DateUtils

fun main(args: Array<String>) {
    val rootPath = "/Users/xuxin14/Desktop/Bookmark/"
    val inputFile = rootPath + "input.json"
    convertBookmark(rootPath, inputFile)
}

fun convertBookmark(rootPath: String, bookmarkJson: String) {
    val addDate = DateUtils.getCurrentFormattedDate()
    val tagsJsonOutputFile = rootPath + addDate + "_" + "tagsJson.json"
    val flattenOutputFile = rootPath + addDate + "_" + "flattenJson.json"
    val markdownOutputFile = rootPath + addDate + "_" + "bookmark.md"
    // 1. 为书签添加tags
    addTagsToBookmarks(bookmarkJson, tagsJsonOutputFile)
    //2. 将书签转换为扁平化的json
    convertBookmarksToFlatJson(tagsJsonOutputFile, flattenOutputFile)
    //3. 将扁平化的json转换为markdown
    convertJsonToMarkdown(flattenOutputFile, markdownOutputFile)
}