package com.deque.axe.android.wrappers;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountMap<T> extends HashMap<T, Integer> {

  public void increment(@NonNull final T key) {
    increment(key, 1);
  }

  /**
   * Increment key by more than 1 at a time.
   * @param key The key to increment.
   * @param byValue By how much.
   */
  public void increment(@NonNull final T key, int byValue) {

    if (!containsKey(key)) {
      put(key, byValue);
    } else {
      put(key, get(key) + byValue);
    }
  }

  /**
   * A list of entries, sorted by the value.
   * @return Don't make me say it again.
   */
  public List<Map.Entry<T, Integer>> entriesSortedByValue() {

    List<Map.Entry<T, Integer>> colorEntries = new ArrayList<>(entrySet());

    Collections.sort(colorEntries, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

    return colorEntries;
  }

  /**
   * Yeah, you heard me, they're sorted by value. Get it?
   * @param ifEqualByThis Oh, yeah, if they're equal, sort by this.
   * @return More sorted entries.
   */
  public List<Map.Entry<T, Integer>> entriesSortedByValue(final Comparator<T> ifEqualByThis) {

    List<Map.Entry<T, Integer>> colorEntries = new ArrayList<>(entrySet());

    Collections.sort(colorEntries, (o1, o2) -> {
      int result = o2.getValue().compareTo(o1.getValue());

      if (result == 0) {
        return ifEqualByThis.compare(o1.getKey(), o2.getKey());
      } else {
        return result;
      }
    });

    return colorEntries;
  }
}
