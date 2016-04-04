package ru.finnetrolle.wheretheyfrom.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.EveGateParser
import ru.finnetrolle.wheretheyfrom.service.Pilot
import ru.finnetrolle.wheretheyfrom.service.PreviousService
import ru.finnetrolle.wheretheyfrom.service.entitysource.AllianceEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CharacterEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CorporationEntitySource
import java.text.SimpleDateFormat
import java.util.*

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
        val eveGateParser: EveGateParser,
        val prev: PreviousService
) {

    data class H1(val name: String, val from: String, val till: String)

    data class Out(val pilot: String, val corps: List<H1>, val allys: List<H1>)

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

    @RequestMapping("investigate")
    @ResponseBody fun investigate(
            @RequestParam(value = "from", required = false, defaultValue = "2000-01-01") from: String,
            @RequestParam(value = "id", required = true) id: Long,
            @RequestParam(value = "depth", required = false, defaultValue = "1") depth: Int)
            :ResponseEntity<List<PreviousService.AllyStat>> {
        val dateFrom: Date = SimpleDateFormat("yyyy-MM-dd").parse(from)
        return ResponseEntity.ok(prev.getExes(id, dateFrom, depth))
//        return ResponseEntity.ok(prev.getExes(1411711376))
//        return ResponseEntity.ok(prev.getExes(99003805))
    }

    @RequestMapping("save")
    @ResponseBody
    fun save():ResponseEntity<String> {
        allianceEntitySource.save("allys.json")
        corporationEntitySource.save("corps.json")
        characterEntitySource.save("chars.json")
        return ResponseEntity.ok("OK")
    }

    @RequestMapping("load")
    @ResponseBody
    fun load():ResponseEntity<String> {
        allianceEntitySource.load("allys.json")
        corporationEntitySource.load("corps.json")
        characterEntitySource.load("chars.json")
        return ResponseEntity.ok("OK")
    }

    @RequestMapping("pilot")
    @ResponseBody
    fun pilot(@RequestParam("name") name: String):ResponseEntity<Out> {
        val pilot = characterEntitySource.getByName(name)
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        if (pilot == null) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            val corps = pilot.occupation.map { o -> H1(corporationEntitySource.get(o.corpId)!!.name, sdf.format(o.from), sdf.format(o.till)) }
            val allys = pilot.allyOccupation.map { a -> H1(a.allyName, sdf.format(a.from), sdf.format(a.to)) }
            return ResponseEntity.ok(Out(pilot.name, corps, allys))
        }
    }



}