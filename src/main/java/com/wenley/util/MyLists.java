package com.wenley.util;

import java.util.List;
import java.util.Random;

public class MyLists {
  public static <T> T sample(List<T> collection, Random random) {
    int size = collection.size();
    return collection.get(random.nextInt(size));
  }
}
