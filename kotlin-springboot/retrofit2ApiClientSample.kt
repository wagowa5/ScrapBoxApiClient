import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ScrapboxAPI(projectname: String, csrfToken: String? = null) {
    private val baseUrl = "https://scrapbox.io/api/"
    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val service: ScrapboxService = retrofit.create(ScrapboxService::class.java)

    fun getPageInfo(pagetitle: String): Map<String, Any> {
        return service.getPageInfo(projectname, pagetitle).execute().body() ?: throw RuntimeException("Failed to get page info")
    }

    fun getPageText(pagetitle: String): String {
        return service.getPageText(projectname, pagetitle).execute().body() ?: throw RuntimeException("Failed to get page text")
    }

    // 他のエンドポイントも同様に実装...
}

