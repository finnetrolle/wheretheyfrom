package ru.finnetrolle.wheretheyfrom.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.EveGateParser
import ru.finnetrolle.wheretheyfrom.service.entitysource.AllianceEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CharacterEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CorporationEntitySource

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Controller
@RequestMapping("/test")
class TestResource @Autowired constructor (
        val crawler: Crawler,
        val allianceEntitySource: AllianceEntitySource,
        val corporationEntitySource: CorporationEntitySource,
        val characterEntitySource: CharacterEntitySource,
        val eveGateParser: EveGateParser
) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun test():ResponseEntity<String> {
//        return ResponseEntity.ok(crawler.getUsersOfAlly(1411711376))
//        return ResponseEntity.ok(crawler.getVisitedCorps(1411711376))
        return ResponseEntity.ok("ok")
    }

    data class Stats(val alliances: Int, val corporations: Int, val characters: Int)

    @RequestMapping("dig")
    @ResponseBody
    fun dig():ResponseEntity<Stats> {
        return ResponseEntity.ok(Stats(allianceEntitySource.size(), corporationEntitySource.size(), characterEntitySource.size()))
    }

    @RequestMapping("gate")
    @ResponseBody
    fun gate():ResponseEntity<List<EveGateParser.HistoryElement>> {
        return ResponseEntity.ok(eveGateParser.parse("Kings of the Carnival Creation"))
    }



}