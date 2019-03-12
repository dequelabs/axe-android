package com.deque.axe.android.colorcontrast;

import java.util.ArrayList;
import java.util.List;

public class ColorContrastResult {

  private List<ColorPair> alternatives = new ArrayList<>();

  private ColorPair mostContrastingPair;

  @ColorContrastRunner.Confidence
  String confidence;

  private transient int numDifferentBackgroundColors = 1;

  private transient int numVisiblyDifferentTextColors = 1;

  public ColorContrastResult() {
    this.confidence = ColorContrastRunner.Confidence.NONE;
  }

  ColorContrastResult add(final ColorPair newColorPair) {

    if (mostContrastingPair == null) {

      mostContrastingPair = newColorPair;

      this.confidence = ColorContrastRunner.Confidence.HIGH;
    }

    if (mostContrastingPair.getContrast() < newColorPair.getContrast()) {
      mostContrastingPair = newColorPair;
    }

    for (final ColorPair alternativePair : alternatives) {

      if (!alternativePair.isVisiblySimilar(newColorPair)) {
        numDifferentBackgroundColors++;
        numVisiblyDifferentTextColors++;
      }
    }

    if (numDifferentBackgroundColors > 3 || numVisiblyDifferentTextColors > 3) {
      confidence = ColorContrastRunner.Confidence.LOW;
    } else if (numDifferentBackgroundColors > 1 || numVisiblyDifferentTextColors > 1) {
      confidence = ColorContrastRunner.Confidence.MID;
    }

    alternatives.add(newColorPair);

    return this;
  }

  /**
   * After running color contrast, get the most likely found background color.
   * @return The most likely background color.
   */
  public AxeColor getMostLikelyBackgroundColor() {

    if (ColorContrastRunner.Confidence.NONE.equals(confidence)) {
      return null;
    } else {
      return mostContrastingPair.backgroundColor;
    }
  }

  /**
   * After running color contrast, get the most likely found text color.
   * @return The most likely text color.
   */
  public AxeColor getMostLikelyTextColor() {

    if (ColorContrastRunner.Confidence.NONE.equals(confidence)) {
      return null;
    } else {
      return mostContrastingPair.textColor;
    }
  }

  public @ColorContrastRunner.Confidence String getConfidence() {
    return confidence;
  }
}
