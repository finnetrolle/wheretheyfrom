package ru.finnetrolle.wheretheyfrom.service

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
class EveGateHandler: DefaultHandler() {

    val list = mutableListOf<String>()

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (qName.equals("div")) {
            if (attributes != null) {
                for (i in 0..attributes.length) {
                    println(attributes.getQName(i) + " = " + attributes.getValue(i))
                }
            }
            println()
        }
//        if (qName.equals("div") && attributes != null && attributes!!.getValue("class").equals("employmentHistoryCopy")) {
//            list.add("some")
//        }
    }
}