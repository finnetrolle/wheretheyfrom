package ru.finnetrolle.wheretheyfrom.service.entitysource

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.*
import java.io.File

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
@Component class CorporationEntitySource : AbstractEntitySource<Long, Corporation>() {

    @Autowired lateinit var crawler: Crawler
    @Autowired lateinit var hookService: HookService

    override fun extractKey(obj: Corporation) = obj.id

    override fun getType(filename: String): List<Corporation> =
            jacksonObjectMapper().readValue<List<Corporation>>(File(filename))

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