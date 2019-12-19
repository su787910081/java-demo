package com.suyh;

import java.util.List;

public class Utility {
    public static <T> boolean listIsNullOrEmpty(List<T> objectList) {
        return objectList == null || objectList.isEmpty();
    }

    public static <T> boolean listIsNotNullOrEmpty(List<T> objectList) {
        return objectList != null && !objectList.isEmpty();
    }
}