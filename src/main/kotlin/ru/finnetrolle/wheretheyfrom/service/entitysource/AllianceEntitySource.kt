package ru.finnetrolle.wheretheyfrom.service.entitysource

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.Alliance
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.HookService
import ru.finnetrolle.wheretheyfrom.service.from
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Component class AllianceEntitySource : AbstractEntitySource<Long, Alliance>()  {

    override fun getType(filename: String): List<Alliance> =
            jacksonObjectMapper().readValue<List<Alliance>>(File(filename))

    @Autowired lateinit var crawler: Crawler
    @Autowired lateinit var hookService: HookService

    override fun gather(id: Long): Alliance? {
        val result = Alliance.from(crawler.getAllyWrapper(id))
        if (result != null)
            hookService.hook(result)
        return result
    }



    override fun extractKey(obj: Alliance) = obj.id

}