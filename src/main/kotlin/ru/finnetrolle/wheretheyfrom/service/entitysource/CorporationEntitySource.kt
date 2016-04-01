package ru.finnetrolle.wheretheyfrom.service.entitysource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.Corporation
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.HookService
import ru.finnetrolle.wheretheyfrom.service.from

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
@Component class CorporationEntitySource : AbstractEntitySource<Long, Corporation>() {

    @Autowired lateinit var crawler: Crawler
    @Autowired lateinit var hookService: HookService

    override fun extractKey(obj: Corporation) = obj.id

    override fun gather(id: Long): Corporation? {
        val wrapper = crawler.getCorpWrapper(id)
        try {
            return Corporation.from(wrapper)
        } catch (e: Exception) {
            println("TROUBLE with $wrapper")
            return null
        }
    }

}