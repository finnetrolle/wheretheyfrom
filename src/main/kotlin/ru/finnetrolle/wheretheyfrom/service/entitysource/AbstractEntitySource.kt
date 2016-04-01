package ru.finnetrolle.wheretheyfrom.service.entitysource

import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

abstract class AbstractEntitySource<KEY,VALUE> {

    private val data = ConcurrentHashMap<KEY, VALUE>()

    fun invalidate() = data.clear()

    fun size() = data.size

    fun get(id: KEY): VALUE? {
        val value = data[id]
        if (value != null) {
            print(".")
            return value
        } else {
            print(":")
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