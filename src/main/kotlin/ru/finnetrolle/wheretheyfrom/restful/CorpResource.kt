package ru.finnetrolle.wheretheyfrom.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.wheretheyfrom.service.entitysource.AllianceEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CorporationEntitySource

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
@Component
@RequestMapping("/corp")
class CorpResource @Autowired constructor (val corporationEntitySource: CorporationEntitySource) {

    data class GetResponse(val name: String, val id: Long)

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody fun get(@RequestParam("id") id: Long): ResponseEntity<GetResponse> {
        val corp = corporationEntitySource.get(id)
        return if (corp == null)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity.ok(GetResponse(corp.name, corp.id))
    }



}