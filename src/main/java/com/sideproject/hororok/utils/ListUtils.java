package com.sideproject.hororok.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListUtils {

    public static boolean areListEqual(List<String> list1, List<String> list2) {

        if (list1.size() != list2.size()) return false;

        List<String> sortedList1 = new ArrayList<>(list1);
        List<String> sortedList2 = new ArrayList<>(list2);
        Collections.sort(sortedList1);
        Collections.sort(sortedList2);

        return sortedList1.equals(sortedList2);
    }

    public static <T> List<T> getEquals(List<T> first, List<T> second) {

        return first.stream()
                .filter(second::contains)
                .collect(Collectors.toList());
    }

}
