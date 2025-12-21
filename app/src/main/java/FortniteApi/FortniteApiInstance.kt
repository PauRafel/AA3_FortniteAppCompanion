package FortniteApi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FortniteApiInstance {

    private const val BASE_URL = "https://fortnite-api.com/"

    val api: FortniteApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FortniteApiService::class.java)
    }
}