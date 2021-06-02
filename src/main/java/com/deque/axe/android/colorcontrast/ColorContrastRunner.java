package com.deque.axe.android.colorcontrast;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.deque.axe.android.wrappers.CountMap;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ColorContrastRunner {

  private final Map<AxeColor, Transition> openTransitions = new HashMap<>();

  private final CountMap<AxeColor> countExactColors = new CountMap<>();

  private final CountMap<ColorPair> countExactPairs = new CountMap<>();

  void onPixel(
      @NonNull final AxeColor color,
      @Nullable final AxeColor previousColor
  ) {

    countExactColors.increment(color);

    List<Transition> newlyClosedTransitions = new ArrayList<>();

    for (Transition transition : openTransitions.values()) {

      transition.addColor(color);

      if (transition.isClosed()) {

        newlyClosedTransitions.add(transition);

        if (transition.isConnecting() && transition.isConsequential()) {

          final ColorPair colorPair = new ColorPair(
              transition.startColor,
              transition.mostContrastingColor
          );


          final ColorPair alternateColorPair = new ColorPair(
                  transition.mostContrastingColor,
                  transition.startColor
          );

          if (countExactPairs.containsKey(alternateColorPair)) {
            countExactPairs.increment(alternateColorPair);
          } else {
            countExactPairs.increment(colorPair);
          }
        }
      }
    }

    if (previousColor != null && !color.equals(previousColor)) {
      openTransitions.put(previousColor, new Transition(previousColor));
    }

    for (Transition transition : newlyClosedTransitions) {
      openTransitions.remove(transition.startColor);
    }
  }

  void onRowBegin() {
    openTransitions.clear();
    countExactPairs.clear();
    countExactColors.clear();
  }

  private AxeColor backgroundColorByCount() {

    if (countExactColors.size() <= 0) {
      return null;
    }

    List<Map.Entry<AxeColor, Integer>> sortedEntries = countExactColors.entriesSortedByValue();

    return sortedEntries.get(0).getKey();
  }

  // Returns true when entries have lead to a confident conclusion about Text and Background color.
  ColorContrastResult onRowEnd() {

    ColorContrastResult result = new ColorContrastResult();

    CountMap<ColorPair> pairsWithSimilarTextColor = new CountMap<>();

    for (Map.Entry<ColorPair, Integer> entry1 : countExactPairs.entrySet()) {
      for (Map.Entry<ColorPair, Integer> entry2 : countExactPairs.entrySet()) {
        if (entry1.getKey().backgroundColor.equals(entry2.getKey().backgroundColor)) {
          if (entry1.getKey().textColor.isVisiblySameColor(entry2.getKey().textColor)) {
            pairsWithSimilarTextColor.increment(entry1.getKey(), entry2.getValue());
          }
        }
      }
    }

    List<Map.Entry<ColorPair, Integer>> sortedByValueAndContrast = pairsWithSimilarTextColor
        .entriesSortedByValue((o1, o2) -> o1.compareTo(o2));

    if (sortedByValueAndContrast.size() <= 0) {
      return result;
    }

    final Set<ColorPair> resultPairs = new HashSet<>();

    Map.Entry<ColorPair, Integer> firstEntry = null;

    for (Map.Entry<ColorPair, Integer> entry : sortedByValueAndContrast) {

      final ColorPair colorPair = entry.getKey();

      final Integer occurrenceCount = entry.getValue();

      if (occurrenceCount < ColorContrastConfig.MIN_CHARACTERS) {
        break;
      }

      final Integer adjustedOccurrenceCount = occurrenceCount
          * ColorContrastConfig.TRANSITION_COUNT_DOMINANCE_FACTOR;


      if (firstEntry != null) {
        if (adjustedOccurrenceCount < firstEntry.getValue()) {
          break;
        }
      }

      resultPairs.add(colorPair);

      if (firstEntry == null) {
        firstEntry = entry;
      }
    }

    for (final ColorPair colorPair : resultPairs) {
      result.add(colorPair);
    }

    return result;
  }

  ColorContrastResult onRowEnd(AxeColor actualTextColor) {

    ColorContrastResult result = new ColorContrastResult();

    CountMap<ColorPair> pairsWithSimilarTextColor = new CountMap<>();

    for (Map.Entry<ColorPair, Integer> entry1 : countExactPairs.entrySet()) {
      for (Map.Entry<ColorPair, Integer> entry2 : countExactPairs.entrySet()) {
        if (entry1.getKey().backgroundColor.equals(entry2.getKey().backgroundColor)) {
          if (entry1.getKey().textColor.isVisiblySameColor(entry2.getKey().textColor)) {
            pairsWithSimilarTextColor.increment(entry1.getKey(), entry2.getValue());
          }
        }
      }
    }

    List<Map.Entry<ColorPair, Integer>> sortedByValueAndContrast = pairsWithSimilarTextColor
            .entriesSortedByValue((o1, o2) -> o1.compareTo(o2));

    List<Map.Entry<ColorPair, Integer>> filteredList = new ArrayList<>();

    for (Map.Entry<ColorPair, Integer> entry : sortedByValueAndContrast) {
      ColorPair colorPair = entry.getKey();
      if (colorPair.backgroundColor.equals(actualTextColor) || colorPair.textColor.equals(actualTextColor)) {
        if (colorPair.backgroundColor.equals(actualTextColor)) {
          filteredList.add(new Map.Entry<ColorPair, Integer>() {
            @Override
            public ColorPair getKey() {
              return new ColorPair(entry.getKey().textColor, entry.getKey().backgroundColor);
            }

            @Override
            public Integer getValue() {
              return entry.getValue();
            }

            @Override
            public Integer setValue(Integer value) {
              return null;
            }
          });
        } else {
          filteredList.add(entry);
        }
      }
    }

    if (filteredList.size() <= 0) {
      return result;
    }

    final Set<ColorPair> resultPairs = new HashSet<>();

    Map.Entry<ColorPair, Integer> firstEntry = null;

    for (Map.Entry<ColorPair, Integer> entry : filteredList) {

      final ColorPair colorPair = entry.getKey();

      final Integer occurrenceCount = entry.getValue();

      if (occurrenceCount < ColorContrastConfig.MIN_CHARACTERS) {
        break;
      }

      final Integer adjustedOccurrenceCount = occurrenceCount
              * ColorContrastConfig.TRANSITION_COUNT_DOMINANCE_FACTOR;


      if (firstEntry != null) {
        if (adjustedOccurrenceCount < firstEntry.getValue()) {
          break;
        }
      }

      resultPairs.add(colorPair);

      if (firstEntry == null) {
        firstEntry = entry;
      }
    }

    for (final ColorPair colorPair : resultPairs) {
      result.add(colorPair);
    }

    return result;
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
      Confidence.NONE,
      Confidence.LOW,
      Confidence.MID,
      Confidence.HIGH
  })
  public @interface Confidence {
    String NONE = "None";
    String LOW = "Low";
    String MID = "Mid";
    String HIGH = "High";
  }

  @Retention(RetentionPolicy.SOURCE)
  @StringDef({
      Type.BACKGROUND,
      Type.TEXT,
      Type.UNKNOWN
  })
  public @interface Type {
    String BACKGROUND = "Background Color";
    String TEXT = "Text Color";
    String UNKNOWN = "Unknown";
  }

}
