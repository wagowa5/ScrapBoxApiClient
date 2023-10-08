package com.gpt.clientsample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ClientSampleApplication

fun main(args: Array<String>) {
    runApplication<ClientSampleApplication>(*args)

    val webClient = WebClient.builder()

    val response = webClient.build()
        .get()
        .uri("https://scrapbox.io/api/pages/project名/ページタイトル")
        //.contentType(MediaType.APPLICATION_JSON)
        //.header("Authorization", "Bearer トークン") // APIキーを設定
        .retrieve()
        .bodyToMono(PageResponse::class.java) // レスポンスボディの型を指定
    
    System.out.println("--------------------------")
    System.out.println(response.block())
    System.out.println("--------------------------")

}

data class PageResponse(
    val id: String,  // ページのid
    val title: String,  // ページのタイトル
    val image: String?,  // ページのサムネイル画像
    val descriptions: List<String>,  // ページのサムネイル本文。おそらく最大5行
    val pin: Int,  // ピン留めされていたら1, されていなかったら0
    val views: Int,  // ページの閲覧回数
    val linked: Int,  // おそらく被リンク数
    val commitId: String?,  // 最新の編集コミットid
    val created: Int,  // ページの作成日時
    val updated: Int,  // ページの最終更新日時
    val accessed: Int,  // Date last visitedに使われる最終アクセス日時
    val lastAccessed: Int?,  // APIを叩いたuserの最終アクセス日時。おそらくこの値を元にテロメアの未読/既読の判別をしている
    val snapshotCreated: Int?,  // Page historyの最終生成日時
    val snapshotCount: Int,  // 生成されたPage historyの数
    val pageRank: Int,  // page rank
    val persistent: Boolean,  // 不明。削除されたページだとfalse？
    val lines: List<Line>,  // 本文。一行ずつ入っている
    val links: List<String>,  // ページ内のリンク
    val icons: List<String>,  // ページアイコン
    val files: List<String>,  // ページ内に含まれる、scrapbox.ioにアップロードしたファイルへのリンク
    val relatedPages: RelatedPages,  // 関連ページ
    val user: User,  // ページの編集者。最後に編集した人が入る？
    val collaborators: List<Collaborator>  // このページの編集者から"user"を除いたもの
)

data class Line(
    val id: String,  // 行のid
    val text: String,  // 行のテキスト
    val userId: String,  // 一番最後に行を編集した人のid
    val created: Int,  // 行の作成日時
    val updated: Int  // 行の最終更新日時
)

data class RelatedPage(
    val id: String,  // ページのid
    val title: String,  // ページのタイトル
    val titleLc: String,
    val image: String?,  // ページのサムネイル画像
    val descriptions: List<String>,  // ページのサムネイル本文。おそらく最大5行
    val linksLc: List<String>,
    val linked: Int,  // おそらく被リンク数
    val updated: Int,  // ページの最終更新日時
    val accessed: Int  // おそらくページの閲覧日時
)

data class RelatedPages(
    val links1hop: List<RelatedPage>,
    val links2hop: List<RelatedPage>,
    val hasBackLinksOrIcons: Boolean  // このページを参照しているページorアイコンがあればtrue
)

data class User(
    val id: String,
    val name: String,
    val displayName: String,
    val photo: String
)

data class Collaborator(
    val id: String,
    val name: String,
    val displayName: String,
    val photo: String
)
