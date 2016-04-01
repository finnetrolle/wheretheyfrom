package ru.finnetrolle.wheretheyfrom.service

import org.springframework.stereotype.Component
import org.xml.sax.InputSource
import java.io.StringReader
import javax.ws.rs.client.ClientBuilder
import javax.xml.parsers.SAXParserFactory

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
@Component
class EveGateParser {

    val client = ClientBuilder.newClient()

    fun parse(corpName: String): String {
        var response = client.target("https://gate.eveonline.com/Corporation/${UrlParamEncoder.encode(corpName)}/CorporationInfoPanel?tab=AllianceHistory")
                .request()
                .get()
//        var response = client.target("https://gate.eveonline.com")
//                .path("/Corporation/{name}/CorporationInfoPanel")
//                .resolveTemplate("name", UrlParamEncoder.encode(corpName))
//                .queryParam("tab", "AllianceHistory")
//                .request()
//                .get()
        response.bufferEntity()
        var html = "<root>${response.readEntity(String::class.java)}</root>"
        xml(html)
        return html
    }

    fun xml(html: String) {
        val parser = SAXParserFactory.newInstance().newSAXParser()
        val handler = EveGateHandler()
        parser.parse(InputSource(StringReader(html)), handler)

    }

}