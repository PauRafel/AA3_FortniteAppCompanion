package FortniteApi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FortniteApiService {

    @GET("v2/news/br")
    fun getNews(
        @Query("language") language: String = "en"
    ): Call<FortniteResponse>

    @GET("v2/shop")
    fun getShop(
        @Query("language") language: String = "en"
    ): Call<FortniteShopResponse>

}