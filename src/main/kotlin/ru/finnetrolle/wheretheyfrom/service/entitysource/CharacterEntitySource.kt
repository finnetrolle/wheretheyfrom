package ru.finnetrolle.wheretheyfrom.service.entitysource

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.Alliance
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.Pilot
import ru.finnetrolle.wheretheyfrom.service.from
import java.io.File

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Component
class CharacterEntitySource : AbstractEntitySource<Long, Pilot>() {

    override fun getType(filename: String): List<Pilot> =
            jacksonObjectMapper().readValue<List<Pilot>>(File(filename))

    @Autowired lateinit var crawler: Crawler

    @Autowired lateinit var corpSource: CorporationEntitySource

    override fun extractKey(obj: Pilot): Long = obj.id

    override fun gather(id: Long): Pilot? = Pilot.from(crawler.getCharacterWrapper(id), corpSource)

    fun getByName(name: String): Pilot? {
        return getData().values.first {  p -> p.name.toUpperCase().equals(name.toUpperCase()) }
    }

}