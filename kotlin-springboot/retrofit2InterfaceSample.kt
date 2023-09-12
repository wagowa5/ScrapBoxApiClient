import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Headers

interface ScrapboxService {
    @Headers("x-csrf-token: YOUR_CSRF_TOKEN_HERE") // トークンは動的に設定することもできます
    @GET("pages/{projectname}/{pagetitle}")
    fun getPageInfo(@Path("projectname") projectname: String, @Path("pagetitle") pagetitle: String): Call<Map<String, Any>>

    @Headers("x-csrf-token: YOUR_CSRF_TOKEN_HERE")
    @GET("pages/{projectname}/{pagetitle}/text")
    fun getPageText(@Path("projectname") projectname: String, @Path("pagetitle") pagetitle: String): Call<String>

    // 他のエンドポイントも同様に定義...
}

