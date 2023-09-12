import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException

/**
 * 注意事項：
 * 1. WebClientは非同期で動作しますが、.block()メソッドを使用して同期的に結果を取得しています。
 * 2. 成功の応答がJSONとして返ってくることを想定してMap<String, Any>としてデコードしますが、実際のAPIの応答に応じて適切なレスポンス型に変更する必要があります。
 * 3. エラー処理は簡略化しています。適切なエラーメッセージや例外処理の追加が必要です。
 */
class ScrapboxAPI(private val projectname: String, private val csrfToken: String? = null) {
    private val baseUrl = "https://scrapbox.io/api/"
    private val webClient: WebClient = WebClient.builder()
        .baseUrl(baseUrl)
        .defaultHeaders {
            if (csrfToken != null) {
                it.set("x-csrf-token", csrfToken)
            }
        }
        .build()

    private fun <T> makeRequest(endpoint: String, responseType: Class<T>): T {
        val response = webClient.get()
            .uri(endpoint)
            .retrieve()
            .onStatus({ it != HttpStatus.OK }) {
                it.createException()
            }
            .bodyToMono(responseType)
            .block() ?: throw WebClientResponseException("Empty response body", 404, "", null, null, null)

        return response
    }

    fun getPageInfo(pagetitle: String): Map<String, Any> {
        return makeRequest("pages/$projectname/$pagetitle", Map::class.java)
    }

    fun getPageText(pagetitle: String): String {
        return makeRequest("pages/$projectname/$pagetitle/text", String::class.java)
    }

    // ... 他のAPIエンドポイントに対応するメソッドを追加 ...

    fun fullTextSearch(query: String): Map<String, Any> {
        return makeRequest("pages/$projectname/search/$query", Map::class.java)
    }

    fun getProjectInfo(): Map<String, Any> {
        return makeRequest("projects/$projectname", Map::class.java)
    }

    // ... 更に他のAPIエンドポイントに対応するメソッドを追加 ...

    fun getUserInfo(): Map<String, Any> {
        return makeRequest("users/me", Map::class.java)
    }

    // CSRF tokenが必要なエンドポイントのメソッドも同様に追加 ...
}

