package ru.finnetrolle.wheretheyfrom.restful

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import ru.finnetrolle.wheretheyfrom.service.Crawler

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */

@Controller
@RequestMapping("/test")
class TestResource @Autowired constructor (val crawler: Crawler) {

    @RequestMapping(method = arrayOf(RequestMethod.GET))
    @ResponseBody
    fun test():ResponseEntity<Set<String>> {
//        return ResponseEntity.ok(crawler.getUsersOfAlly(1411711376))
        return ResponseEntity.ok(crawler.getVisitedCorps(1411711376))
    }


}