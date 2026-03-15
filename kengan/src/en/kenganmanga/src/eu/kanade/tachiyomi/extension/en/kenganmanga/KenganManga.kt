package eu.kanade.tachiyomi.extension.en.kenganmanga

import eu.kanade.tachiyomi.network.GET
import eu.kanade.tachiyomi.source.model.FilterList
import eu.kanade.tachiyomi.source.model.Page
import eu.kanade.tachiyomi.source.model.SChapter
import eu.kanade.tachiyomi.source.model.SManga
import eu.kanade.tachiyomi.source.online.ParsedHttpSource
import okhttp3.Request
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class KenganManga : ParsedHttpSource() {
    override val name = "Kengan Manga Online"
    override val baseUrl = "https://w15.kengan-manga.com"
    override val lang = "en"
    override val supportsLatest = true

    // --- POPULAR / LATEST (Site à manga unique : on renvoie vers l'accueil) ---
    override fun popularMangaRequest(page: Int): Request = GET(baseUrl, headers)
    override fun popularMangaSelector() = "div.container"
    override fun popularMangaFromElement(element: Element): SManga = SManga.create().apply {
        title = "Kengan Omega"
        url = "/"
        thumbnail_url = "https://w15.kengan-manga.com/wp-content/uploads/2020/01/kengan-omega-logo.png"
    }
    override fun popularMangaNextPageSelector(): String? = null

    override fun latestUpdatesRequest(page: Int): Request = popularMangaRequest(page)
    override fun latestUpdatesSelector() = popularMangaSelector()
    override fun latestUpdatesFromElement(element: Element): SManga = popularMangaFromElement(element)
    override fun latestUpdatesNextPageSelector(): String? = null

    // --- SEARCH ---
    override fun searchMangaRequest(page: Int, query: String, filters: FilterList): Request = popularMangaRequest(page)
    override fun searchMangaSelector() = popularMangaSelector()
    override fun searchMangaFromElement(element: Element): SManga = popularMangaFromElement(element)
    override fun searchMangaNextPageSelector(): String? = null

    // --- DETAILS ---
    override fun mangaDetailsParse(document: Document): SManga = SManga.create().apply {
        title = "Kengan Omega"
        author = "Sandrovich Yabako"
        artist = "Daromeon"
        description = "The sequel to the hit martial arts manga Kengan Ashura."
        status = SManga.ONGOING
    }

    // --- CHAPTERS ---
    override fun chapterListSelector() = "ul.sub-menu li, div.chapters-list div.chapter"
    override fun chapterFromElement(element: Element): SChapter = SChapter.create().apply {
        val link = element.select("a").first()
        url = link?.attr("href")?.removePrefix(baseUrl) ?: ""
        name = link?.text() ?: "Chapter"
    }

    // --- PAGES ---
    override fun pageListParse(document: Document): List<Page> {
        return document.select("div.vung-doc img, div.reader-area img").mapIndexed { i, img ->
            // On utilise abs:src pour transformer les liens relatifs en liens complets automatiquement
            Page(i, "", img.attr("abs:src"))
        }
    }

    override fun imageUrlParse(document: Document): String = throw UnsupportedOperationException("Not used")
}