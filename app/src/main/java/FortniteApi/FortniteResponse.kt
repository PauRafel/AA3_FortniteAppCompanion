package FortniteApi

data class FortniteResponse(
    val status: Int,
    val data: NewsData
)

data class FortniteShopResponse(
    val status: Int,
    val data: ShopData
)

data class ItemDetailResponse(
    val status: Int,
    val data: BrItem?
)

data class NewsData(
    val hash: String,
    val date: String,
    val image: String,
    val motds: List<NewsItem>
)

data class ShopData(
    val hash: String,
    val date: String,
    val vbuckIcon: String,
    val entries: List<ShopEntry>
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

data class ShopEntry(
    val regularPrice: Int,
    val finalPrice: Int,
    val devName: String,
    val offerId: String,
    val inDate: String,
    val outDate: String,

    val bundle: ShopBundle?,
    val banner: ShopBanner?,
    val offerTag: OfferTag?,

    val giftable: Boolean,
    val refundable: Boolean,
    val sortPriority: Int,

    val layoutId: String?,
    val layout: ShopLayout?,

    val tileSize: String?,
    val displayAssetPath: String?,
    val newDisplayAsset: NewDisplayAsset?,

    val brItems: List<BrItem>?
)

data class ShopBundle(
    val name: String,
    val info: String,
    val image: String
)

data class ShopBanner(
    val value: String,
    val intensity: String,
    val backendValue: String
)

data class OfferTag(
    val id: String,
    val text: String
)

data class ShopLayout(
    val id: String,
    val name: String,
    val category: String,
    val index: Int,
    val rank: Int,
    val background: String?,
    val useWidePreview: Boolean,
    val displayType: String,
    val colors: LayoutColors?
)

data class LayoutColors(
    val color1: String,
    val color2: String,
    val color3: String,
    val textBackgroundColor: String
)

data class NewDisplayAsset(
    val id: String,
    val cosmeticId: String,
    val images: Map<String, String>,
    val colors: Map<String, String>,
    val scalings: Map<String, Float>,
    val flags: Map<String, Boolean>
)

data class BrItem(
    val id: String,
    val name: String,
    val description: String,
    val type: DisplayType,
    val rarity: DisplayType,
    val series: Series?,
    val set: ItemSet?,
    val introduction: Introduction?,
    val images: ItemImages,
    val added: String,
    val shopHistory: List<String>
)

data class DisplayType(
    val value: String,
    val displayValue: String,
    val backendValue: String
)

data class Series(
    val value: String,
    val image: String?,
    val colors: List<String>?,
    val backendValue: String
)

data class ItemSet(
    val value: String,
    val text: String,
    val backendValue: String
)

data class Introduction(
    val chapter: String,
    val season: String,
    val text: String,
    val backendValue: Int
)

data class ItemImages(
    val smallIcon: String?,
    val icon: String?,
    val featured: String?,
    val lego: LegoImages?,
    val bean: BeanImages?
)

data class LegoImages(
    val small: String?,
    val large: String?,
    val wide: String?
)

data class BeanImages(
    val small: String?,
    val large: String?
)

