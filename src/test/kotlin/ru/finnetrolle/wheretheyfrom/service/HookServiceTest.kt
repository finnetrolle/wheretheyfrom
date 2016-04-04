package ru.finnetrolle.wheretheyfrom.service

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
class HookServiceTest {

    var sdf = SimpleDateFormat("yyyy-MM-dd")
    
    @Before
    fun init() {
        
    }

    //    [     ]
    //  < + > < + >   1   2
    // <->  <+>  <->  3 4 5
    // <     +     >    6
    
    @Test
    fun check() {
        val hs = HookService()
        val occs = mutableListOf(

                EveGateParser.HistoryElement("1", sdf.parse("1999-06-01"), sdf.parse("2000-06-01")),
                EveGateParser.HistoryElement("2", sdf.parse("2000-06-01"), sdf.parse("2001-06-01")),
                EveGateParser.HistoryElement("3", sdf.parse("1999-06-01"), sdf.parse("1999-09-01")),
                EveGateParser.HistoryElement("4", sdf.parse("2000-03-01"), sdf.parse("2000-09-01")),
                EveGateParser.HistoryElement("5", sdf.parse("2001-03-01"), sdf.parse("2001-06-01")),
                EveGateParser.HistoryElement("6", sdf.parse("1999-06-01"), sdf.parse("2001-06-01"))
        )
        val corp = Corporation("A", 0, 0, occs)
        val s = hs.historyFromCorp(Occupation(1, 0, sdf.parse("2000-01-01"), sdf.parse("2001-01-01")), corp)
        assert(s.size == 4)
    }

    @Test
    fun splitTwo() {
        val hs = HookService()
        val occs = mutableListOf(
                EveGateParser.HistoryElement("before",  sdf.parse("2009-01-01"), sdf.parse("2010-01-01")),
                EveGateParser.HistoryElement("1",       sdf.parse("2010-01-01"), sdf.parse("2011-01-01")),
                EveGateParser.HistoryElement("2",       sdf.parse("2011-01-01"), sdf.parse("2012-01-01")),
                EveGateParser.HistoryElement("after",   sdf.parse("2012-01-01"), sdf.parse("2013-01-01"))
        )
        val corp = Corporation("First", 0, 0, occs)

        val s = hs.historyFromCorp(Occupation(1, 0, sdf.parse("2010-06-01"), sdf.parse("2011-06-01")), corp)
        assert(s.size == 2)
        val first = s.get(0)
        val second = s.get(1)
        assertEquals(sdf.parse("2010-06-01"), first.from)
        assertEquals(sdf.parse("2011-01-01"), first.to)
        assertEquals("1", first.allyName)
        assertEquals(sdf.parse("2011-01-01"), second.from)
        assertEquals(sdf.parse("2011-06-01"), second.to)
        assertEquals("2", second.allyName)
    }
    
    
}