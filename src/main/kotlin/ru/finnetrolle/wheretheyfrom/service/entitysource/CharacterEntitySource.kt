package ru.finnetrolle.wheretheyfrom.service.entitysource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.HookService
import ru.finnetrolle.wheretheyfrom.service.Pilot
import ru.finnetrolle.wheretheyfrom.service.from

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Component
class CharacterEntitySource : AbstractEntitySource<Long, Pilot>() {

    @Autowired lateinit var crawler: Crawler

    @Autowired lateinit var corpSource: CorporationEntitySource

    override fun extractKey(obj: Pilot): Long = obj.id

    override fun gather(id: Long): Pilot? = Pilot.from(crawler.getCharacterWrapper(id), corpSource)

}