package ru.finnetrolle.wheretheyfrom.service.entitysource

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.finnetrolle.wheretheyfrom.service.Alliance
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

abstract class AbstractEntitySource<KEY,VALUE> {

    private val data = ConcurrentHashMap<KEY, VALUE>()

    protected fun getData() = data

    fun invalidate() = data.clear()

    fun size() = data.size

    fun all(): MutableCollection<VALUE> {
        return data.values
    }

    fun save(filename: String) {
        jacksonObjectMapper().writeValue(File(filename), data.values)
//        val mapper = ObjectMapper()
//        mapper.writeValue(File(filename), listOf(getData().values))
    }

    fun load(filename: String) {
        val obj = getType(filename)
        getData().clear()
        obj.forEach { e -> getData().put(extractKey(e), e) }
    }

    protected abstract fun getType(filename: String): List<VALUE>

    fun get(id: KEY): VALUE? {
        val value = data[id]
        if (value != null) {
//            print(".")
            return value
        } else {
//            print(":")
            return gatherAndPutIfOk(id)
        }
    }

    private fun gatherAndPutIfOk(id: KEY): VALUE? {
        val gathered = gather(id)
        if (gathered != null) {
            data.put(extractKey(gathered), gathered)
        }
        return gathered
    }

    abstract protected fun extractKey(obj: VALUE): KEY

    abstract protected fun gather(id: KEY): VALUE?

}