package FortniteApi

data class FortniteResponse(
    val status: Int,
    val data: NewsData
)
data class NewsData(
    val hash: String,
    val date: String,
    val image: String,
    val motds: List<NewsItem>
)
data class NewsItem(
    val id: String,
    val title: String,
    val tabTitle: String,
    val body: String,
    val image: String,
    val tileImage: String,
    val sortingPriority: Int,
    val hidden: Boolean,
    val websiteUrl: String?,
    val videoString: String?,
    val videoId: String?,
    val messages: List<NewsMessage>?
)

data class NewsMessage(
    val title: String,
    val body: String,
    val image: String,
    val adspace: String
)