package ru.finnetrolle.wheretheyfrom.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.ws.rs.client.ClientBuilder

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
@Component
class EveGateParser {

    val client = ClientBuilder.newClient()

    data class HistoryElement(val allyName: String, val from: Date, val to: Date)

    private val emptys = ConcurrentHashMap<Long, Long>()

    fun parse(corpId: Long): List<HistoryElement> {
        if (emptys[corpId] != null) {
            return listOf()
        }
        var html: String = "NO HTML AVAILABLE"
        var response = client.target("http://evemaps.dotlan.net/corp/${corpId}/alliances")
                    .request()
                    .get()
        response.bufferEntity()
        html = response.readEntity(String::class.java)
        val tables = Jsoup.parse(html).getElementsByClass("tablelist")
        if (tables.size != 2) {
                emptys.put(corpId, corpId)
                println("$corpId have no history")
                return listOf()
        }
        try {
            val myTable = tables.get(1).childNode(1)
            return myTable.childNodes()
                    .filter { n -> n.hasAttr("class") }
                    .map { extract(it) }
        } catch (e: Exception) {
            println("No history for $corpId because of ${e.message} with html = $html")
            emptys.put(corpId, corpId)
            return listOf()
        }
    }

    fun extract(e: Node): HistoryElement {
        if (e is Element) {
            val name = (e.childNode(3).childNode(0).childNode(0).childNode(0) as TextNode).text()
            val fromText = (e.childNode(7).childNode(0) as TextNode).text()
            val toText = (e.childNode(9).childNode(0) as TextNode).text()

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return HistoryElement(
                    name,
                    sdf.parse(if (fromText.equals("Unknown")) "2001.01.01 00:00:00" else fromText),
                    if (toText.equals("-")) Date() else sdf.parse(toText))
        } else {
            return HistoryElement("", Date(), Date())
        }

    }

    /*
    fun parse(corpName: String): List<HistoryElement> {
        var html: String = "NO HTML AVAILABLE"
        try {
            var response = client.target("https://gate.eveonline.com/Corporation/${UrlParamEncoder.encode(corpName)}/" +
                    "CorporationInfoPanel?tab=AllianceHistory")
                    .request()
                    .get()
            response.bufferEntity()
            html = response.readEntity(String::class.java)
            val doc = Jsoup.parseBodyFragment(html).getElementsContainingOwnText("PREVIOUS ALLIANCE(S)")
            if (doc.size == 0) {
                println("$corpName have no history")
                return listOf()
            }
            val history = doc[0].parent().childNodes().filter { x -> x is Element && x.hasClass("employmentHistoryCopy") }
            return history.map { x -> extract(x) }
        } catch (e: Exception) {
            println("No history for $corpName because of ${e.message} with html = $html")
            return listOf()
        }
    }

    fun extract(e: Node): HistoryElement {
        if (e is Element) {
            val name = (e.childNode(1).childNode(0) as TextNode).text().substringBefore(" [")
            val fromText = (e.childNode(2) as TextNode).text().substringAfter("from ").substringBefore(" to")
            val toText = (e.childNode(2) as TextNode).text().substringAfter(" to ").substringBefore(".\r\n")
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return HistoryElement(
                    name,
                    sdf.parse(if (fromText.equals("Unknown")) "2001.01.01 00:00" else fromText),
                    sdf.parse(toText))
        } else {
            return HistoryElement("", Date(), Date())
        }

    }*/

}