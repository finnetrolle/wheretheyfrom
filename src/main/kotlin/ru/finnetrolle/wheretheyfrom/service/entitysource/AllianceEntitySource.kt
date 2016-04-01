package ru.finnetrolle.wheretheyfrom.service.entitysource

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.finnetrolle.wheretheyfrom.service.Alliance
import ru.finnetrolle.wheretheyfrom.service.Crawler
import ru.finnetrolle.wheretheyfrom.service.HookService
import ru.finnetrolle.wheretheyfrom.service.from
import java.util.*

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Component class AllianceEntitySource : AbstractEntitySource<Long, Alliance>()  {

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