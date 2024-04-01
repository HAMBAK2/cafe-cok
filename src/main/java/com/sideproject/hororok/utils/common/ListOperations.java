package com.sideproject.hororok.utils.common;

import java.util.List;

public class ListOperations {

    public static <T> void removeCommonElements(List<T> target, List<T>... others) {
        for (List<T> other : others) {
            target.removeAll(other);
        }
    }
}
