package ru.finnetrolle.wheretheyfrom.service;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Licence: MIT
 * Where-They-From
 * Created by maxsyachin
 */
public class Par {

    public static <I,O> List<O> go(List<I> objs, Function<I, O> func) {
        return objs.parallelStream().map(func).collect(toList());
    }

}
