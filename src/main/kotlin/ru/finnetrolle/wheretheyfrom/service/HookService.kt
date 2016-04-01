package ru.finnetrolle.wheretheyfrom.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.entitysource.AllianceEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CharacterEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CorporationEntitySource

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

    fun hook(alliance: Alliance) {
        println("Alliance ${alliance.name} generates hook")
        Par.go(alliance.corpIds.toList(), {i -> corp.get(i)})
        Par.go(alliance.charIds.toList(), {i -> println();print("$i\t\t"); charSource.get(i)})
    }

    fun hook(pilot: Pilot) {
        println("Pilot ${pilot.name} generates hook")
        Par.go(pilot.occupation, {i -> corp.get(i.corpId)})
        Par.go(pilot.occupation, {i -> if (i.allyId != null) corp.get(i.corpId) else null})
    }
}