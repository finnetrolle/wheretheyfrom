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
class PreviousService {

    @Autowired lateinit var allianceEntitySource: AllianceEntitySource
    @Autowired lateinit var corporationEntitySource: CorporationEntitySource
    @Autowired lateinit var characterEntitySource: CharacterEntitySource

    data class AllyStat(val name: String, val exists: Int)

    fun getExes(currentAllyId: Long, from: Date, depth: Int): List<AllyStat> {
        val ally = allianceEntitySource.get(currentAllyId)
        if (ally == null) {
            return listOf()
        }

        val list = ally.charIds//.toList().subList(1,10)
                .map { id -> characterEntitySource.get(id) }
                .filter { x -> x != null }
                .flatMap { x -> x!!.allyOccupation.takeLast(depth) }
                .filter { x -> x!!.to.compareTo(from) >= 0 }
                .map { o -> o.allyName }
        val map = mutableMapOf<String, Int>()
        list.forEach { a ->
            if (map[a] == null)
                map.put(a, 0)
            map[a] = map[a]!! + 1
        }

        return map.map { v -> AllyStat(v.key, v.value) }.sortedBy { e -> e.exists }.reversed()

    }

}