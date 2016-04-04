package ru.finnetrolle.wheretheyfrom.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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

@Component
class HookService () {

    @Autowired lateinit var ally: AllianceEntitySource

    @Autowired lateinit var corp: CorporationEntitySource

    @Autowired lateinit var charSource: CharacterEntitySource

    @Autowired lateinit var parser: EveGateParser

    fun hook(alliance: Alliance) {
        println("Alliance ${alliance.name} fulfill")
        val start = System.currentTimeMillis()
        Par.go(alliance.corpIds.toList(), {i -> corp.get(i)})
        Par.go(alliance.charIds.toList(), {i -> charSource.get(i)})
        Par.go(corp.all().toList(), {c -> c.occupation.addAll(parser.parse(c.id)) })
        Par.go(alliance.charIds.toList(), {i ->
            val pilot = charSource.get(i)
            if (pilot != null) {
                pilot.allyOccupation.addAll(defineOcc(pilot.occupation))
            }
        })

        println("Done in ${System.currentTimeMillis() - start}")
    }

    fun defineOcc(pilotOccupation: List<Occupation>): List<EveGateParser.HistoryElement> {
        return pilotOccupation
                .map { Pair(it, corp.get(it.corpId)) }
                .filter { p -> p.second != null }
                .flatMap { historyFromCorp(it.first, it.second!!) }
                .sortedBy { r -> r.to }
    }

    fun historyFromCorp(occupation: Occupation, corporation: Corporation): List<EveGateParser.HistoryElement> {
        val viable = corporation.occupation
                .filter { o -> o.from.before(occupation.till) && o.to.after(occupation.from) }
                .map { o -> EveGateParser.HistoryElement(o.allyName,
                            if (o.from.before(occupation.from)) occupation.from else o.from,
                            if (o.to.after(occupation.till)) occupation.till else o.to)
                }.sortedBy { o -> o.to }
        return viable
    }

}