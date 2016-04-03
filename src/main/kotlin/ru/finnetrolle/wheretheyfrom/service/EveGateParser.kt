package ru.finnetrolle.wheretheyfrom.service

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
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

    fun parse(corpName: String): List<HistoryElement> {
        var response = client.target("https://gate.eveonline.com/Corporation/${UrlParamEncoder.encode(corpName)}/" +
                        "CorporationInfoPanel?tab=AllianceHistory")
                .request()
                .get()
        response.bufferEntity()
        var html = response.readEntity(String::class.java)
        val doc = Jsoup.parseBodyFragment(html).getElementsContainingOwnText("PREVIOUS ALLIANCE(S)")
        val history = doc[0].parent().childNodes().filter { x -> x is Element && x.hasClass("employmentHistoryCopy") }
        return history.map { x -> extract(x) }
    }

    fun extract(e: Node): HistoryElement {
        if (e is Element) {
            val name = (e.childNode(1).childNode(0) as TextNode).text().substringBefore(" [")
            val fromText = (e.childNode(2) as TextNode).text().substringAfter("from ").substringBefore(" to")
            val toText = (e.childNode(2) as TextNode).text().substringAfter(" to ").substringBefore(".\r\n")
            val sdf = SimpleDateFormat("yyyy.MM.dd HH:mm")
            return HistoryElement(
                    name,
                    sdf.parse(fromText),
                    sdf.parse(toText))
        } else {
            return HistoryElement("", Date(), Date())
        }

    }

}