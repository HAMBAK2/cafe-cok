package com.sideproject.cafe_cok.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListUtil {

    public static boolean areListEqual(final List<String> list1,
                                       final List<String> list2) {

        if (list1.size() != list2.size()) return false;

        List<String> sortedList1 = new ArrayList<>(list1);
        List<String> sortedList2 = new ArrayList<>(list2);
        Collections.sort(sortedList1);
        Collections.sort(sortedList2);

        return sortedList1.equals(sortedList2);
    }
}
