package ru.finnetrolle.wheretheyfrom.service

import ru.finnetrolle.wheretheyfrom.service.entitysource.AllianceEntitySource
import ru.finnetrolle.wheretheyfrom.service.entitysource.CorporationEntitySource
import java.util.*

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

data class Alliance(val name: String, val id: Long, val corpIds: Set<Long>, val charIds: Set<Long>) {companion object{}}
data class Corporation(val name: String, val id: Long, val allyId: Long) {companion object{}}
data class Pilot(val name: String, val id: Long, val corp: Long, val ally: Long, val occupation: List<Occupation>) {companion object{}}
data class Occupation(val corpId: Long, val allyId: Long?, val from: Date, val till: Date) {companion object{}}

fun Alliance.Companion.from(ally: Crawler.AllyWrapper?) =
    if (ally == null) null
    else Alliance(ally.info.name, ally.info.alliance_id.toLong(),
                ally.characters.map { c -> c.corporation_id.toLong() }.toSet(),
                ally.characters.map { c -> c.character_id.toLong() }.toSet())

fun Corporation.Companion.from(corp: Crawler.CorporationInfo?) =
        if (corp == null) null
        else Corporation(corp.name, corp.corporation_id.toLong(), corp.alliance_id.toLong())
fun Corporation.Companion.empty() = Corporation("", 0L, 0L)

fun Pilot.Companion.from(cw: Crawler.CharacterWrapper?, corpSource: CorporationEntitySource) =
        if (cw == null) null
        else {
            val occ = cw.history.map { o ->
                val corp = corpSource.get(o.corporation_id.toLong())
                val allyId = if (corp != null) corp.allyId else null
                Occupation(o.corporation_id.toLong(), allyId, Date(), Date())
            }
            Pilot(
                    cw.info.name,
                    cw.info.character_id.toLong(),
                    cw.info.corporation_id.toLong(),
                    cw.info.alliance_id.toLong(), occ)
        }