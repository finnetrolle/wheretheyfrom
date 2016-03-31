package ru.finnetrolle.wheretheyfrom.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Invocation
import javax.ws.rs.client.WebTarget
import javax.ws.rs.core.MediaType


/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Component
class Crawler {

    data class Character(val character_id: String, val corporation_id: String, val alliance_id: String, val name: String)
    data class AllyInfo(val alliance_id: String, val name: String, val memberCount: Int)
    data class AllyWrapper(val info: AllyInfo, val characters: List<Character>)
    data class History(val corporation_id: String, val start_date: String, val end_date: String?)
    data class CharacterWrapper(val info: CharacterInfo, val history: List<History>)
    data class CharacterInfo(val character_id: String, val corporation_id: String, val alliance_id: String,
                             val faction_id: String, val name: String, val sec_status: String)

    val client = ClientBuilder.newClient()

    fun getUsersOfAlly(ally: Long):List<Character> {
        val checkResp = makeAlliListRequest(ally.toString(), 10000)
        checkResp.bufferEntity()
        val count = jacksonObjectMapper().readValue<AllyWrapper>(checkResp.readEntity(String::class.java)).info.memberCount
        return (0..(count/200)).map { mineAllyUsers(ally.toString(), it) }.flatten()
    }

    fun getVisitedCorps(ally: Long) = getVisitedCorps(getUsersOfAlly(ally))

    private fun getVisitedCorps(chars: List<Character>): Set<String> {
        val corps = Par.go(chars, {c -> getCharacterSheet(c.character_id)})
                .flatMap { p -> p.history }
                .map { h -> h.corporation_id }
        println("whole ${corps.size} corps")
        return corps.toSet()
    }

    fun getCharacterSheet(id: String): CharacterWrapper {
        val resp = makeCharacterRequest(id)
        resp.bufferEntity()
        val data = jacksonObjectMapper().readValue<CharacterWrapper>(resp.readEntity(String::class.java))
        return data
    }

    private fun makeAlliListRequest(id: String, page: Int) = makeRequest("allilist", client, id, page)
    private fun makeCharacterRequest(id: String) = makeRequest("character", client, id, 0)

    private fun makeRequest(type: String, client: Client, id: String, page: Int) = client.target("http://evewho.com").path("api.php")
                .queryParam("type", type)
                .queryParam("id", id)
                .queryParam("page", page)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get()

    private fun mineAllyUsers(ally: String, page: Int): List<Character> {
        val resp = makeAlliListRequest(ally, page)
        resp.bufferEntity()
        val data = jacksonObjectMapper().readValue<AllyWrapper>(resp.readEntity(String::class.java))
        return data.characters
    }

}