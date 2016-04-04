package ru.finnetrolle.wheretheyfrom.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import javax.ws.rs.client.ClientBuilder
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
    data class CorporationInfo(val corporation_id: String, val alliance_id: String, val name: String,
                               val ticker: String, val member_count: String, val is_npc_corp: String,
                               val avg_sec_status: String, val active: String, val ceoID: String,
                               val taxRate: String, val description: String)
    data class CorporationInfoWrapper(val info: CorporationInfo?)

    private val client = ClientBuilder.newClient()

    private fun ask(type: String, id: Long, page: Int): String? {
        try {
            val resp = client.target("http://evewho.com").path("api.php")
                    .queryParam("type", type)
                    .queryParam("id", id)
                    .queryParam("page", page)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get()
            resp.bufferEntity()
            return resp.readEntity(String::class.java)
        } catch (e: Exception) {
            println("Some shit happened for $id. Continue")
            // Todo: Add logger here
            return null
        }
    }

    private fun askAllyWrapper(id: Long, page: Int): AllyWrapper? {
        val json = ask("allilist", id, page)
        return if (json != null) {
            jacksonObjectMapper().readValue<AllyWrapper>(json)
        } else json
    }

    fun getAllyWrapper(id: Long): AllyWrapper? {
        val info = askAllyWrapper(id, 10000)
        if (info == null)
            return null
        else {
            val corpsList = Par.go((0..info.info.memberCount/200).toList(), { i -> askAllyWrapper(id, i)})
                    .filter { s -> s != null}
                    .flatMap { i -> i!!.characters }
            return AllyWrapper(info.info, corpsList)
        }
    }

    fun getCorpWrapper(id: Long): CorporationInfo? {
        try {
            val json = ask("corporation", id, 0)
            if (json != null) {
                return jacksonObjectMapper().readValue<CorporationInfoWrapper>(json).info
            } else {
                return null
            }
        } catch (e: Exception) {
            println("CRAWLER fails with $id and message ${e.message} of ${e.cause}")
            return null
        }
    }

    fun getCharacterWrapper(id: Long): CharacterWrapper? {
        val json = ask("character", id, 0)
        if (json != null) {
            return jacksonObjectMapper().readValue<CharacterWrapper>(json)
        } else {
            return null
        }
    }

}