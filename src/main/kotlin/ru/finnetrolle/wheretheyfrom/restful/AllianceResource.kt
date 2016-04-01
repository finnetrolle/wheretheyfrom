package ru.finnetrolle.wheretheyfrom.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import ru.finnetrolle.wheretheyfrom.service.entitysource.AllianceEntitySource

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Component
@RequestMapping("/alliance")
class AllianceResource @Autowired constructor (var allianceEntitySource: AllianceEntitySource) {

    data class GetResponse(val name: String, val id: Long, val corporations: Int)

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody fun get(@RequestParam("id") id: Long): ResponseEntity<GetResponse> {
        val ally = allianceEntitySource.get(id)
        return if (ally == null)
            ResponseEntity(HttpStatus.NOT_FOUND)
        else
            ResponseEntity.ok(GetResponse(ally.name, ally.id, ally.corpIds.size))
    }



}