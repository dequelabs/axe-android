package com.deque.axe.android.colorcontrast;

import java.util.ArrayList;

/**
 * Collects a series of colors that are different.
 */
class Transition {

  private final ArrayList<AxeColor> colors = new ArrayList<>();

  AxeColor mostContrastingColor;

  AxeColor startColor;

  private int size;

  private boolean isConnecting =  false;

  private boolean isClosed = false;

  private boolean isMountainShaped = true;

  private boolean isIncreasingInContrast = true;

  Transition(AxeColor color) {
    startColor = color;
    mostContrastingColor = color;
    size = 1;
  }

  final void addColor(final AxeColor color) {

    size++;

    if (!isClosed && size > ColorContrastConfig.MAX_TEXT_THICKNESS
        || (size > 1 && startColor.equals(color))) {

      if (startColor.equals(color)) {
        isConnecting = true;
      }

      isClosed = true;

      return;
    }

    if (mostContrastingColor.contrast(startColor) < color.contrast(startColor)) {
      mostContrastingColor = color;

      if (!isIncreasingInContrast) {
        isMountainShaped = false;
      }
    } else {
      isIncreasingInContrast = false;
    }

    colors.add(color);
  }

  /**
   * Closed transitions have cycled back to the color they started from.
   * They are also thinner than a configurable number of pixels.
   * @return True if this is a closed Transition.
   */
  final boolean isClosed() {
    return isClosed;
  }

  /**
   * Connecting transitions have originating and closing colors that are the same.
   * @return True if the transition loops back on itself.
   */
  final boolean isConnecting() {
    return isConnecting;
  }

  final boolean isConsequential() {
    return colors.size() > 2 && isMountainShaped();
  }

  /**
   * Mountain shaped color transitions have their peak contrast difference in the middle.
   * @return True if the color transition is mountain shaped.
   */
  public final boolean isMountainShaped() {
    return isMountainShaped;
  }
}
